package com.example.parkingmateprac.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.parkingmateprac.databinding.ActivityChargeLogBinding
import com.example.parkingmateprac.databinding.ActivityHomeBinding
import com.example.parkingmateprac.databinding.ActivityMyChargeBinding

class ChargeLogActivity : AppCompatActivity() {

    lateinit var binding: ActivityChargeLogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 바인딩 객체 획득
        binding = ActivityChargeLogBinding.inflate(layoutInflater)
        settingButton()

        // 액티비티 화면 출력
        setContentView(binding.root)
    }

    fun settingButton() {
        binding.chargeBtn.setOnClickListener{
            val intent = Intent(this, ChargeActivity::class.java)
            startActivity(intent)
        }
    }
}