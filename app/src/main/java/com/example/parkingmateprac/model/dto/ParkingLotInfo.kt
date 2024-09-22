package com.example.parkingmateprac.model.dto

data class ParkingLotInfo(
    val name: String,
    val address: String,
    val maxCar: Int,
    val price: Int,
    val explain: String = "가볍게 주차하고 가세요"
)
