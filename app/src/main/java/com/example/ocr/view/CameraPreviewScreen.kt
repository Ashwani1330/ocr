package com.example.ocr.view

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ocr.data.model.OcrListItem
import com.example.ocr.viewModel.MainViewModel

@Composable
fun CameraPreviewScreen(viewModel: MainViewModel, modifier: Any) {

    val context = LocalContext.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    val lifecycleOwner = LocalLifecycleOwner.current

    val paragraphs by viewModel.paragraphs.observeAsState(emptyList())
    val bitmaps by viewModel.bitmaps.collectAsState()

    // Debug logs
    Log.e("CameraPreviewScreen", "Paragraphs: $paragraphs")
    Log.e("CameraPreviewScreen", "Bitmaps: $bitmaps")

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenHeight = maxHeight
        val cameraHeight = screenHeight * 0.2f

        Column {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        // scaleType = PreviewView.ScaleType.FILL_START
                        viewModel.startCamera(this, cameraProviderFuture, lifecycleOwner)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cameraHeight)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight - cameraHeight)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    if (paragraphs.isEmpty()) {
                        item {
                            Text("No data available", Modifier.padding(16.dp))
                        }
                    } else {
                        items(paragraphs) { item ->
                            val bitmap = bitmaps[item.hash] ?: Bitmap.createBitmap(60, 60, Bitmap.Config.ARGB_8888)
                            val rotatedImage = bitmap.rotate(90f)
                            ParagraphCard(item, rotatedImage)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ParagraphCard(item: OcrListItem, bitmap: Bitmap) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = item.paragraph,
                modifier = Modifier.weight(1f),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// Function to rotate a Bitmap by 90 degrees clockwise
private fun Bitmap.rotate(degrees: Float = 90f): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}