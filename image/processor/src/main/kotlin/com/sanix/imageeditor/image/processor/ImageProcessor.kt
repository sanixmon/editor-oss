package com.sanix.imageeditor.image.processor

data class ImageData(val bytes: ByteArray, val width: Int, val height: Int)

sealed interface ImageOperation {
    data class Brightness(val amount: Float) : ImageOperation
    data class Contrast(val amount: Float) : ImageOperation
    data class Saturation(val amount: Float) : ImageOperation
}

interface ImageProcessor {
    suspend fun process(image: ImageData, operation: ImageOperation): ImageData
}
