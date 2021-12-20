package com.tlife.store

import com.tlife.Connection

class SessionStore {
    companion object {
        var listSession: MutableList<Connection> = ArrayList()
    }
}