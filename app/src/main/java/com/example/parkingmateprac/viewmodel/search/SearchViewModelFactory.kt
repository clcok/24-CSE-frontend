package com.example.parkingmateprac.viewmodel.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parkingmateprac.api.KakaoLocalApi

class SearchViewModelFactory(private val api: KakaoLocalApi) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}