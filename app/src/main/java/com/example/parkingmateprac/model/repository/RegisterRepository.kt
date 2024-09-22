package com.example.parkingmateprac.model.repository

import com.example.parkingmateprac.model.api.ApiService
import com.example.parkingmateprac.model.dto.RegisterRequestDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class RegisterRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun registerUser(registerRequest: RegisterRequestDto): Boolean {
        return withContext(Dispatchers.IO) {
            val response: Response<Void> = apiService.registerUser(registerRequest).execute()
            response.isSuccessful
        }
    }
}
