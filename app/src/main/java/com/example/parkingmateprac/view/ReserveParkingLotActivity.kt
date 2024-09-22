package com.example.parkingmateprac.view

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parkingmateprac.databinding.ActivityReserveParkingLotBinding
import java.text.SimpleDateFormat
import java.util.*

class ReserveParkingLotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReserveParkingLotBinding

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var parkingName: String
    private lateinit var address: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화
        binding = ActivityReserveParkingLotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent로부터 좌표 정보와 주차장 이름, 주소 받기
        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)
        parkingName = intent.getStringExtra("parkingName") ?: "알 수 없는 주차장"
        address = intent.getStringExtra("address") ?: "주소 정보 없음"

        // 주차장 이름과 주소 설정
        binding.parkingNameTextView.text = parkingName
        binding.addressTextView.text = address

        // 시간 목록 생성 (예시로 08:00부터 22:00까지 생성)
        val availableTime = generateTimeList("08:00", "22:00")

        // ArrayAdapter 생성
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, availableTime)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Spinner에 어댑터 설정
        binding.inTimeSpinner.adapter = adapter
        binding.outTimeSpinner.adapter = adapter

        // Spinner 아이템 선택 리스너 설정
        binding.inTimeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, // Spinner 자체
                view: View?, // 선택된 뷰
                position: Int, // 선택된 아이템의 위치
                id: Long // 선택된 아이템의 ID
            ) {
                // 선택된 시간 가져오기
                val selectedInTime = parent?.getItemAtPosition(position).toString()
                if (position != 0) {
                    Toast.makeText(this@ReserveParkingLotActivity, "입차 시간: $selectedInTime", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때
            }
        }

        binding.outTimeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, // Spinner 자체
                view: View?, // 선택된 뷰
                position: Int, // 선택된 아이템의 위치
                id: Long // 선택된 아이템의 ID
            ) {
                // 선택된 시간 가져오기
                val selectedOutTime = parent?.getItemAtPosition(position).toString()
                if (position != 0) {
                    Toast.makeText(this@ReserveParkingLotActivity, "출차 시간: $selectedOutTime", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때
            }
        }
    }

    private fun generateTimeList(startTime: String, endTime: String): List<String> {
        val timeList = mutableListOf("시간을 선택하세요.")
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

        val startDate = sdf.parse(startTime)
        val endDate = sdf.parse(endTime)

        if (startDate != null && endDate != null) {
            val calendar = Calendar.getInstance()
            calendar.time = startDate

            while (calendar.time <= endDate) {
                val time = sdf.format(calendar.time)
                timeList.add(time)
                calendar.add(Calendar.MINUTE, 30)
            }
        }

        return timeList
    }
}
