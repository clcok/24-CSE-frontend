package com.example.parkingmateprac.model

// 주차장 정보를 저장하기 위한 데이터 클래스
data class ParkingItem(
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
