package com.example.parkingmateprac.utils

import com.example.parkingmateprac.model.ParkingItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun parseParkingJson(jsonString: String): List<ParkingItem> {
    val gson = Gson()
    val listType = object : TypeToken<List<ParkingItem>>() {}.type
    return gson.fromJson(jsonString, listType)
}
