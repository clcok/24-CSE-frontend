package com.example.parkingmateprac.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // baseUrl은 반드시 "/"로 끝나야 함. 서버의 기본 URL만 설정
    private const val BASE_URL = "http://192.168.176.91:8080/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())  // Gson 변환기 추가
            .build()
            .create(ApiService::class.java)
    }
}
