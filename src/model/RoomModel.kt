package com.tlife.model

data class RoomModel(
    var roomId: Long = System.currentTimeMillis().rem(1000000L),
    var status: RoomStatus = RoomStatus.WAITING,
    var firstPlayer: PlayerModel? = null,
    var secondPlayer: PlayerModel? = null,
    var winner: PlayerModel? = null
){
    fun isFull(): Boolean = firstPlayer != null && secondPlayer != null
}

enum class RoomStatus(val value: String){
    WAITING("waiting"),
    CLOSED("closed"),
    IN_MATCH("in_match")
}
