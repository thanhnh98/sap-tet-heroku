package com.tlife

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import io.ktor.websocket.*

fun main() {
    val isDev = false
    embeddedServer(Netty, port = 8080, if (isDev) "127.0.0.1" else "192.168.0.97") {
        module()
    }.start(wait = true)
}
@Suppress("unused") // Referenced in application.conf
fun Application.module(testing: Boolean = false) {
    install(WebSockets)
    install(Sessions)

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/version") {
            call.respondText("in side version", contentType = ContentType.Text.Plain)
        }
    }
}
