package com.sanix.imageeditor.core.model

data class Project(
    val id: String,
    val canvas: CanvasSize,
    val layers: List<Layer> = emptyList(),
)

data class CanvasSize(val width: Int, val height: Int) {
    init {
        require(width > 0 && height > 0) { "Canvas dimensions must be positive" }
    }
}

data class LayerId(val value: String)

data class Vec2(val x: Float, val y: Float)

data class Transform(
    val position: Vec2 = Vec2(0f, 0f),
    val scale: Vec2 = Vec2(1f, 1f),
    val rotation: Float = 0f,
)

enum class LayerType { IMAGE, TEXT, SHAPE }
enum class BlendMode { NORMAL, MULTIPLY, SCREEN, OVERLAY }

data class Color(val red: Float, val green: Float, val blue: Float, val alpha: Float = 1f)

sealed interface Layer {
    val id: LayerId
    val transform: Transform
    val opacity: Float
    val visible: Boolean
    val type: LayerType
}

data class ImageLayer(
    override val id: LayerId,
    override val transform: Transform = Transform(),
    override val opacity: Float = 1f,
    override val visible: Boolean = true,
    val imageId: String,
    val blendMode: BlendMode = BlendMode.NORMAL,
) : Layer {
    override val type = LayerType.IMAGE
}

data class TextLayer(
    override val id: LayerId,
    override val transform: Transform = Transform(),
    override val opacity: Float = 1f,
    override val visible: Boolean = true,
    val text: String,
    val color: Color = Color(0f, 0f, 0f),
) : Layer {
    override val type = LayerType.TEXT
}

data class ShapeLayer(
    override val id: LayerId,
    override val transform: Transform = Transform(),
    override val opacity: Float = 1f,
    override val visible: Boolean = true,
    val color: Color = Color(1f, 1f, 1f),
) : Layer {
    override val type = LayerType.SHAPE
}
