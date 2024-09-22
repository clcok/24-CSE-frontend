package com.example.parkingmateprac.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingmateprac.model.repository.KeywordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KeywordViewModel @Inject constructor(
    private val repository: KeywordRepository // Hilt를 통해 KeywordRepository 주입
) : ViewModel() {

    private val _keywords = MutableLiveData<List<String>>()
    val keywords: LiveData<List<String>> get() = _keywords

    init {
        loadKeywords() // loadKeywords()를 호출하되 비동기적으로 실행
    }

    private fun loadKeywords() {
        viewModelScope.launch {
            _keywords.value = repository.read()
        }
    }

    suspend fun saveKeyword(keyword: String) {
        repository.update(keyword) // keyword 저장
        loadKeywords() // 업데이트 후 다시 로드하여 UI 업데이트
    }

    suspend fun deleteKeyword(keyword: String) {
        repository.delete(keyword)
        loadKeywords() // 삭제 후 다시 로드하여 UI 업데이트
    }
}
