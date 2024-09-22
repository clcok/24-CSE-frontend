package com.example.parkingmateprac.viewmodel

import androidx.lifecycle.*
import com.example.parkingmateprac.model.api.KakaoLocalApi
import com.example.parkingmateprac.model.dto.ItemDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val api: KakaoLocalApi  // Hilt를 통해 KakaoLocalApi 주입
) : ViewModel() {

    private val _items = MutableLiveData<List<ItemDto>>()
    val items: LiveData<List<ItemDto>>
        get() = _items

    fun searchLocationData(keyword: String) {
        viewModelScope.launch {
            try {
                val response = api.searchKeyword("KakaoAK ${com.example.parkingmateprac.BuildConfig.KAKAO_REST_API_KEY}", keyword)
                _items.value = response.documents.map {
                    ItemDto(it.placeName, it.addressName, it.categoryGroupName, it.latitude, it.longitude)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
