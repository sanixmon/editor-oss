package com.sanix.imageeditor.engine.texture

data class TextureSize(val width: Int, val height: Int) {
    init { require(width > 0 && height > 0) }
}

data class TextureId(val value: String)

data class ImageTexture(
    val id: TextureId,
    val size: TextureSize,
    val sourceImageId: String,
)

interface TextureProvider {
    fun upload(imageId: String, width: Int, height: Int): ImageTexture
    fun release(texture: ImageTexture)
}
