package com.sanix.imageeditor.rendering

import com.sanix.imageeditor.core.model.Project

interface Renderer {
    fun initialize()
    fun resize(width: Int, height: Int)
    fun render(project: Project, target: RenderTarget)
    fun dispose()
}

interface RenderTarget
interface Texture
interface Shader
