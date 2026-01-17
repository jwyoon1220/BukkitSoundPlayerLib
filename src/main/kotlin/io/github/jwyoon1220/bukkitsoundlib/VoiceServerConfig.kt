package io.github.jwyoon1220.bukkitsoundlib

data class VoiceServerConfig(
    val host: String = "0.0.0.0",
    val port: Int,
    val path: String = "/",
    val pingSeconds: Long = 15,
    val timeoutSeconds: Long = 30
)
