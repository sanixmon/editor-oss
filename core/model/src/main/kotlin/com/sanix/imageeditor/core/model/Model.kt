package com.sanix.imageeditor.core.model

data class Project(
    val id: String,
    val canvas: CanvasSize,
    val objects: List<EditorObject> = emptyList(),
)

data class CanvasSize(val width: Int, val height: Int) {
    init { require(width > 0 && height > 0) { "Canvas dimensions must be positive" } }
}

data class ObjectId(val value: String)
data class Vec2(val x: Float, val y: Float)

data class Transform(
    val position: Vec2 = Vec2(0f, 0f),
    val scale: Vec2 = Vec2(1f, 1f),
    val rotation: Float = 0f,
)

enum class ObjectType { IMAGE, TEXT, STICKER, SHAPE, SPEECH_BUBBLE, EFFECT }
enum class BlendMode { NORMAL, MULTIPLY, SCREEN, OVERLAY }
data class Color(val red: Float, val green: Float, val blue: Float, val alpha: Float = 1f)

data class EffectParameters(val values: Map<String, Float> = emptyMap()) {
    operator fun get(key: String): Float? = values[key]
}

sealed interface EditorObject {
    val id: ObjectId
    val transform: Transform
    val opacity: Float
    val visible: Boolean
    val type: ObjectType
}

data class ImageObject(
    override val id: ObjectId,
    override val transform: Transform = Transform(),
    override val opacity: Float = 1f,
    override val visible: Boolean = true,
    val imageId: String,
) : EditorObject { override val type = ObjectType.IMAGE }

data class TextObject(
    override val id: ObjectId,
    override val transform: Transform = Transform(),
    override val opacity: Float = 1f,
    override val visible: Boolean = true,
    val text: String,
    val color: Color = Color(0f, 0f, 0f),
) : EditorObject { override val type = ObjectType.TEXT }

data class StickerObject(
    override val id: ObjectId,
    override val transform: Transform = Transform(),
    override val opacity: Float = 1f,
    override val visible: Boolean = true,
    val assetId: String,
) : EditorObject { override val type = ObjectType.STICKER }

data class ShapeObject(
    override val id: ObjectId,
    override val transform: Transform = Transform(),
    override val opacity: Float = 1f,
    override val visible: Boolean = true,
    val color: Color = Color(1f, 1f, 1f),
) : EditorObject { override val type = ObjectType.SHAPE }

data class SpeechBubbleObject(
    override val id: ObjectId,
    override val transform: Transform = Transform(),
    override val opacity: Float = 1f,
    override val visible: Boolean = true,
    val text: String,
    val color: Color = Color(1f, 1f, 1f),
) : EditorObject { override val type = ObjectType.SPEECH_BUBBLE }

data class EffectObject(
    override val id: ObjectId,
    override val transform: Transform = Transform(),
    override val opacity: Float = 1f,
    override val visible: Boolean = true,
    val effectId: String,
    val parameters: EffectParameters = EffectParameters(),
) : EditorObject { override val type = ObjectType.EFFECT }
