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
    var appVersionModel = OkXeVersionModel(
        false,
        false,
        "3.10.1"
    )
    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/app-config") {
            call.respondText(
                OkxeModel(
                    data = appVersionModel
                ).toJson(),
                contentType = ContentType.Text.Plain)
        }

        get("/app-config/code/{code}") {
            val versioncode = this.call.request.call.parameters["code"]?.toString()?:""

            appVersionModel = appVersionModel.copy(
                lastest_version = versioncode
            )
            call.respondText(
                OkxeModel(
                    data = appVersionModel
                ).toJson(),
                contentType = ContentType.Text.Plain)
        }

        get("/app-config/force/{force}") {
            val forceUpdate = this.call.request.call.parameters["force"]?.toBoolean()?:false

            appVersionModel = appVersionModel.copy(
                force_update = forceUpdate
            )
            call.respondText(
                OkxeModel(
                    data = appVersionModel
                ).toJson(),
                contentType = ContentType.Text.Plain)
        }

        get("/app-config/recommended/{recommended}") {
            val recommended = this.call.request.call.parameters["recommended"]?.toBoolean()?:false

            appVersionModel = appVersionModel.copy(
                recommend_update = recommended
            )
            call.respondText(
                OkxeModel(
                    data = appVersionModel
                ).toJson(),
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

data class OkxeModel(
    val result: String = "ok",
    val result_code: Int = 1,
    val data: OkXeVersionModel
)

data class OkXeVersionModel(
    val force_update: Boolean,
    val recommend_update: Boolean,
    val lastest_version: String
)
