package com.example.parkingmatepractice

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.parkingmatepractice.databinding.ActivityHomeBinding
import com.example.parkingmatepractice.databinding.ActivityMyPageBinding
import com.example.parkingmatepractice.databinding.ActivityRegisterParkingLotBinding

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 바인딩 객체 획득
        val binding = ActivityMyPageBinding.inflate(layoutInflater)
        // 액티비티 화면 출력
        setContentView(binding.root)
    }
}