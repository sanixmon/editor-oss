package com.sanix.imageeditor.core.geometry

import com.sanix.imageeditor.core.model.Transform
import com.sanix.imageeditor.core.model.Vec2
import kotlin.math.cos
import kotlin.math.sin

data class Rect(val left: Float, val top: Float, val right: Float, val bottom: Float) {
    val width get() = right - left
    val height get() = bottom - top
}

data class Matrix3(val values: FloatArray) {
    init { require(values.size == 9) }

    companion object {
        fun identity() = Matrix3(floatArrayOf(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f))
    }

    fun map(point: Vec2): Vec2 = Vec2(
        values[0] * point.x + values[1] * point.y + values[2],
        values[3] * point.x + values[4] * point.y + values[5],
    )
}

fun Transform.toMatrix(): Matrix3 {
    val radians = Math.toRadians(rotation.toDouble())
    val c = cos(radians).toFloat()
    val s = sin(radians).toFloat()
    return Matrix3(floatArrayOf(
        c * scale.x, -s * scale.y, position.x,
        s * scale.x, c * scale.y, position.y,
        0f, 0f, 1f,
    ))
}

fun screenToCanvas(point: Vec2, viewport: Rect, zoom: Float, pan: Vec2): Vec2 = Vec2(
    (point.x - viewport.left - viewport.width / 2f) / zoom + pan.x,
    (point.y - viewport.top - viewport.height / 2f) / zoom + pan.y,
)
