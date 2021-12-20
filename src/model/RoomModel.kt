package com.tlife.model

data class RoomModel(
    var roomId: Long = System.currentTimeMillis().rem(1000000L),
    var status: RoomStatus = RoomStatus.WAITING,
    var firstPlayer: PlayerModel? = null,
    var secondPlayer: PlayerModel? = null,
    var winner: PlayerModel? = null,
    var currentMatch: MatchModel = MatchModel()
){
    fun isFull(): Boolean = firstPlayer != null && secondPlayer != null
    fun addFirstPlayer(player: PlayerModel): RoomModel{
        this.firstPlayer = player
        checkoutCurrentStatus()
        return this
    }

    fun addSecondPlayer(player: PlayerModel): RoomModel {
        this.secondPlayer = player
        checkoutCurrentStatus()
        return this
    }

    private fun checkoutCurrentStatus(){
        when{
            firstPlayer == null || secondPlayer == null -> this.status = RoomStatus.WAITING
            firstPlayer != null && secondPlayer != null -> this.status = RoomStatus.IN_MATCH
        }

    }
}

enum class RoomStatus(val value: String){
    WAITING("waiting"),
    IN_MATCH("in_match")
}
