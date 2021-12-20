package com.tlife.route

import com.tlife.Connection
import com.tlife.model.RoomModel
import com.tlife.store.RoomStore
import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*

fun joinRoom(roomId: Long? = null, connection: Connection): suspend DefaultWebSocketServerSession.() -> Unit =  {

    val room: RoomModel = when {
        roomId == null || roomId <= 0 -> RoomModel()
        !RoomStore.isRoomAlreadyExisted(roomId) -> RoomModel()
        else -> RoomStore.findRoomById(roomId)
    }

    val thisConnection = Connection(this)
    println("Adding user into session: ${this.call.parameters.toString()}")
    try {
        for (frame in incoming) {
            frame as? Frame.Text ?: continue
            connection.session.send(room.toString())
        }
    } catch (e: Exception) {
        println(e.localizedMessage)
    } finally {
        println("Removing $thisConnection!")
    }
}

fun createRoom(connection: Connection): suspend DefaultWebSocketServerSession.() -> Unit =  {

    val room: RoomModel = RoomModel()

    val thisConnection = Connection(this)
    println("Adding user into session: ${this.call.parameters.toString()}")
    try {
        for (frame in incoming) {
            frame as? Frame.Text ?: continue
            connection.session.send(room.toString())
        }
    } catch (e: Exception) {
        println(e.localizedMessage)
    } finally {
        println("Removing $thisConnection!")
    }
}