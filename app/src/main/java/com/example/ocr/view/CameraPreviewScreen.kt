package com.example.ocr.view

import android.view.ViewGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ocr.viewModel.MainViewModel

@Composable
fun CameraPreviewScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val lifecycleOwner = LocalLifecycleOwner.current

    // Remember the PreviewView so it doesn't get recreated every recomposition
    val previewView = remember { PreviewView(context) }

    // Apply layout parameters and scale type
    LaunchedEffect(Unit) {
        previewView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        previewView.scaleType = PreviewView.ScaleType.FILL_START
        // Start the camera with the ViewModel
        viewModel.startCamera(previewView, cameraProviderFuture, lifecycleOwner)
    }

    // Display the PreviewView in the composable
    AndroidView(
        factory = { previewView },
        modifier = modifier
    )

}

