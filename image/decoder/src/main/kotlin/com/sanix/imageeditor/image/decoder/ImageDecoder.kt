package com.sanix.imageeditor.image.decoder

data class ImageSource(val value: String)
data class ImageMetadata(val width: Int, val height: Int, val mimeType: String?)
data class DecodedImage(val data: ByteArray, val metadata: ImageMetadata)

interface ImageDecoder {
    suspend fun decode(source: ImageSource): DecodedImage
}
