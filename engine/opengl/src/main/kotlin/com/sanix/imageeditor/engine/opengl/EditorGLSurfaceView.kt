package com.sanix.imageeditor.engine.opengl

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import com.sanix.imageeditor.core.model.Project
import com.sanix.imageeditor.engine.renderer.Camera
import com.sanix.imageeditor.engine.renderer.RenderTarget
import com.sanix.imageeditor.engine.texture.TextureSize
import kotlin.math.hypot

class EditorGLSurfaceView(context: Context) : GLSurfaceView(context) {
    private val glRenderer = SurfaceRenderer()
    private var previousDistance = 0f
    private var previousX = 0f
    private var previousY = 0f

    init {
        setEGLContextClientVersion(3)
        setRenderer(glRenderer)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    fun setProject(project: Project) { queueEvent { glRenderer.project = project }; requestRender() }
    fun uploadBitmap(imageId: String, bitmap: Bitmap) { queueEvent { glRenderer.renderer.uploadImage(imageId, bitmap); bitmap.recycle(); requestRender() } }
    fun updateCamera(camera: Camera) { queueEvent { glRenderer.camera = camera }; requestRender() }
    fun rotateBy(degrees: Float) { updateCamera(currentCamera().rotatedBy(degrees)) }
    fun currentCamera(): Camera = glRenderer.camera

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> { previousX = event.x; previousY = event.y }
            MotionEvent.ACTION_POINTER_DOWN -> if (event.pointerCount >= 2) previousDistance = distance(event)
            MotionEvent.ACTION_MOVE -> when {
                event.pointerCount >= 2 -> {
                    val distance = distance(event)
                    if (previousDistance > 0f) updateCamera(currentCamera().zoomedBy(distance / previousDistance))
                    previousDistance = distance
                }
                event.pointerCount == 1 -> {
                    updateCamera(currentCamera().pannedBy(event.x - previousX, previousY - event.y))
                    previousX = event.x; previousY = event.y
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> previousDistance = 0f
        }
        return true
    }

    private fun distance(event: MotionEvent): Float = hypot(event.getX(0) - event.getX(1), event.getY(0) - event.getY(1))

    private class SurfaceRenderer : GLSurfaceView.Renderer {
        val renderer = OpenGLRenderer()
        var project = Project("empty", com.sanix.imageeditor.core.model.CanvasSize(1, 1))
        var camera = Camera()
        private var target = object : RenderTarget { override val size = TextureSize(1, 1) }

        override fun onSurfaceCreated(gl: javax.microedition.khronos.opengles.GL10?, config: javax.microedition.khronos.egl.EGLConfig?) { renderer.initialize() }
        override fun onSurfaceChanged(gl: javax.microedition.khronos.opengles.GL10?, width: Int, height: Int) { target = object : RenderTarget { override val size = TextureSize(width, height) }; renderer.resize(width, height) }
        override fun onDrawFrame(gl: javax.microedition.khronos.opengles.GL10?) { renderer.render(project, camera, target) }
    }
}
