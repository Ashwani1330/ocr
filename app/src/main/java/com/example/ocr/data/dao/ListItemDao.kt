package com.example.ocr.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ocr.data.model.OcrListItem

@Dao
interface   ListItemDao {

    @Insert
    suspend fun insert(item: OcrListItem)

    @Query("SELECT * FROM ocr_list_items")
    suspend fun getAllItems(): List<OcrListItem>
}