package com.example.parkingmateprac.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.parkingmateprac.model.dao.KeywordDao
import com.example.parkingmateprac.model.dao.ParkingItemDao
import com.example.parkingmateprac.model.entity.KeywordEntity

// RoomDatabase 상속
@Database(entities = [KeywordEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // Dao 인터페이스를 반환하는 추상 함수
    abstract fun keywordDao(): ParkingItemDao
}
