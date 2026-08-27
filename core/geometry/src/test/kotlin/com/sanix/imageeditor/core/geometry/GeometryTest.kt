package com.sanix.imageeditor.core.geometry

import com.sanix.imageeditor.core.model.Transform
import com.sanix.imageeditor.core.model.Vec2
import kotlin.test.Test
import kotlin.test.assertEquals

class GeometryTest {
    @Test fun identityTransformKeepsPoint() {
        assertEquals(Vec2(2f, 3f), Transform().toMatrix().map(Vec2(2f, 3f)))
    }
    @Test fun translationMovesPoint() {
        assertEquals(Vec2(7f, 1f), Transform(position = Vec2(5f, -2f)).toMatrix().map(Vec2(2f, 3f)))
    }
    @Test fun scaleMovesPoint() {
        assertEquals(Vec2(4f, 9f), Transform(scale = Vec2(2f, 3f)).toMatrix().map(Vec2(2f, 3f)))
    }
    @Test fun rotationTurnsPoint() {
        val result = Transform(rotation = 90f).toMatrix().map(Vec2(1f, 0f))
        assertEquals(0f, result.x, 0.0001f)
        assertEquals(1f, result.y, 0.0001f)
    }
    @Test fun screenCoordinatesRespectZoomAndPan() {
        val result = screenToCanvas(Vec2(150f, 100f), Rect(0f, 0f, 300f, 200f), 2f, Vec2(10f, 20f))
        assertEquals(Vec2(10f, 20f), result)
    }
}
