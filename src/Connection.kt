package com.tlife

import com.tlife.model.RoomModel
import io.ktor.websocket.*
import java.util.concurrent.atomic.AtomicInteger

data class Connection(val session: DefaultWebSocketServerSession)