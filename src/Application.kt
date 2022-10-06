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
    var enable_in_app = false

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

        get("/app-config/update") {
            val recommended = this.call.request.queryParameters["recommend_update"]?.toBoolean()
            val forceUpdate = this.call.request.call.parameters["force_update"]?.toBoolean()
            val versioncode = this.call.request.call.parameters["lastest_version"]?.toString()

            recommended?.apply {
                appVersionModel = appVersionModel.copy(
                    recommend_update = this
                )
            }

            forceUpdate?.apply {
                appVersionModel = appVersionModel.copy(
                    force_update = this
                )
            }

            versioncode?.apply {
                appVersionModel = appVersionModel.copy(
                    lastest_version = this
                )
            }

            call.respondText(
                OkxeModel(
                    data = appVersionModel,
                ).toJson(),
                contentType = ContentType.Text.Plain)
        }

        get("/feature-flag") {
            val enableInApp = this.call.request.call.parameters["in-app"]?.toBoolean()

            enableInApp?.apply {
                enable_in_app = this
            }

            call.respondText(
                "{\n" +
                        "\"success\": true,\n" +
                        "\"result_code\": 1,\n" +
                        "\"result\": \"Ok\",\n" +
                        "\"data\": {\n" +
                            "\"feature_flags\": {\n" +
                            "\"module-insurance\": true,\n" +
                            "\"module-cash-loan\": true,\n" +
                            "\"module-in-app-update\": $enable_in_app\n" +
                        "}\n" +
                        "}\n" +
                    "}",
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
