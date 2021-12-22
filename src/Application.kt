package com.tlife

import com.tlife.model.PlayerModel
import com.tlife.model.RoomModel
import com.tlife.model.StepModel
import com.tlife.route.createRoom
import com.tlife.route.joinRoom
import com.tlife.store.RoomStore
import com.tlife.store.SessionStore
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
        module()
    }.start(wait = true)
}
@Suppress("unused") // Referenced in application.conf
fun Application.module(testing: Boolean = false) {
    install(WebSockets)
    install(Sessions)

    routing {
        webSocket("/join/{room_id}") {
            val roomId = this.call.request.call.parameters["room_id"]?.toLong() ?: 0L
            val connection = Connection(this)

            if (!RoomStore.isRoomAlreadyExisted(roomId)) {
                connection.session.send("Không tìm thấy id $roomId")
                connection.session.close()
                return@webSocket
            }

            if (RoomStore.findRoomById(roomId).isFull()) {
                connection.session.send("Phòng $roomId đã đủ người")
                connection.session.close()
                return@webSocket
            }

            val room = RoomStore.findRoomById(roomId)

            if (room.firstPlayer == null)
                room.addFirstPlayer(PlayerModel("Người chơi 1", connection)).apply {
                    send("Người chơi 1 đã vào phòng")
                }
            else if (room.secondPlayer == null)
                room.addSecondPlayer(PlayerModel("Người chơi 2", connection)).apply {
                    send("Người chơi 2 đã vào phòng")
                    room.firstPlayer?.connection?.session?.send("Người chơi 2 đã vào phòng")
                }

            try {
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val textData = frame.readText()
                    val step = fromJson<StepModel>(textData)
                    room.currentMatch.checkIn(step)
                    room.firstPlayer?.connection?.session?.send(room.toString())
                    room.secondPlayer?.connection?.session?.send(room.toString())
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $connection!")
            }
        }

        webSocket("/destroy/{room_id}") {
            val roomId = this.call.request.call.parameters["room_id"]?.toLong() ?: 0L
            val connection = Connection(this)

            if (RoomStore.isRoomAlreadyExisted(roomId)) {
                try {
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        connection.session.send("deleted room: ${RoomStore.destroyRoomById(roomId)}")
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                } finally {

                }
            } else {
                connection.session.send("Phòng $roomId không tồn tại")
            }
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/room/create") {
            val room: RoomModel = RoomStore.addRoom(RoomModel())
            call.respond(room.toJson())

        }
        get("/room/chat") {
            val message = call.parameters.get("message")
            val roomId = call.parameters.get("room_id")

            call.respond("room: $roomId, msg: $message")

        }
    }
}
