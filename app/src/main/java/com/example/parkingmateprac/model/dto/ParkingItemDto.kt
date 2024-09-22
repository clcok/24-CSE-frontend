package com.example.parkingmateprac.model.dto

data class ParkingItemDto(
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