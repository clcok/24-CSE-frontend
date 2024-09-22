package com.example.parkingmateprac.model.di

import android.content.Context
import androidx.room.Room
import com.example.parkingmateprac.model.AppDatabase
import com.example.parkingmateprac.model.dao.KeywordDao
import com.example.parkingmateprac.model.dao.LocationDao
import com.example.parkingmateprac.model.entity.LocationEntity
import com.example.parkingmateprac.model.repository.KeywordRepository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration() // 필요시 마이그레이션 재생성
            .build()
    }

    @Provides
    @Singleton
    fun provideKeywordDao(database: AppDatabase): KeywordDao {
        return database.keywordDao()
    }

    @Provides
    @Singleton
    fun provideItemDao(database: AppDatabase): LocationDao {
        return database.locationDao()
    }

    @Provides
    @Singleton
    fun provideKeywordRepository(keywordDao: KeywordDao): KeywordRepository {
        return KeywordRepository(keywordDao)
    }

    class LocationSearcher(private val locationDao: LocationDao) {
        suspend fun search(keyword: String): List<LocationEntity> {
            return locationDao.searchByCategory(keyword)
        }
    }

}