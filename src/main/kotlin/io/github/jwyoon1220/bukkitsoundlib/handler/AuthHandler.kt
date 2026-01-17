package io.github.jwyoon1220.bukkitsoundlib.handler

import java.util.UUID

fun interface AuthHandler {
    fun authenticate(rawMessage: String): UUID?
}
