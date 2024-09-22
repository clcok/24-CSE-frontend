package com.example.parkingmateprac.model.api

import com.example.parkingmateprac.model.dto.EnrollCarDto
import com.example.parkingmateprac.model.dto.UserCarDto
import com.example.parkingmateprac.model.dto.LoginRequestDto
import com.example.parkingmateprac.model.dto.LoginResponseDto
import com.example.parkingmateprac.model.dto.ParkingItemDto
import com.example.parkingmateprac.model.dto.RegisterRequestDto
import com.example.parkingmateprac.model.dto.ResponseParkingSpaceDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("/auth/register")
    fun registerUser(@Body registerRequest: RegisterRequestDto): Call<Void>

    @POST("/auth/login")
    fun loginUser(@Body loginRequest: LoginRequestDto): Call<LoginResponseDto>

    @POST("/users/car/enroll")
    fun enrollCar(@Body enrollCarRequst: EnrollCarDto): Call<EnrollCarDto>

    @GET("/api/parkingItems")
    suspend fun getParkingItems(): List<ParkingItemDto>

    @GET("/reservation/create/{parkingSpaceId}")
    fun getParkingSpace(): Call<List<ResponseParkingSpaceDto>>
}
