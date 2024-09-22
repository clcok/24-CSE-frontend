package com.example.parkingmateprac.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.parkingmateprac.model.entity.KeywordEntity
import com.example.parkingmateprac.model.entity.LocationEntity
import com.example.parkingmateprac.model.dao.KeywordDao
import com.example.parkingmateprac.model.dao.LocationDao
import com.example.parkingmateprac.model.dao.ParkingItemDao

@Database(entities = [KeywordEntity::class, LocationEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun parkingItemDao(): ParkingItemDao
    abstract fun keywordDao(): KeywordDao
    abstract fun locationDao(): LocationDao
}