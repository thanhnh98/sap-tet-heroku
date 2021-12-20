package com.tlife.session

import com.tlife.model.RoomModel

data class RoomSession(
    val id: String = System.currentTimeMillis().rem(1000000L).toString(),
    var data: RoomModel
)