package com.example.parkingmateprac.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parkingmateprac.databinding.ActivityRegisterBinding
import com.example.parkingmateprac.model.dto.RegisterRequestDto

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val userName = binding.userNameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val telephone = binding.telephoneEditText.text.toString()

            if (name.isNotEmpty() && userName.isNotEmpty() && password.isNotEmpty() && telephone.isNotEmpty()) {
                val registerRequest = RegisterRequestDto(
                    name = name,
                    userName = userName,
                    password = password,
                    telephone = telephone
                )

                // TODO: 서버로 회원가입 요청 전송
                // 예시: apiService.registerUser(registerRequest)

                Toast.makeText(this, "회원가입 요청이 전송되었습니다.", Toast.LENGTH_SHORT).show()
                finish() // 회원가입 완료 후 액티비티 종료 또는 로그인 화면으로 이동
            } else {
                Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
