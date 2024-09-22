package com.example.parkingmateprac.view

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.parkingmateprac.viewmodel.adapter.BankSpinnerAdapter
import com.example.parkingmateprac.R
import com.example.parkingmateprac.databinding.ActivityChargeBinding
import com.example.parkingmateprac.model.dto.BankItem  // repository 패키지에서 BankItem 사용

class ChargeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChargeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ViewBinding 초기화
        binding = ActivityChargeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Spinner에 사용할 데이터: BankItem 리스트
        val bankList = listOf(
            BankItem("입금하실 은행을\n선택하세요", R.drawable.baseline_attach_money_24),
            BankItem("농협은행", R.drawable.ic_nonghyup),
            BankItem("카카오뱅크", R.drawable.ic_kakaobank),
            BankItem("KB국민은행", R.drawable.ic_kb),
            BankItem("우리은행", R.drawable.ic_shinhan),
            BankItem("하나은행", R.drawable.ic_hana)
        )

        // 커스텀 어댑터 생성
        val adapter = BankSpinnerAdapter(this, R.layout.spinner_bank_item, bankList)

        // Spinner에 어댑터 설정
        binding.spinner.adapter = adapter

        // Spinner 아이템 선택 리스너 설정
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedBank = bankList[position].name

                if (position != 0) { // "선택하세요"가 아닐 경우
                    Toast.makeText(this@ChargeActivity, "$selectedBank 선택됨", Toast.LENGTH_SHORT).show()
                    // 추가적인 동작 구현 가능 (예: 다른 Activity로 이동)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 선택되지 않았을 때 추가적인 처리
                Toast.makeText(this@ChargeActivity, "은행을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
