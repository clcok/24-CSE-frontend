// 파일 위치: com/example/parkingmatepractice/parser/Parser.kt

package com.example.parkingmatepractice.parser

import android.content.Context
import com.example.parkingmatepractice.model.Item
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

    private fun getJsonDataFromAsset(fileName: String): String? {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            null
        }
    }
}