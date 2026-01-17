package io.github.jwyoon1220.bukkitsoundlib.handler

import io.github.jwyoon1220.bukkitsoundlib.model.VoiceClient

interface ConnectionListener {
    fun onConnect(client: VoiceClient)
    fun onDisconnect(client: VoiceClient)
}