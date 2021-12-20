package com.tlife

import com.google.gson.Gson

fun Any.toJson(): String = Gson().toJson(this)
inline fun <reified T>fromJson(json: String): T{
    return Gson().fromJson(json, T::class.java)
}