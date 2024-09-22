// ParkingItemDao.kt
package com.example.parkingmateprac.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.parkingmateprac.model.entity.ParkingItemEntity

@Dao
interface ParkingItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(parkingItems: List<ParkingItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(parkingItem: ParkingItemEntity)

    @Query("SELECT * FROM parking_items")
    suspend fun getAllParkingItems(): List<ParkingItemEntity>

    @Query("SELECT * FROM parking_items WHERE id = :id")
    suspend fun getParkingItemById(id: Int): ParkingItemEntity?

    @Query("DELETE FROM parking_items")
    suspend fun deleteAllParkingItems()
}
