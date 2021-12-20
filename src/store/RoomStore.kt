package com.tlife.store

import com.tlife.model.RoomModel

class RoomStore {
    companion object{
        private var listRooms: MutableList<RoomModel> = ArrayList()
        fun addRoom(room: RoomModel): RoomModel{
            listRooms.add(room)
            return room
        }

        fun destroyRoomById(id: Long): Long{
            listRooms.removeIf {
                it.roomId == id
            }
            return id
        }

        fun isRoomAlreadyExisted(id: Long) = listRooms.any {
            it.roomId == id
        }

        fun findRoomById(id: Long) = listRooms.last {
            it.roomId == id
        }

        fun getListRooms(): List<RoomModel> = listRooms
    }

}