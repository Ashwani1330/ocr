package com.example.ocr.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.ocr.data.dao.OcrListItemDao
import com.example.ocr.viewModel.MainViewModel

class MainViewModelFactory(
    private val listItemDao: OcrListItemDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(listItemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
