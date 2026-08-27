package com.sanix.imageeditor.core.geometry

import com.sanix.imageeditor.core.model.Transform
import com.sanix.imageeditor.core.model.Vec2
import kotlin.test.Test
import kotlin.test.assertEquals

class GeometryTest {
    @Test
    fun identityTransformKeepsPoint() {
        assertEquals(Vec2(2f, 3f), Transform().toMatrix().map(Vec2(2f, 3f)))
    }

    @Test
    fun screenCoordinatesRespectZoomAndPan() {
        val result = screenToCanvas(Vec2(150f, 100f), Rect(0f, 0f, 300f, 200f), 2f, Vec2(10f, 20f))
        assertEquals(Vec2(10f, 20f), result)
    }
}
