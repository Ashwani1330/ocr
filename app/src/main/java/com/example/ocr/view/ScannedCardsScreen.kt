package com.example.ocr.view

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ocr.data.model.OcrListItem
import com.example.ocr.viewModel.MainViewModel

@Composable
fun ScannedCardsScreen(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val scannedItems: List<OcrListItem> by viewModel.paragraphs.observeAsState(emptyList())

    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(scannedItems) { item ->
            val bitmap = item.imagePath?.let { BitmapFactory.decodeFile(it) }
            if (bitmap != null) {
                ParagraphCard(item, bitmap)
            }
        }
    }
}