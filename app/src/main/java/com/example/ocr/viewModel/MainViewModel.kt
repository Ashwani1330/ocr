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
import com.example.ocr.data.dao.OcrListItemDao
import com.example.ocr.data.model.OcrListItem
import com.example.ocr.utils.TextAnalyzer
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.text.Text
import kotlinx.coroutines.launch

class MainViewModel (
    private val listItemDao: OcrListItemDao
): ViewModel() {

    private val cachedHashes = mutableSetOf<Int>()
    private val _paragraphs = MutableLiveData<List<OcrListItem>>()
    val paragraphs: LiveData<List<OcrListItem>> get() = _paragraphs

    // Process a detected paragraph
    private fun processParagraph(paragraph: String) {
        val hash = paragraph.hashCode()
        if (!isDuplicate(hash)) {
            storeParagraph(paragraph)
        }
    }

    // Check if the paragraph is a duplicate
    private fun isDuplicate(hash: Int): Boolean {
        return cachedHashes.contains(hash)
    }

    // Store the paragraph in the database
    private fun storeParagraph(paragraph: String) {
        viewModelScope.launch {
            val item = OcrListItem(paragraph = paragraph, hash = paragraph.hashCode(), imagePath = null)
            // listItemDao.insertItem(item)
            cachedHashes.add(paragraph.hashCode())

            // Update LiveData to reflect changes in the UI
            _paragraphs.value = _paragraphs.value.orEmpty() + item
            println(_paragraphs)
            println(item)
        }
    }

    /* private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
     val bitmaps = _bitmaps.asStateFlow()

     fun onTakePhoto(bitmap: Bitmap) {
         _bitmaps.value += bitmap
     }*/

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
                        TextAnalyzer { textBlocks ->
                            processTextBlocks(textBlocks)
                        })
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
}