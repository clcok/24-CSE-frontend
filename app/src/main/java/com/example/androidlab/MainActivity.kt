package com.example.androidlab

import android.os.Bundle
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidlab.databinding.ActivityMainBinding
import com.example.androidlab.databinding.ViewBindingTestBinding

class MainActivity : AppCompatActivity() {
//    private lateinit var mySpinner: Spinner
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Spinner 초기화
//        mySpinner = findViewById(R.id.my_spinner)
//
//        // 어댑터 생성
//
//        // 화면 출력 XML 명시
//        setContentView(R.layout.text1)
//
//        val textView1: TextView = findViewById(R.id.text1)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 바인딩 객체 획득
        val binding = ViewBindingTestBinding.inflate(layoutInflater)
        // 액티비티 화면 출력
        setContentView(binding.root)
    }
}