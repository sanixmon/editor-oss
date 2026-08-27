package com.sanix.imageeditor.engine.opengl

import android.graphics.Bitmap
import android.opengl.GLES30
import android.opengl.GLUtils
import com.sanix.imageeditor.engine.texture.ImageTexture
import com.sanix.imageeditor.engine.texture.TextureId
import com.sanix.imageeditor.engine.texture.TextureProvider
import com.sanix.imageeditor.engine.texture.TextureSize

class OpenGLTextureProvider : TextureProvider {
    private val textures = mutableMapOf<String, Pair<ImageTexture, Int>>()

    fun uploadBitmap(imageId: String, bitmap: Bitmap): ImageTexture {
        textures[imageId]?.let { return it.first }
        val ids = IntArray(1)
        GLES30.glGenTextures(1, ids, 0)
        check(ids[0] != 0) { "Unable to allocate image texture" }
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, ids[0])
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE)
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)
        GLUtil.checkError("upload texture")
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)
        val texture = ImageTexture(TextureId(imageId), TextureSize(bitmap.width, bitmap.height), imageId)
        textures[imageId] = texture to ids[0]
        return texture
    }

    fun glId(texture: ImageTexture): Int = textures[texture.sourceImageId]?.second ?: 0

    override fun upload(imageId: String, width: Int, height: Int): ImageTexture =
        error("Use uploadBitmap for Android image textures")

    override fun release(texture: ImageTexture) {
        textures.remove(texture.sourceImageId)?.let { GLES30.glDeleteTextures(1, intArrayOf(it.second), 0) }
    }

    fun releaseAll() {
        textures.values.forEach { GLES30.glDeleteTextures(1, intArrayOf(it.second), 0) }
        textures.clear()
    }
}
