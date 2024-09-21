// 파일 위치: com/example/parkingmatepractice/TestActivity.kt

package com.example.parkingmatepractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.graphics.Color
import com.example.parkingmatepractice.model.Item
import com.example.parkingmatepractice.parser.Parser
import com.example.parkingmatepractice.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {

    // View Binding을 위한 변수 선언
    private lateinit var binding: ActivityTestBinding

    private var itemList: List<Item>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰 바인딩 초기화
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val parser = Parser(this)
        itemList = parser.parseJson()

        itemList?.let { list ->
            // 데이터를 동적으로 UI에 표시
            for (item in list) {
                val textView = TextView(this).apply {
                    text = "Place: ${item.place}, Address: ${item.address}"
                    textSize = 16f
                    setPadding(16, 16, 16, 16)
                }
                binding.linearLayout.addView(textView)
            }
        } ?: run {
            // 파싱에 실패한 경우 처리
            val errorTextView = TextView(this).apply {
                text = "Failed to parse JSON"
                textSize = 16f
                setTextColor(Color.RED)
                setPadding(16, 16, 16, 16)
            }
            binding.linearLayout.addView(errorTextView)
        }
    }
}