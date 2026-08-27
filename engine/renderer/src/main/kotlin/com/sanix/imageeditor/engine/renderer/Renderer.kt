package com.sanix.imageeditor.engine.renderer

import com.sanix.imageeditor.core.model.Project
import com.sanix.imageeditor.engine.texture.ImageTexture
import com.sanix.imageeditor.engine.texture.TextureSize

interface Renderer {
    fun initialize()
    fun resize(width: Int, height: Int)
    fun render(project: Project, camera: Camera, target: RenderTarget)
    fun dispose()
}

data class Camera(
    val zoom: Float = 1f,
    val panX: Float = 0f,
    val panY: Float = 0f,
    val rotation: Float = 0f,
) {
    init { require(zoom > 0f) { "Zoom must be positive" } }
    fun zoomedBy(factor: Float): Camera = copy(zoom = (zoom * factor).coerceIn(MIN_ZOOM, MAX_ZOOM))
    fun pannedBy(dx: Float, dy: Float): Camera = copy(panX = panX + dx / zoom, panY = panY + dy / zoom)
    fun rotatedBy(degrees: Float): Camera = copy(rotation = rotation + degrees)
    companion object { const val MIN_ZOOM = 0.05f; const val MAX_ZOOM = 32f }
}

interface RenderTarget { val size: TextureSize }
interface TextureRegistry { fun textureFor(imageId: String): ImageTexture? }
