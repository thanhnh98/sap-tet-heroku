package com.tlife

import com.tlife.model.RoomModel
import io.ktor.websocket.*
import java.util.concurrent.atomic.AtomicInteger

class Connection(val session: DefaultWebSocketServerSession) {
    companion object {
        val room: RoomModel = RoomModel()
    }
}