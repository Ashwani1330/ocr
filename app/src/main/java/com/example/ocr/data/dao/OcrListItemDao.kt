package com.example.ocr.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.ocr.data.model.OcrListItem
import androidx.room.Query

@Dao
interface OcrListItemDao {

   /* @Query("SELECT * FROM ocr_list_items WHERE hash = :hash LIMIT 1")
    suspend fun getOcrListItemByHash(hash: Int): OcrListItem?*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOcrListItem(item: OcrListItem): Long

    @Delete
    suspend fun deleteOcrListItem(item: OcrListItem): Int
}