package com.example.ocr.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/*@Entity(tableName = "ocr_list_items")
data class OcrListItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // Auto-generated primary key
    val paragraph: String,
    val hash: Int,
    val imagePath: String?  // Nullable for cases when no image is available
)*/

@Entity(tableName = "ocr_list_items")
data class OcrListItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val paragraph: String,
    val hash: Int,
    val imagePath: String?
)