package com.example.ocr.di

import android.content.Context
import androidx.room.Room
import com.example.ocr.data.dao.OcrListItemDao
import com.example.ocr.data.database.AppDatabase
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
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideListItemDao(appDatabase: AppDatabase): OcrListItemDao {
        return appDatabase.listItemDao()
    }

}
