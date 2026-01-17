package io.github.jwyoon1220.bukkitsoundlib.def

import io.github.jwyoon1220.bukkitsoundlib.handler.AuthHandler
import java.util.UUID

object DefaultAuthHandler : AuthHandler {
    override fun authenticate(rawMessage: String): UUID? =
        if (rawMessage.startsWith("AUTH:"))
            UUID.fromString(rawMessage.removePrefix("AUTH:"))
        else null
}