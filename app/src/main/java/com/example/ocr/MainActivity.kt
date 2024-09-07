package com.example.ocr

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.ocr.data.database.AppDatabase
import com.example.ocr.ui.theme.OcrTheme
import com.example.ocr.view.CameraPreviewScreen
import com.example.ocr.viewModel.MainViewModel
import com.example.ocr.di.MainViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the database and DAO
        val database = AppDatabase.getDatabase(applicationContext)
        val listItemDao = database.ocrListItemDao()

        // Create ViewModelFactory
        val viewModelFactory = MainViewModelFactory(listItemDao)

        // Initialize ViewModel with factory
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        if (!has_camera_permission()) {
            ActivityCompat.requestPermissions(
                this,
                CAMERAX_PERMISSIONS,
                0
            )
        }
        enableEdgeToEdge()
        setContent {
            OcrTheme {
                CameraPreviewScreen(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxHeight(0.5f)
                )
            }
        }
    }

    private fun has_camera_permission(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    }
}