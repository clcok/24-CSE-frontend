package com.example.parkingmateprac.viewmodel.keyword

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parkingmateprac.repository.keyword.KeywordRepository

class KeywordViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KeywordViewModel::class.java)) {
            return KeywordViewModel(KeywordRepository.getInstance(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}