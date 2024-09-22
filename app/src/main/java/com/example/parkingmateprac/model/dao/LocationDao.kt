package com.example.parkingmateprac.model.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.parkingmateprac.model.entity.LocationEntity

@Dao
interface LocationDao {
    @Query("SELECT * FROM location WHERE category_group_name = :category")
    suspend fun searchByCategory(category: String): List<LocationEntity>

    suspend fun search(keyword: String): List<LocationEntity> {
        return this.searchByCategory(keyword)
    }
}
