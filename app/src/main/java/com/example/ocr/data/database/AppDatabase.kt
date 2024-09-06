package com.example.ocr.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ocr.data.dao.OcrListItemDao
import com.example.ocr.data.model.OcrListItem

@Database(entities = [OcrListItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Define the DAO
    abstract fun listItemDao(): OcrListItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Create and get the database instance
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ocr_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}