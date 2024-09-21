package com.example.parkingmateprac.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.parkingmateprac.model.RegisterRequest

interface ApiService {
    @POST("/auth/register")  // "/auth/register" 경로를 여기에서 설정
    fun registerUser(@Body registerRequest: RegisterRequest): Call<Void>
}
