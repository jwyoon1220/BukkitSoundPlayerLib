package io.github.jwyoon1220.bukkitsoundlib.handler

import io.github.jwyoon1220.bukkitsoundlib.model.VoiceClient

fun interface MessageHandler {
    suspend fun onMessage(client: VoiceClient, text: String)
}
