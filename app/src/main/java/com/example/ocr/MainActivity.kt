package com.example.ocr

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.ocr.ui.theme.OcrTheme
import com.example.ocr.utils.AppNavigation
import com.example.ocr.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (!hasCameraPermission()) {
            ActivityCompat.requestPermissions(
                this,
                CAMERAX_PERMISSIONS,
                0
            )
        }
        setContent {
            OcrTheme {
/*
                val innerPadding = 0.dp
                CameraPreviewScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
*/
                val navController = rememberNavController()

                // Set up navigation and content using AppNavigation
                AppNavigation(navController = navController, viewModel = viewModel)
            }
        }
    }

    private fun hasCameraPermission(): Boolean {
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