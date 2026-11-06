package com.museumassistant.ocr

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class OcrReader {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    fun read(bitmap: Bitmap, onResult: (List<String>, Float) -> Unit, onError: (Exception) -> Unit) {
        recognizer.process(InputImage.fromBitmap(bitmap, 0))
            .addOnSuccessListener { result ->
                val lines = result.textBlocks.flatMap { block -> block.lines.map { it.text } }
                val quality = if (lines.isEmpty()) 0f else (lines.size.coerceAtMost(5) / 5f)
                onResult(lines, quality)
            }.addOnFailureListener { onError(it) }
    }
    fun close() = recognizer.close()
}
