package io.github.jwyoon1220.bukkitsoundlib

import io.github.jwyoon1220.bukkitsoundlib.def.DefaultAuthHandler
import io.github.jwyoon1220.bukkitsoundlib.handler.AuthHandler
import io.github.jwyoon1220.bukkitsoundlib.handler.ConnectionListener
import io.github.jwyoon1220.bukkitsoundlib.handler.MessageHandler
import io.github.jwyoon1220.bukkitsoundlib.model.VoiceClient
import io.ktor.server.application.install
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach
import java.time.Duration
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.toKotlinDuration

class VoiceServer(
    private val config: VoiceServerConfig,
    private val authHandler: AuthHandler = DefaultAuthHandler,
    private val messageHandler: MessageHandler? = null,
    private val connectionListener: ConnectionListener? = null,
    private val logger: (String) -> Unit = {}
) {

    private val clients = ConcurrentHashMap<UUID, VoiceClient>()
    private var engine: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>? = null

    fun start() {
        if (engine != null) return

        engine = embeddedServer(
            Netty,
            host = config.host,
            port = config.port
        ) {
            install(WebSockets) {
                pingPeriod = Duration.ofSeconds(config.pingSeconds).toKotlinDuration()
                timeout = Duration.ofSeconds(config.timeoutSeconds).toKotlinDuration()
            }

            routing {
                webSocket(config.path) {
                    var client: VoiceClient? = null

                    try {
                        incoming.consumeEach { frame ->
                            if (frame is Frame.Text) {
                                val text = frame.readText()

                                if (client == null) {
                                    val uuid = authHandler.authenticate(text)
                                        ?: return@consumeEach

                                    client = VoiceClient(uuid, this)
                                    clients[uuid] = client!!
                                    connectionListener?.onConnect(client!!)
                                    logger("Client authenticated: $uuid")
                                } else {
                                    messageHandler?.onMessage(client!!, text)
                                }
                            }
                        }
                    } finally {
                        client?.let {
                            clients.remove(it.uuid)
                            connectionListener?.onDisconnect(it)
                            logger("Client disconnected: ${it.uuid}")
                        }
                    }
                }
            }
        }.start(false)
    }

    fun stop() {
        engine?.stop()
        engine = null
        clients.clear()
    }

    /* ======================
       Public API
       ====================== */

    fun getClient(uuid: UUID): VoiceClient? = clients[uuid]

    fun getClients(): Collection<VoiceClient> = clients.values

    suspend fun send(uuid: UUID, json: String) {
        clients[uuid]?.session?.send(Frame.Text(json))
    }

    suspend fun broadcast(json: String) {
        val frame = Frame.Text(json)
        clients.values.forEach { it.session.send(frame) }
    }
}
