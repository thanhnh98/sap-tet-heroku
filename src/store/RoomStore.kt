package com.tlife.store

import com.tlife.model.RoomModel

class RoomStore {
    companion object{
        private var listRooms: MutableList<RoomModel> = ArrayList()
        fun addRoom(room: RoomModel){
            listRooms.add(room)
        }

        fun isRoomAlreadyExisted(id: Long) = listRooms.any {
            it.roomId == id
        }

        fun findRoomById(id: Long) = listRooms.last {
            it.roomId == id
        }
    }

}