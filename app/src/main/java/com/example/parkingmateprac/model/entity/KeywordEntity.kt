package com.example.parkingmateprac.model.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keywords")
data class KeywordEntity(
    @PrimaryKey val keyword: String
)
