package com.example.parkingmateprac.model.dto

data class UserCarDto(
    val carNumber: String?
) {
    val success: Boolean
        get() = !carNumber.isNullOrEmpty()
}