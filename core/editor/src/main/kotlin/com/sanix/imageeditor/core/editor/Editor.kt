package com.sanix.imageeditor.core.editor

import com.sanix.imageeditor.core.history.CommandHistory
import com.sanix.imageeditor.core.model.EditorObject
import com.sanix.imageeditor.core.model.ObjectId
import com.sanix.imageeditor.core.model.Project
import com.sanix.imageeditor.core.model.Transform

data class EditorState(
    val project: Project,
    val selectedObjectId: ObjectId? = null,
)

interface EditorCommand {
    fun execute(state: EditorState): EditorState
    fun undo(state: EditorState): EditorState
}

class Editor(initialState: EditorState) {
    var state: EditorState = initialState
        private set
    private val history = CommandHistory<EditorCommand>()
    private val redoHistory = CommandHistory<EditorCommand>()

    fun execute(command: EditorCommand) {
        state = command.execute(state)
        history.push(command)
        redoHistory.clear()
    }
    fun undo() { history.undo()?.let { state = it.undo(state); redoHistory.push(it) } }
    fun redo() { redoHistory.redo()?.let { state = it.execute(state); history.push(it) } }
}

data class AddObjectCommand(private val objectToAdd: EditorObject) : EditorCommand {
    override fun execute(state: EditorState) = state.copy(project = state.project.copy(objects = state.project.objects + objectToAdd))
    override fun undo(state: EditorState) = state.copy(project = state.project.copy(objects = state.project.objects.filterNot { it.id == objectToAdd.id }))
}

data class RemoveObjectCommand(private val objectToRemove: EditorObject) : EditorCommand {
    override fun execute(state: EditorState) = state.copy(project = state.project.copy(objects = state.project.objects.filterNot { it.id == objectToRemove.id }))
    override fun undo(state: EditorState) = state.copy(project = state.project.copy(objects = state.project.objects + objectToRemove))
}

data class TransformObjectCommand(private val id: ObjectId, private val from: Transform, private val to: Transform) : EditorCommand {
    override fun execute(state: EditorState) = state.withTransform(id, to)
    override fun undo(state: EditorState) = state.withTransform(id, from)
}

private fun EditorState.withTransform(id: ObjectId, transform: Transform) = copy(
    project = project.copy(objects = project.objects.map { it.updateTransform(id, transform) }),
)

private fun EditorObject.updateTransform(id: ObjectId, transform: Transform): EditorObject =
    if (this.id == id) when (this) {
        is com.sanix.imageeditor.core.model.ImageObject -> copy(transform = transform)
        is com.sanix.imageeditor.core.model.TextObject -> copy(transform = transform)
        is com.sanix.imageeditor.core.model.StickerObject -> copy(transform = transform)
        is com.sanix.imageeditor.core.model.ShapeObject -> copy(transform = transform)
        is com.sanix.imageeditor.core.model.SpeechBubbleObject -> copy(transform = transform)
        is com.sanix.imageeditor.core.model.EffectObject -> copy(transform = transform)
    } else this
