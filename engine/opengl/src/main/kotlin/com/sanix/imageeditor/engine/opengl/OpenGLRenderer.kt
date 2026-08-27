package com.sanix.imageeditor.engine.opengl

import android.graphics.Bitmap
import android.opengl.GLES30
import com.sanix.imageeditor.core.model.ImageObject
import com.sanix.imageeditor.core.model.Project
import com.sanix.imageeditor.engine.compositor.Compositor
import com.sanix.imageeditor.engine.renderer.Camera
import com.sanix.imageeditor.engine.renderer.Matrix4
import com.sanix.imageeditor.engine.renderer.RenderTarget
import com.sanix.imageeditor.engine.renderer.Renderer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class OpenGLRenderer(private val compositor: Compositor = Compositor()) : Renderer {
    private var initialized = false
    private var program = 0
    private var vertexBuffer = 0
    private var textureProvider: OpenGLTextureProvider? = null
    private var texture: com.sanix.imageeditor.engine.texture.ImageTexture? = null
    private var viewportWidth = 0
    private var viewportHeight = 0
    private var imageWidth = 1
    private var imageHeight = 1

    fun uploadImage(imageId: String, bitmap: Bitmap) {
        check(initialized) { "Renderer must be initialized before image upload" }
        textureProvider?.let { provider ->
            texture?.takeUnless { it.sourceImageId == imageId }?.let(provider::release)
            texture = provider.uploadBitmap(imageId, bitmap)
            imageWidth = bitmap.width
            imageHeight = bitmap.height
        }
    }

    override fun initialize() {
        check(GLES30.glGetString(GLES30.GL_VERSION) != null) { "OpenGL ES 3.0 is unavailable" }
        val vertexShader = """
            #version 300 es
            layout(location = 0) in vec2 aPosition;
            layout(location = 1) in vec2 aTexCoord;
            uniform mat4 uMvp;
            out vec2 vTexCoord;
            void main() { gl_Position = uMvp * vec4(aPosition, 0.0, 1.0); vTexCoord = aTexCoord; }
        """.trimIndent()
        val fragmentShader = """
            #version 300 es
            precision mediump float;
            uniform sampler2D uTexture;
            uniform float uOpacity;
            in vec2 vTexCoord;
            out vec4 outColor;
            void main() { outColor = texture(uTexture, vTexCoord) * uOpacity; }
        """.trimIndent()
        program = GLUtil.linkProgram(vertexShader, fragmentShader)
        val vertices = floatArrayOf(-0.5f, -0.5f, 0f, 1f, 0.5f, -0.5f, 1f, 1f, -0.5f, 0.5f, 0f, 0f, 0.5f, 0.5f, 1f, 0f)
        val buffer = ByteBuffer.allocateDirect(vertices.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
        buffer.put(vertices).position(0)
        val buffers = IntArray(1)
        GLES30.glGenBuffers(1, buffers, 0)
        vertexBuffer = buffers[0]
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBuffer)
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.size * 4, buffer, GLES30.GL_STATIC_DRAW)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
        textureProvider = OpenGLTextureProvider()
        initialized = true
        GLUtil.checkError("initialize")
    }

    override fun resize(width: Int, height: Int) {
        check(initialized) { "Renderer must be initialized before resize" }
        require(width > 0 && height > 0)
        viewportWidth = width
        viewportHeight = height
        GLES30.glViewport(0, 0, width, height)
    }

    override fun render(project: Project, camera: Camera, target: RenderTarget) {
        check(initialized) { "Renderer must be initialized before render" }
        GLES30.glClearColor(0.08f, 0.08f, 0.08f, 1f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        val image = compositor.renderPlan(project).filterIsInstance<ImageObject>().firstOrNull() ?: return
        val imageTexture = texture ?: return
        val aspect = imageWidth.toFloat() / imageHeight
        val viewportAspect = viewportWidth.toFloat() / viewportHeight
        val fitX = if (aspect > viewportAspect) 1f else aspect / viewportAspect
        val fitY = if (aspect > viewportAspect) viewportAspect / aspect else 1f
        val projection = Matrix4.orthographic(-1f, 1f, -1f, 1f)
        val mvp = projection * camera.matrix() * image.transform.matrix() * Matrix4.scale(fitX, fitY)
        GLES30.glUseProgram(program)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBuffer)
        GLES30.glEnableVertexAttribArray(0)
        GLES30.glVertexAttribPointer(0, 2, GLES30.GL_FLOAT, false, 16, 0)
        GLES30.glEnableVertexAttribArray(1)
        GLES30.glVertexAttribPointer(1, 2, GLES30.GL_FLOAT, false, 16, 8)
        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(program, "uMvp"), 1, false, mvp.values, 0)
        GLES30.glUniform1f(GLES30.glGetUniformLocation(program, "uOpacity"), image.opacity)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureProvider?.glId(imageTexture) ?: 0)
        GLES30.glUniform1i(GLES30.glGetUniformLocation(program, "uTexture"), 0)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)
        GLES30.glDisableVertexAttribArray(0)
        GLES30.glDisableVertexAttribArray(1)
    }

    override fun dispose() {
        textureProvider?.releaseAll()
        texture = null
        if (vertexBuffer != 0) GLES30.glDeleteBuffers(1, intArrayOf(vertexBuffer), 0)
        if (program != 0) GLES30.glDeleteProgram(program)
        vertexBuffer = 0
        program = 0
        initialized = false
    }
}
