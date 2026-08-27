package com.sanix.imageeditor.engine.renderer

import kotlin.test.Test
import kotlin.test.assertEquals

class CameraTest {
    @Test fun zoomIsClamped() {
        assertEquals(Camera.MIN_ZOOM, Camera().zoomedBy(0f).zoom)
        assertEquals(Camera.MAX_ZOOM, Camera().zoomedBy(100f).zoom)
    }
    @Test fun panIsAdjustedForZoom() {
        assertEquals(5f, Camera(zoom = 2f).pannedBy(10f, 0f).panX)
    }
    @Test fun rotationIsAccumulated() {
        assertEquals(25f, Camera().rotatedBy(10f).rotatedBy(15f).rotation)
    }
}
