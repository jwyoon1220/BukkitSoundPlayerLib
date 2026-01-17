package io.github.jwyoon1220.bukkitsoundlib.model

import io.ktor.websocket.WebSocketSession
import java.util.UUID

data class VoiceClient(
    val uuid: UUID,
    val session: WebSocketSession
)
