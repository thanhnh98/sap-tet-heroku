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
    val isDev = true
    embeddedServer(Netty, port = 8080, if (isDev) "127.0.0.1" else "192.168.0.97") {
        module()
    }.start(wait = true)
}
@Suppress("unused") // Referenced in application.conf
fun Application.module(testing: Boolean = false) {
    install(WebSockets)
    install(Sessions)
    var appVersionModel = AppVersionModel(
        324,
        "3.2.4",
        false
    )
    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/app-config") {
            call.respondText(
                Version(appVersionModel).toJson(),
                contentType = ContentType.Text.Plain)
        }

        get("/app-config/{code}") {
            val versioncode = this.call.request.call.parameters["code"]?.toInt()?:0

            appVersionModel = appVersionModel.copy(
                lastVersionCode = versioncode
            )
            call.respondText(
                Version(appVersionModel).toJson(),
                contentType = ContentType.Text.Plain)
        }
    }
}

data class AppVersionModel(
    val lastVersionCode: Int,
    val lastVersionName: String,
    val forceUpdate: Boolean
)

data class Version(
    val version: AppVersionModel
)