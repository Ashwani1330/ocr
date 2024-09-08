package com.example.ocr.utils

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class TextAnalyzer(private val onTextDetected: (List<Text.TextBlock>) -> Unit): ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    // Callback with the recognized text blocks
                    onTextDetected(visionText.textBlocks)
                }
                .addOnFailureListener { e ->
                    Log.e("TextAnalyzer", "Text recognition failed", e)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
}