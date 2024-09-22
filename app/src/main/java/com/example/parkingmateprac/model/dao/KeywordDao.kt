package com.example.parkingmateprac.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.parkingmateprac.model.entity.KeywordEntity

@Dao
interface KeywordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeyword(keyword: KeywordEntity)

    @Query("SELECT * FROM keywords")
    suspend fun getAllKeywords(): List<KeywordEntity>

    @Query("DELETE FROM keywords WHERE keyword = :keyword")
    suspend fun deleteKeyword(keyword: KeywordEntity)
}
