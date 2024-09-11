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


/*
class TextAnalyzer(
    private val onTextDetected: (List<Text.TextBlock>) -> Unit
): ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private var lastDocumentText: String? = null
    private var lastProcessedTime = 0L
    private var lastImageHash: Int? = null

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            // Check if enough time has passed since the last processing
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastProcessedTime < 2000) {  // 2 seconds delay
                imageProxy.close()
                return
            }

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val documentText = visionText.text
                    val currentImageHash = imageProxy.hashCode()

                    // Check if the document or image has changed
                    if (isNewDocument(documentText) || currentImageHash != lastImageHash) {
                        lastProcessedTime = currentTime
                        lastDocumentText = documentText
                        lastImageHash = currentImageHash
                    }
                    // Callback with the recognized text blocks
                    val cardDataList = processDocument(visionText.textBlocks)
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

    private fun isNewDocument(currentText: String): Boolean {
        // Compare current text with the last processed document text
        return lastDocumentText == null || lastDocumentText != currentText
    }

    private fun areDocumentsSimilar(lastText: String, currentText: String): Boolean {
        val lastTextWords = lastText.split(" ")
        val currentTextWords = currentText.split(" ")

        // Compare word sets for a quick similarity check
        val commonWords = lastTextWords.intersect(currentTextWords.toSet())
        val similarity = commonWords.size.toDouble() / maxOf(lastTextWords.size, currentTextWords.size)

        // Adjust the threshold as needed (e.g., 0.8 means 80% similarity)
        return similarity > 0.8
    }

    private fun processDocument(textBlock: List<Text.TextBlock>): List<CardData> {
        val cardDataList = mutableListOf<CardData>()

        for (block in textBlock) {
            for (line in block.lines) {
                val paragraphText = line.text
                val imageFound = checkForImageInParagraph(line)
                cardDataList.add(CardData(paragraphText, imageFound))
            }
        }

        return cardDataList
    }

    private fun checkForImageInParagraph(line: Text.Line): Boolean {
        // Safely access the boundingBox property
        val boundingBox = line.boundingBox

        // Define keywords that might indicate an image
        val imageKeywords = listOf("image", "photo", "figure", "diagram", "chart", "illustration", "map", "picture")
        val words = line.elements.map { it.text.toLowerCase() }

        // Check for keywords in the text
        if (words.any { word -> imageKeywords.any { it in word } }) {
            return true
        }

        // Check if the bounding box is not null and its dimensions
        boundingBox?.let {
            val height = it.height()
            val width = it.width()

            // If the bounding box is significantly taller than it is wide, and the text is empty
            if (height > 2 * width && line.text.trim().isEmpty()) {
                return true
            }

            // Check if the aspect ratio of the bounding box suggests an image
            val aspectRatio = width.toDouble() / height
            if (aspectRatio < 0.5 || aspectRatio > 2.0) {
                return true
            }

            // New Check: Consider text density within the bounding box
            val area = width * height
            val textLength = line.text.length
            val density = textLength / area.toDouble()

            // If density is low, it might indicate an image
            if (density < 0.1) {
                return true
            }
        }

        // If none of the conditions are met, return false
        return false
    }
}

data class CardData(
    val title: String,
    val imageFound: Boolean
)*/

class TextAnalyzer(
    private val onTextDetected: (List<Text.TextBlock>) -> Unit
) : ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private var lastDocumentText: String? = null
    private var lastProcessedTime = 0L
    private var lastImageHash: Int? = null

    /*@OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            val currentTime = System.currentTimeMillis()
            if (currentTime - lastProcessedTime < calculateDelay(lastDocumentText?.length ?: 0)) {
                imageProxy.close()
                return
            }

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val documentText = visionText.text
                    val currentImageHash = imageProxy.hashCode()

                    if (isNewDocument(documentText) || currentImageHash != lastImageHash) {
                        lastProcessedTime = currentTime
                        lastDocumentText = documentText
                        lastImageHash = currentImageHash

                        val cardDataList = processDocument(visionText.textBlocks)
                        onTextDetected(visionText.textBlocks)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("TextAnalyzer", "Text recognition failed: ${e.message}", e)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
*/

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            // Check if enough time has passed since the last processing
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastProcessedTime < 2000) {  // 2 seconds delay
                imageProxy.close()
                return
            }

            // Use a try-finally block to ensure the image proxy is always closed
            try {
                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        val documentText = visionText.text
                        val currentImageHash = imageProxy.hashCode()

                        // Check if the document or image has changed
                        if (isNewDocument(documentText) || currentImageHash != lastImageHash) {
                            lastProcessedTime = currentTime
                            lastDocumentText = documentText
                            lastImageHash = currentImageHash
                        }
                        // Callback with the recognized text blocks
                        onTextDetected(visionText.textBlocks)
                    }
                    .addOnFailureListener { e ->
                        Log.e("TextAnalyzer", "Text recognition failed", e)
                    }
            } finally {
                imageProxy.close()
            }
        }
    }


    private fun isNewDocument(currentText: String): Boolean {
        return lastDocumentText == null || !areDocumentsSimilar(lastDocumentText!!, currentText)
    }

    private fun areDocumentsSimilar(lastText: String, currentText: String): Boolean {
        val lastTextWords = lastText.split(" ")
        val currentTextWords = currentText.split(" ")

        val commonWords = lastTextWords.intersect(currentTextWords.toSet())
        val similarity = commonWords.size.toDouble() / maxOf(lastTextWords.size, currentTextWords.size)

        return similarity > 0.8
    }

    private fun calculateDelay(documentLength: Int): Long {
        return when {
            documentLength < 100 -> 1000
            documentLength < 500 -> 2000
            else -> 3000
        }
    }

    private fun processDocument(textBlock: List<Text.TextBlock>): List<CardData> {
        val cardDataList = mutableListOf<CardData>()

        for (block in textBlock) {
            for (line in block.lines) {
                val paragraphText = line.text
                val imageFound = checkForImageInParagraph(line)
                cardDataList.add(CardData(paragraphText, imageFound))
            }
        }

        return cardDataList
    }

    private fun checkForImageInParagraph(line: Text.Line): Boolean {
        val boundingBox = line.boundingBox
        val imageKeywords = listOf("image", "photo", "figure", "diagram", "chart", "illustration", "map", "picture")
        val words = line.elements.map { it.text.toLowerCase() }

        if (words.any { word -> imageKeywords.any { it in word } }) {
            return true
        }

        boundingBox?.let {
            val height = it.height()
            val width = it.width()

            val aspectRatio = width.toDouble() / height

            if (height > 2 * width && line.text.trim().isEmpty()) {
                return true
            }

            if (aspectRatio < 0.5 || aspectRatio > 2.0) {
                return true
            }

            val area = width * height
            val textLength = line.text.length
            val density = textLength / area.toDouble()

            if (density < 0.1) {
                return true
            }
        }

        return false
    }
}

data class CardData(
    val title: String,
    val imageFound: Boolean
)