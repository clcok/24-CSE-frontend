package com.example.parkingmateprac.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.parkingmateprac.model.entity.ParkingItemEntity

@Dao
interface ParkingItemDao {
    // 모든 주차장 데이터를 가져오는 쿼리
    @Query("SELECT * FROM parking_items")
    suspend fun getAllParkingItems(): List<ParkingItemEntity>

    // 주차장 데이터를 삽입하는 함수
    @Insert
    suspend fun insertParkingItems(parkingItems: List<ParkingItemEntity>)
}
