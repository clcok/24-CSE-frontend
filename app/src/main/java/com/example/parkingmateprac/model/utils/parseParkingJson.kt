package com.example.parkingmateprac.model.utils

import com.example.parkingmateprac.model.dto.ParkingItemDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun parseParkingJson(jsonString: String): List<ParkingItemDto> {
    val gson = Gson()
    val listType = object : TypeToken<List<ParkingItemDto>>() {}.type
    return gson.fromJson(jsonString, listType)
}
