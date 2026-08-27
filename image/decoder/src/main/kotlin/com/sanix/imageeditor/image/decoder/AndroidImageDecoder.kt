package com.sanix.imageeditor.image.decoder

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidImageDecoder(private val contentResolver: ContentResolver) : ImageDecoder {
    override suspend fun decode(source: ImageSource): DecodedImage = withContext(Dispatchers.IO) {
        val uri = Uri.parse(source.value)
        val decoded = decodeBitmap(source)
        val bytes = contentResolver.openInputStream(uri).use { input -> input?.readBytes() }
            ?: error("Unable to read image: $uri")
        DecodedImage(bytes, ImageMetadata(decoded.width, decoded.height, contentResolver.getType(uri)))
    }

    suspend fun decodeBitmap(source: ImageSource): Bitmap = withContext(Dispatchers.IO) {
        val uri = Uri.parse(source.value)
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        contentResolver.openInputStream(uri).use { input -> BitmapFactory.decodeStream(input, null, bounds) }
        require(bounds.outWidth > 0 && bounds.outHeight > 0) { "Unable to decode image: $uri" }
        val options = BitmapFactory.Options().apply { inSampleSize = sampleSize(bounds.outWidth, bounds.outHeight) }
        contentResolver.openInputStream(uri).use { input -> BitmapFactory.decodeStream(input, null, options) }
            ?: error("Unable to decode image: $uri")
    }

    private fun sampleSize(width: Int, height: Int): Int {
        var sample = 1
        while (width / sample > MAX_DIMENSION || height / sample > MAX_DIMENSION) sample *= 2
        return sample
    }

    companion object { private const val MAX_DIMENSION = 4096 }
}
