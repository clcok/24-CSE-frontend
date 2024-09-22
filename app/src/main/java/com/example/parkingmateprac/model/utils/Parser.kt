package com.example.parkingmateprac.model.utils

import android.content.Context
import com.example.parkingmateprac.model.dto.ItemDto
import com.example.parkingmateprac.model.dto.ParkingItemDto
import com.example.parkingmateprac.model.dto.RegisterRequestDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class Parser(private val context: Context) {

    fun parseItemJson(): List<ItemDto>? {
        val jsonString = getJsonDataFromAsset("items.json")
        return jsonString?.let {
            val listItemType = object : TypeToken<List<ItemDto>>() {}.type
            Gson().fromJson(it, listItemType)
        }
    }

    fun parseParkingJson(): List<ParkingItemDto>? {
        val jsonString = getJsonDataFromAsset("parking_items.json")
        return jsonString?.let {
            val listItemType = object : TypeToken<List<ParkingItemDto>>() {}.type
            Gson().fromJson(it, listItemType)
        }
    }

    fun parseRegisterRequestJson(): List<RegisterRequestDto>? {
        val jsonString = getJsonDataFromAsset("register_requests.json")
        return jsonString?.let {
            val listItemType = object : TypeToken<List<RegisterRequestDto>>() {}.type
            Gson().fromJson(it, listItemType)
        }
    }

    private fun getJsonDataFromAsset(fileName: String): String? {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            null
        }
    }
}
