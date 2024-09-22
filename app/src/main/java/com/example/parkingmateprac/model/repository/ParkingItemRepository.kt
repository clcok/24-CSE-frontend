// ParkingItemRepository.kt
package com.example.parkingmateprac.repository

import com.example.parkingmateprac.model.api.ApiService
import com.example.parkingmateprac.model.dao.ParkingItemDao
import com.example.parkingmateprac.model.entity.ParkingItemEntity
import com.example.parkingmateprac.model.dto.ParkingItemDto
import javax.inject.Inject

class ParkingItemRepository @Inject constructor(
    private val apiService: ApiService,
    private val parkingItemDao: ParkingItemDao
) {

    suspend fun fetchAndSaveParkingItems() {
        val dtoList = apiService.getParkingItems()
        val entityList = dtoList.map { it.toEntity() }
        parkingItemDao.insertAll(entityList)
    }

    suspend fun getAllParkingItems(): List<ParkingItemEntity> {
        return parkingItemDao.getAllParkingItems()
    }
}


// 변환 확장 함수 추가
fun ParkingItemDto.toEntity(): ParkingItemEntity {
    return ParkingItemEntity(
        ownername = this.ownername,
        parkingName = this.parkingName,
        category = this.category,
        maxCar = this.maxCar,
        useCar = this.useCar,
        price = this.price,
        explain = this.explain,
        isAvailable = this.isAvailable,
        parkingSpaceId = this.parkingSpaceId,
        latitude = this.latitude,
        longitude = this.longitude
    )
}

