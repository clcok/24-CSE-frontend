package com.example.parkingmateprac.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.parkingmateprac.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    // 바인딩 객체 획득
    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)

        settingButton()
        // 액티비티 화면 출력
        setContentView(binding.root)
    }

    fun settingButton() {
        binding.registerParkingLotBtn.setOnClickListener{
            // 등록 화면으로 이동 -> intent
            val intent = Intent(this, RegisterParkingLotActivity::class.java)
            startActivity(intent)
        }
    }
}