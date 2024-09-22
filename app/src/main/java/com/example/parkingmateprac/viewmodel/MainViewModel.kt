package com.example.parkingmateprac.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.parkingmateprac.model.dto.ItemDto
import com.example.parkingmateprac.model.entity.ParkingItemEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    // 마지막 마커 위치를 관리하는 LiveData
    private val _lastMarkerPosition = MutableLiveData<ItemDto?>()
    val lastMarkerPosition: LiveData<ItemDto?> = _lastMarkerPosition

    // 주차장 데이터를 관리하는 LiveData
    private val _parkingItems = MutableLiveData<List<ParkingItemEntity>>()
    val parkingItems: LiveData<List<ParkingItemEntity>> = _parkingItems

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    init {
        loadLastMarkerPosition()  // 뷰모델 초기화 시 마커 위치 불러오기
    }

    // 주차장 정보를 ViewModel에 저장하는 함수
    fun setParkingItems(parkingItems: List<ParkingItemEntity>) {
        _parkingItems.value = parkingItems
    }

    // 마지막 마커 위치 저장
    fun saveLastMarkerPosition(item: ItemDto) {
        with(sharedPreferences.edit()) {
            putFloat(PREF_LATITUDE, item.latitude.toFloat())
            putFloat(PREF_LONGITUDE, item.longitude.toFloat())
            putString(PREF_PLACE_NAME, item.place)
            putString(PREF_ROAD_ADDRESS_NAME, item.address)
            apply()  // SharedPreferences에 값 저장
        }
        _lastMarkerPosition.value = item  // LiveData 업데이트
    }

    // SharedPreferences에서 마지막 마커 위치 불러오기
    private fun loadLastMarkerPosition() {
        if (sharedPreferences.contains(PREF_LATITUDE) && sharedPreferences.contains(PREF_LONGITUDE)) {
            val latitude = sharedPreferences.getFloat(PREF_LATITUDE, 0.0f).toDouble()
            val longitude = sharedPreferences.getFloat(PREF_LONGITUDE, 0.0f).toDouble()
            val placeName = sharedPreferences.getString(PREF_PLACE_NAME, "") ?: ""
            val roadAddressName = sharedPreferences.getString(PREF_ROAD_ADDRESS_NAME, "") ?: ""

            _lastMarkerPosition.value = if (placeName.isNotEmpty() && roadAddressName.isNotEmpty()) {
                ItemDto(placeName, roadAddressName, "", latitude, longitude)
            } else {
                null
            }
        } else {
            _lastMarkerPosition.value = null
        }
    }

    companion object {
        private const val PREFS_NAME = "LastMarkerPrefs"
        private const val PREF_LATITUDE = "lastLatitude"
        private const val PREF_LONGITUDE = "lastLongitude"
        private const val PREF_PLACE_NAME = "lastPlaceName"
        private const val PREF_ROAD_ADDRESS_NAME = "lastRoadAddressName"
    }
}
