package com.sanix.imageeditor.engine.renderer

import com.sanix.imageeditor.core.model.Transform
import kotlin.math.cos
import kotlin.math.sin

class Matrix4 private constructor(val values: FloatArray) {
    operator fun times(other: Matrix4): Matrix4 {
        val out = FloatArray(16)
        for (row in 0..3) for (column in 0..3) {
            out[column * 4 + row] = (0..3).sumOf { k ->
                (values[k * 4 + row] * other.values[column * 4 + k]).toDouble()
            }.toFloat()
        }
        return Matrix4(out)
    }

    companion object {
        fun identity() = Matrix4(floatArrayOf(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f))
        fun translation(x: Float, y: Float) = identity().also { it.values[12] = x; it.values[13] = y }
        fun scale(x: Float, y: Float) = identity().also { it.values[0] = x; it.values[5] = y }
        fun rotation(degrees: Float): Matrix4 {
            val radians = Math.toRadians(degrees.toDouble())
            val c = cos(radians).toFloat()
            val s = sin(radians).toFloat()
            return Matrix4(floatArrayOf(c, s, 0f, 0f, -s, c, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f))
        }
        fun orthographic(left: Float, right: Float, bottom: Float, top: Float): Matrix4 = Matrix4(floatArrayOf(
            2f / (right - left), 0f, 0f, 0f,
            0f, 2f / (top - bottom), 0f, 0f,
            0f, 0f, -1f, 0f,
            -(right + left) / (right - left), -(top + bottom) / (top - bottom), 0f, 1f,
        ))
    }
}

fun Transform.matrix() = Matrix4.translation(position.x, position.y) * Matrix4.rotation(rotation) * Matrix4.scale(scale.x, scale.y)
fun Camera.matrix() = Matrix4.translation(-panX, -panY) * Matrix4.rotation(-rotation) * Matrix4.scale(zoom, zoom)
