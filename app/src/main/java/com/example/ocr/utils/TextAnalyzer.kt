package com.example.ocr.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.YuvImage
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.ocr.viewModel.MainViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.ByteArrayOutputStream


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

/*
class TextAnalyzer(
    private val onTextDetected: (List<Text.TextBlock>) -> Unit
) : ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private var lastDocumentText: String? = null
    private var lastProcessedTime = 0L
    private var lastImageHash: Int? = null

    */
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
*//*


    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        val bitmap = imageProxy.convertToBitmap()
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
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            } catch (e: Exception) {
                Log.e("TextAnalyzer", "Text recognition failed", e)
                imageProxy.close()
            }
        } else {
            imageProxy.close()
        }
    }


    private fun isNewDocument(currentText: String): Boolean {
        // Add a check to discard short or meaningless text
        if (currentText.length < 5 || currentText.filter { it.isLetter() }.length.toDouble() / currentText.length < 0.5) {
            return false
        }
        return lastDocumentText == null || !areDocumentsSimilar(lastDocumentText!!, currentText)
    }

    private fun areDocumentsSimilar(lastText: String, currentText: String): Boolean {
*/
/*
        val lastTextWords = lastText.split(" ")
        val currentTextWords = currentText.split(" ")

        val commonWords = lastTextWords.intersect(currentTextWords.toSet())
        val similarity = commonWords.size.toDouble() / maxOf(lastTextWords.size, currentTextWords.size)

        return similarity > 0.8
*//*

        val lastTextWords = lastText.split("\\s+".toRegex()).filter { it.isNotBlank() }
        val currentTextWords = currentText.split("\\s+".toRegex()).filter { it.isNotBlank() }

        val commonWords = lastTextWords.intersect(currentTextWords.toSet())
        val similarity = commonWords.size.toDouble() / maxOf(lastTextWords.size, currentTextWords.size)

        return similarity > 0.9  // Increase similarity threshold to 90%
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

    */
/*private fun checkForImageInParagraph(line: Text.Line): Boolean {
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
    }*//*


    private fun checkForImageInParagraph(line: Text.Line): Boolean {
        val boundingBox = line.boundingBox ?: return false
        val width = boundingBox.width()
        val height = boundingBox.height()
        val area = width * height
        val textLength = line.text.length

        val aspectRatio = width.toDouble() / height
        val density = textLength / area.toDouble()

        // Adjust the thresholds for better accuracy
        return (aspectRatio < 0.3 || aspectRatio > 3.0 || density < 0.05)
    }

    private fun getBitmapHash(bitmap: Bitmap): Int {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false)  // Resize to reduce computation
        var hash = 0
        for (y in 0 until resizedBitmap.height) {
            for (x in 0 until resizedBitmap.width) {
                val pixel = resizedBitmap.getPixel(x, y)
                hash = 31 * hash + pixel
            }
        }
        return hash
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
    private var lastImageBitmap: Bitmap? = null

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

            // Convert image to Bitmap for more detailed comparison
            val bitmap = imageProxy.convertToBitmap()
            if (bitmap == null || isSameImage(bitmap)) {
                imageProxy.close()
                return
            }

            // Use a try-finally block to ensure the image proxy is always closed
            try {
                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        val documentText = visionText.text

                        // Check if the document text is new or meaningful
                        if (isNewDocument(documentText)) {
                            lastProcessedTime = currentTime
                            lastDocumentText = documentText
                            lastImageBitmap = bitmap

                            // Callback with the recognized text blocks
                            onTextDetected(visionText.textBlocks)
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("TextAnalyzer", "Text recognition failed", e)
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            } catch (e: Exception) {
                Log.e("TextAnalyzer", "Text recognition failed", e)
                imageProxy.close()
            }
        } else {
            imageProxy.close()
        }
    }

    // Check if the current document text is new or different
    private fun isNewDocument(currentText: String): Boolean {
        // Add a check to discard short or meaningless text
        if (currentText.length < 5 || currentText.filter { it.isLetter() }.length.toDouble() / currentText.length < 0.5) {
            return false
        }
        return lastDocumentText == null || !areDocumentsSimilar(lastDocumentText!!, currentText)
    }

    // Check if the image has changed meaningfully using bitmap comparison
    private fun isSameImage(currentBitmap: Bitmap): Boolean {
        lastImageBitmap?.let { lastBitmap ->
            // Compare the last and current bitmap using a pixel difference or feature similarity
            val similarity = compareBitmaps(lastBitmap, currentBitmap)
            return similarity > 0.9 // Adjust threshold for acceptable similarity
        }
        return false
    }

    // Compare two bitmaps for similarity
    private fun compareBitmaps(bmp1: Bitmap, bmp2: Bitmap): Double {
        if (bmp1.width != bmp2.width || bmp1.height != bmp2.height) return 0.0

        var diff = 0
        for (y in 0 until bmp1.height) {
            for (x in 0 until bmp1.width) {
                val pixel1 = bmp1.getPixel(x, y)
                val pixel2 = bmp2.getPixel(x, y)
                if (pixel1 != pixel2) {
                    diff++
                }
            }
        }
        val totalPixels = bmp1.width * bmp1.height
        return 1.0 - (diff.toDouble() / totalPixels)
    }

    // Function to compare documents for similarity based on words
    private fun areDocumentsSimilar(lastText: String, currentText: String): Boolean {
        val lastTextWords = lastText.split(" ")
        val currentTextWords = currentText.split(" ")

        val commonWords = lastTextWords.intersect(currentTextWords.toSet())
        val similarity = commonWords.size.toDouble() / maxOf(lastTextWords.size, currentTextWords.size)

        return similarity > 0.8
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