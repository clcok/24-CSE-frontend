package com.example.parkingmateprac.api

import retrofit2.Call
import retrofit2.http.POST
import com.example.parkingmateprac.model.RegisterRequest
import retrofit2.http.Body

interface ApiService {
    @POST("/auth/register")  // 엔드포인트 수정
    fun registerUser(@Body registerRequest: RegisterRequest): Call<Void>

}
