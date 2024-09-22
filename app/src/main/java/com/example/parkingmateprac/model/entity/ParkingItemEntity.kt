package com.example.parkingmateprac.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parking_items")
data class ParkingItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ownername: String,
    val parkingName: String,
    val category: String,
    val maxCar: Int,
    val useCar: Int,
    val price: Int,
    val explain: String,
    val isAvailable: Boolean,
    val parkingSpaceId: Int,
    val latitude: Double,
    val longitude: Double
)
