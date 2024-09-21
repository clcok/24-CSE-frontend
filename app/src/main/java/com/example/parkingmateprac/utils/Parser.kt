package com.example.parkingmateprac.utils

import android.content.Context
import com.example.parkingmateprac.model.Item
import com.example.parkingmateprac.model.ParkingItem
import com.example.parkingmateprac.model.RegisterRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class Parser(private val context: Context) {

    fun parseJson(): List<Item>? {
        val jsonString = getJsonDataFromAsset("items.json")
        return jsonString?.let {
            val listItemType = object : TypeToken<List<Item>>() {}.type
            Gson().fromJson(it, listItemType)
        }
    }
    fun parseJson2(): List<ParkingItem>? {
        val jsonString = getJsonDataFromAsset("items.json")
        return jsonString?.let {
            val listItemType = object : TypeToken<List<Item>>() {}.type
            Gson().fromJson(it, listItemType)
        }
    }
    fun parseJson3(): List<RegisterRequest>? {
        val jsonString = getJsonDataFromAsset("items.json")
        return jsonString?.let {
            val listItemType = object : TypeToken<List<Item>>() {}.type
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