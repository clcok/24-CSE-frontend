package com.example.parkingmatepractice

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.parkingmatepractice.databinding.ActivityChargeLogBinding
import com.example.parkingmatepractice.databinding.ActivityMyChargeBinding

class ChargeLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 바인딩 객체 획득
        val binding = ActivityChargeLogBinding.inflate(layoutInflater)
        // 액티비티 화면 출력
        setContentView(binding.root)
    }
}