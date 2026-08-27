package com.sanix.imageeditor.image.decoder

data class ImageSource(val value: String)
data class ImageMetadata(val width: Int, val height: Int, val mimeType: String?)
data class DecodedImage(val data: ByteArray, val metadata: ImageMetadata)
data class ImportedPhoto(val imageId: String, val decoded: DecodedImage)

interface ImageDecoder {
    suspend fun decode(source: ImageSource): DecodedImage
}
