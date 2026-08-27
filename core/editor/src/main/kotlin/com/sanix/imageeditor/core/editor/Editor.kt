package com.sanix.imageeditor.core.editor

import com.sanix.imageeditor.core.history.CommandHistory
import com.sanix.imageeditor.core.model.EditorObject
import com.sanix.imageeditor.core.model.EffectObject
import com.sanix.imageeditor.core.model.ImageObject
import com.sanix.imageeditor.core.model.ObjectId
import com.sanix.imageeditor.core.model.Project
import com.sanix.imageeditor.core.model.ShapeObject
import com.sanix.imageeditor.core.model.SpeechBubbleObject
import com.sanix.imageeditor.core.model.StickerObject
import com.sanix.imageeditor.core.model.TextObject
import com.sanix.imageeditor.core.model.Transform

data class EditorState(val project: Project, val selectedObjectId: ObjectId? = null)

interface EditorCommand {
    fun execute(state: EditorState): EditorState
    fun undo(state: EditorState): EditorState
}

class Editor(initialState: EditorState) {
    var state: EditorState = initialState
        private set
    private val history = CommandHistory<EditorCommand>()
    private val redoHistory = CommandHistory<EditorCommand>()

    fun execute(command: EditorCommand) { state = command.execute(state); history.push(command); redoHistory.clear() }
    fun undo() { history.undo()?.let { state = it.undo(state); redoHistory.push(it) } }
    fun redo() { redoHistory.redo()?.let { state = it.execute(state); history.push(it) } }
}

data class SelectObjectCommand(private val id: ObjectId?) : EditorCommand {
    private var previous: ObjectId? = null
    override fun execute(state: EditorState): EditorState { previous = state.selectedObjectId; return state.copy(selectedObjectId = id) }
    override fun undo(state: EditorState) = state.copy(selectedObjectId = previous)
}

data class AddObjectCommand(private val objectToAdd: EditorObject) : EditorCommand {
    override fun execute(state: EditorState) = state.copy(project = state.project.copy(objects = state.project.objects + objectToAdd), selectedObjectId = objectToAdd.id)
    override fun undo(state: EditorState) = state.copy(project = state.project.copy(objects = state.project.objects.filterNot { it.id == objectToAdd.id }), selectedObjectId = null)
}

data class RemoveObjectCommand(private val objectToRemove: EditorObject) : EditorCommand {
    override fun execute(state: EditorState) = state.copy(project = state.project.copy(objects = state.project.objects.filterNot { it.id == objectToRemove.id }))
    override fun undo(state: EditorState) = state.copy(project = state.project.copy(objects = state.project.objects + objectToRemove))
}

data class TransformObjectCommand(private val id: ObjectId, private val from: Transform, private val to: Transform) : EditorCommand {
    override fun execute(state: EditorState) = state.withObject(id) { it.updateTransform(to) }
    override fun undo(state: EditorState) = state.withObject(id) { it.updateTransform(from) }
}

data class SetObjectOpacityCommand(private val id: ObjectId, private val from: Float, private val to: Float) : EditorCommand {
    override fun execute(state: EditorState) = state.withObject(id) { it.updateOpacity(to) }
    override fun undo(state: EditorState) = state.withObject(id) { it.updateOpacity(from) }
}

private fun EditorState.withObject(id: ObjectId, update: (EditorObject) -> EditorObject) = copy(
    project = project.copy(objects = project.objects.map { if (it.id == id) update(it) else it }),
)

private fun EditorObject.updateTransform(value: Transform): EditorObject = when (this) {
    is ImageObject -> copy(transform = value)
    is TextObject -> copy(transform = value)
    is StickerObject -> copy(transform = value)
    is ShapeObject -> copy(transform = value)
    is SpeechBubbleObject -> copy(transform = value)
    is EffectObject -> copy(transform = value)
}

private fun EditorObject.updateOpacity(value: Float): EditorObject {
    require(value in 0f..1f) { "Opacity must be between 0 and 1" }
    return when (this) {
        is ImageObject -> copy(opacity = value)
        is TextObject -> copy(opacity = value)
        is StickerObject -> copy(opacity = value)
        is ShapeObject -> copy(opacity = value)
        is SpeechBubbleObject -> copy(opacity = value)
        is EffectObject -> copy(opacity = value)
    }
}
