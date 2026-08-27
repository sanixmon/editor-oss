package com.sanix.imageeditor.rendering

import com.sanix.imageeditor.core.model.EffectParameters
import com.sanix.imageeditor.core.model.Project

interface Renderer {
    fun initialize()
    fun resize(width: Int, height: Int)
    fun render(project: Project, target: RenderTarget)
    fun dispose()
}

interface Effect {
    val id: String
    val name: String
    fun apply(input: Texture, parameters: EffectParameters): Texture
}

interface RenderTarget
interface Texture
interface Shader
