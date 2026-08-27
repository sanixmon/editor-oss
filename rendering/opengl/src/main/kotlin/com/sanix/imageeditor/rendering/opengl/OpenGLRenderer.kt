package com.sanix.imageeditor.rendering.opengl

import com.sanix.imageeditor.core.model.Project
import com.sanix.imageeditor.rendering.RenderTarget
import com.sanix.imageeditor.rendering.Renderer

class OpenGLRenderer : Renderer {
    private var initialized = false

    override fun initialize() { initialized = true }
    override fun resize(width: Int, height: Int) {
        check(initialized) { "Renderer must be initialized before resize" }
        require(width > 0 && height > 0)
    }
    override fun render(project: Project, target: RenderTarget) {
        check(initialized) { "Renderer must be initialized before render" }
        project.layers.asSequence().filter { it.visible }.forEach { /* GPU compositing will be added in Phase 2. */ }
    }
    override fun dispose() { initialized = false }
}
