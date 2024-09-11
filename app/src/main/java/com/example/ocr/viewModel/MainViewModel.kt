package com.example.ocr.viewModel

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr.data.dao.ListItemDao
import com.example.ocr.data.model.OcrListItem
import com.example.ocr.utils.TextAnalyzer
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.text.Text
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.YuvImage
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import java.io.ByteArrayOutputStream

/*
@HiltViewModel
class MainViewModel @Inject constructor(
    private val listItemDao: ListItemDao
): ViewModel() {

    private val cachedHashes = mutableSetOf<Int>()
    private val _paragraphs = MutableLiveData<List<OcrListItem>>()
    val paragraphs: LiveData<List<OcrListItem>> get() = _paragraphs

    private val _bitmaps = MutableStateFlow<Map<Int, Bitmap>>(emptyMap())
    val bitmaps = _bitmaps.asStateFlow()

    // Process a detected paragraph
    private fun processParagraph(paragraph: String, bitmap: Bitmap) {
        val hash = paragraph.hashCode()
        if (!isDuplicate(hash)) {
            storeParagraph(paragraph, bitmap)
        }
    }

    // Check if the paragraph is a duplicate
    private fun isDuplicate(hash: Int): Boolean {
        return cachedHashes.contains(hash)
    }

    // Store the paragraph in the database
    private fun storeParagraph(paragraph: String, bitmap: Bitmap) {
        viewModelScope.launch {
            val item = OcrListItem(paragraph = paragraph, hash = paragraph.hashCode(), imagePath = null)
            listItemDao.insert(item)
            cachedHashes.add(paragraph.hashCode())

            // Update LiveData to reflect changes in the UI
            _paragraphs.value = _paragraphs.value.orEmpty() + item
            onTakePhoto(bitmap, paragraph.hashCode())
        }
    }

    private fun onTakePhoto(bitmap: Bitmap, hash: Int) {
        _bitmaps.value = _bitmaps.value.toMutableMap().apply {
            this[hash] = bitmap
        }
    }

    // Initialize and start the camera
    fun startCamera(
        previewView: PreviewView,
        cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
        lifecycleOwner: LifecycleOwner
    ) {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(
                        ContextCompat.getMainExecutor(previewView.context),
                        { imageProxy ->
                            val bitmap = imageProxy.toBitmap()
                            processTextBlocks(bitmap)
                            imageProxy.close()
                        }
                    )
                }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.bindToLifecycle(
                lifecycleOwner, cameraSelector, preview, imageAnalyzer
            )
        }, ContextCompat.getMainExecutor(previewView.context))
    }

    private fun processTextBlocks(textBlocks: List<Text.TextBlock>) {
        textBlocks.forEach { textBlock ->
            val paragraph = textBlock.text
            if (paragraph.isNotBlank()) {
                // Pass the paragraph to the ViewModel for processing
                processParagraph(paragraph)
            }
        }
    }

    fun ImageProxy.convertToBitmap(): Bitmap? {
        val yBuffer = planes[0].buffer // Y
        val uBuffer = planes[1].buffer // U
        val vBuffer = planes[2].buffer // V

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(android.graphics.Rect(0, 0, width, height), 100, out)
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}*/

@HiltViewModel
class MainViewModel @Inject constructor(
    private val listItemDao: ListItemDao
) : ViewModel() {

    private val cachedHashes = mutableSetOf<Int>()
    private val _paragraphs = MutableLiveData<List<OcrListItem>>()
    val paragraphs: LiveData<List<OcrListItem>> get() = _paragraphs

    private val _bitmaps = MutableStateFlow<Map<Int, Bitmap>>(emptyMap())
    val bitmaps = _bitmaps.asStateFlow()

    // Process a detected paragraph
    private fun processParagraph(paragraph: String, bitmap: Bitmap) {
        val hash = paragraph.hashCode()
        if (!isDuplicate(hash)) {
            storeParagraph(paragraph, bitmap)
        }
    }

    // Check if the paragraph is a duplicate
    private fun isDuplicate(hash: Int): Boolean {
        return cachedHashes.contains(hash)
    }

    // Store the paragraph in the database
    private fun storeParagraph(paragraph: String, bitmap: Bitmap) {
        viewModelScope.launch {
            val item = OcrListItem(paragraph = paragraph, hash = paragraph.hashCode(), imagePath = null)
            listItemDao.insert(item)
            cachedHashes.add(paragraph.hashCode())

            // Update LiveData to reflect changes in the UI
            _paragraphs.value = _paragraphs.value.orEmpty() + item
            onTakePhoto(bitmap, paragraph.hashCode())
        }
    }

    private fun onTakePhoto(bitmap: Bitmap, hash: Int) {
        _bitmaps.value = _bitmaps.value.toMutableMap().apply {
            this[hash] = bitmap
        }
    }

    // Initialize and start the camera
    fun startCamera(
        previewView: PreviewView,
        cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
        lifecycleOwner: LifecycleOwner
    ) {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = androidx.camera.core.Preview.Builder().build().apply {
                surfaceProvider = previewView.surfaceProvider
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .apply {
                    setAnalyzer(
                        ContextCompat.getMainExecutor(previewView.context)
                    ) { imageProxy ->
                        val bitmap = imageProxy.convertToBitmap()
                        bitmap?.let {
                            // Pass bitmap and ImageProxy to TextAnalyzer
                            val textAnalyzer = TextAnalyzer { textBlocks ->
                                processTextBlocks(textBlocks, bitmap)
                            }
                            textAnalyzer.analyze(imageProxy)
                        }
                        imageProxy.close()
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.bindToLifecycle(
                lifecycleOwner, cameraSelector, preview, imageAnalyzer
            )
        }, ContextCompat.getMainExecutor(previewView.context))
    }

    private fun processTextBlocks(textBlocks: List<Text.TextBlock>, bitmap: Bitmap) {
        textBlocks.forEach { textBlock ->
            val paragraph = textBlock.text
            if (paragraph.isNotBlank()) {
                // Pass the paragraph and bitmap to the ViewModel for processing
                processParagraph(paragraph, bitmap)
            }
        }
    }


    // Convert ImageProxy to Bitmap
    private fun ImageProxy.convertToBitmap(): Bitmap? {
        val yBuffer = planes[0].buffer // Y
        val uBuffer = planes[1].buffer // U
        val vBuffer = planes[2].buffer // V

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(android.graphics.Rect(0, 0, width, height), 100, out)
        val imageBytes = out.toByteArray()

        return try {
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        } catch (e: Exception) {
            Log.e("MainViewModel", "Bitmap conversion failed", e)
            null
        }
    }
}