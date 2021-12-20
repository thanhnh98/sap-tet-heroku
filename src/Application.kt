package com.tlife

import com.tlife.route.createRoom
import com.tlife.route.joinRoom
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import java.util.*
import kotlin.collections.LinkedHashSet

fun main() {
    embeddedServer(Netty, port = 8080, "192.168.0.97") {
        install(WebSockets)
        install(Sessions)
        module()
    }.start(wait = true)
}
@Suppress("unused") // Referenced in application.conf
fun Application.module(testing: Boolean = false) {
    routing {
        webSocket("/join/{room_id}") {
            val connection = Connection(this)
            val roomId = this.call.request.call.parameters["room_id"]?.toLong()?:0L
            joinRoom(roomId, connection)
        }
        webSocket("/create") {
            val connection = Connection(this)
            createRoom(connection)
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/test") {
            call.respond("Hello 4, test server deployment")
        }
    }
}
