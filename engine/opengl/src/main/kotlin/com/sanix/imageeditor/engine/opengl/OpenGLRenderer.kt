package com.sanix.imageeditor.engine.opengl

import com.sanix.imageeditor.core.model.Project
import com.sanix.imageeditor.engine.compositor.Compositor
import com.sanix.imageeditor.engine.renderer.Camera
import com.sanix.imageeditor.engine.renderer.RenderTarget
import com.sanix.imageeditor.engine.renderer.Renderer

class OpenGLRenderer(
    private val compositor: Compositor = Compositor(),
) : Renderer {
    private var initialized = false
    private var viewportWidth = 0
    private var viewportHeight = 0

    override fun initialize() { initialized = true }

    override fun resize(width: Int, height: Int) {
        check(initialized) { "Renderer must be initialized before resize" }
        require(width > 0 && height > 0)
        viewportWidth = width
        viewportHeight = height
    }

    override fun render(project: Project, camera: Camera, target: RenderTarget) {
        check(initialized) { "Renderer must be initialized before render" }
        check(viewportWidth > 0 && viewportHeight > 0) { "Renderer must be resized before render" }
        compositor.renderPlan(project).forEach { renderObject(it, camera) }
    }

    private fun renderObject(objectToRender: com.sanix.imageeditor.core.model.EditorObject, camera: Camera) {
        // Phase 1 deliberately records the render boundary; GLES texture binding and draw calls land next.
        objectToRender.transform
        camera.zoom
    }

    override fun dispose() {
        initialized = false
        viewportWidth = 0
        viewportHeight = 0
    }
}
