package io.github.jwyoon1220.bukkitsoundlib

object VoicePackets {

    fun play(url: String, repeat: Boolean = false) = """
        {
          "type": "play",
          "url": "$url",
          "repeat": $repeat
        }
    """.trimIndent()

    fun stop() = """{ "type": "stop" }"""
}
