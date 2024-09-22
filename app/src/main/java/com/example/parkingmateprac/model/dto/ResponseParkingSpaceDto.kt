package com.example.parkingmateprac.model.dto

class ResponseParkingSpaceDto(
    val id: Long,
    val userId: Long,
    val parkingSpaceId: Long,
    val date: String,
    val startTiem:String,
    val endTime: String
)