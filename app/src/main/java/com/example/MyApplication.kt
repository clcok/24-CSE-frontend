package com.example

import android.app.Application
import com.example.parkingmateprac.BuildConfig
import com.kakao.vectormap.KakaoMapSdk
import com.example.parkingmateprac.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {

    companion object {
        lateinit var retrofit: Retrofit
            private set
    }

    override fun onCreate() {
        super.onCreate()

        // 카카오 지도 SDK 초기화
        KakaoMapSdk.init(this, getString(R.string.KAKAO_API_KEY))

        // Retrofit 초기화
        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.KAKAO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}