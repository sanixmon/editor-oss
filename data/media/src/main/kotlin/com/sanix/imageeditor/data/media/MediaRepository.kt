package com.sanix.imageeditor.data.media

import com.sanix.imageeditor.image.decoder.ImageSource

interface MediaRepository {
    fun pickImage(): ImageSource?
    fun exportImage(source: ByteArray, displayName: String, mimeType: String): String?
}
