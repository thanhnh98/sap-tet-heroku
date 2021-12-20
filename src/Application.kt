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
    embeddedServer(Netty, port = 8080, "127.0.0.1") {
        module()
    }.start(wait = true)
}
@Suppress("unused") // Referenced in application.conf
fun Application.module(testing: Boolean = false) {
    install(WebSockets)
    install(Sessions)

    routing {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        webSocket("/join/{room_id}") {
            val connection = Connection(this)
            connections += connection

            val roomId = this.call.request.call.parameters["room_id"]?.toLong()?:0L
            val playerName = this.call.request.call.parameters["name"].toString()
            if (!RoomStore.isRoomAlreadyExisted(roomId)){
                connection.session.send("Không tìm thấy id $roomId")
            }
            else {
                val room = RoomStore.findRoomById(roomId)
                if (!room.isFull()){
                    if (room.firstPlayer == null)
                        room.firstPlayer = PlayerModel(playerName)
                    else if (room.secondPlayer == null)
                        room.secondPlayer = PlayerModel(playerName)
                }
                println("data room: ${room}")

                try {
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val textData = frame.readText()
                        val step = fromJson<StepModel>(textData)
                        room.currentMatch.checkIn(step)
                        connections.forEach {
                            it.session.send(room.toJson())
                        }
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                } finally {
                    println("Removing $connection!")
                }
            }
        }

        webSocket("/create") {
            val room: RoomModel = RoomStore.addRoom(RoomModel())

            val connection = Connection(this)

            try {
                connection.session.send("created room: ${room}, total: ${RoomStore.getListRooms().size}")
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {

            }
        }

        webSocket("/destroy/{room_id}") {
            val roomId = this.call.request.call.parameters["room_id"]?.toLong()?:0L
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
            }
            else {
                connection.session.send("Phòng $roomId không tồn tại")
            }
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
