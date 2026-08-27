package com.sanix.imageeditor.core.editor

import com.sanix.imageeditor.core.model.CanvasSize
import com.sanix.imageeditor.core.model.ImageObject
import com.sanix.imageeditor.core.model.ObjectId
import com.sanix.imageeditor.core.model.Project
import com.sanix.imageeditor.core.model.Transform
import com.sanix.imageeditor.core.model.Vec2
import kotlin.test.Test
import kotlin.test.assertEquals

class EditorTest {
    @Test
    fun addTransformUndoAndRedoObject() {
        val image = ImageObject(ObjectId("photo"), imageId = "source")
        val editor = Editor(EditorState(Project("project", CanvasSize(100, 100))))
        editor.execute(AddObjectCommand(image))
        val moved = Transform(position = Vec2(20f, 10f))
        editor.execute(TransformObjectCommand(image.id, image.transform, moved))
        assertEquals(moved, editor.state.project.objects.single().transform)
        editor.undo()
        assertEquals(image.transform, editor.state.project.objects.single().transform)
        editor.redo()
        assertEquals(moved, editor.state.project.objects.single().transform)
        editor.undo()
        editor.undo()
        assertEquals(0, editor.state.project.objects.size)
    }
}
