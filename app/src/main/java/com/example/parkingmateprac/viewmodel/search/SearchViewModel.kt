package com.example.parkingmateprac.viewmodel.search

import androidx.lifecycle.*
import com.example.parkingmateprac.api.KakaoLocalApi
import com.example.parkingmateprac.model.Item
import kotlinx.coroutines.launch

class SearchViewModel(private val api: KakaoLocalApi) : ViewModel() {
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>>
        get() = _items

    fun searchLocationData(keyword: String) {
        viewModelScope.launch {
            try {
                val response = api.searchKeyword("KakaoAK ${com.example.parkingmateprac.BuildConfig.KAKAO_REST_API_KEY}", keyword)
                _items.value = response.documents.map {
                    Item(it.placeName, it.addressName, it.categoryGroupName, it.latitude, it.longitude)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}