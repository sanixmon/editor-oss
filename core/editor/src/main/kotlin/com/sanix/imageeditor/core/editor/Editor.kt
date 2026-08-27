package com.sanix.imageeditor.core.editor

import com.sanix.imageeditor.core.history.CommandHistory
import com.sanix.imageeditor.core.model.Layer
import com.sanix.imageeditor.core.model.LayerId
import com.sanix.imageeditor.core.model.Project
import com.sanix.imageeditor.core.model.Transform

data class EditorState(
    val project: Project,
    val selectedLayerId: LayerId? = null,
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

    fun undo() {
        history.undo()?.let { command ->
            state = command.undo(state)
            redoHistory.push(command)
        }
    }

    fun redo() {
        redoHistory.redo()?.let { command ->
            state = command.execute(state)
            history.push(command)
        }
    }
}

data class AddLayerCommand(private val layer: Layer) : EditorCommand {
    override fun execute(state: EditorState) = state.copy(project = state.project.copy(layers = state.project.layers + layer))
    override fun undo(state: EditorState) = state.copy(project = state.project.copy(layers = state.project.layers.filterNot { it.id == layer.id }))
}

data class RemoveLayerCommand(private val layer: Layer) : EditorCommand {
    override fun execute(state: EditorState) = state.copy(project = state.project.copy(layers = state.project.layers.filterNot { it.id == layer.id }))
    override fun undo(state: EditorState) = state.copy(project = state.project.copy(layers = state.project.layers + layer))
}

data class TransformLayerCommand(private val id: LayerId, private val from: Transform, private val to: Transform) : EditorCommand {
    override fun execute(state: EditorState) = state.withTransform(id, to)
    override fun undo(state: EditorState) = state.withTransform(id, from)
}

private fun EditorState.withTransform(id: LayerId, transform: Transform) = copy(
    project = project.copy(layers = project.layers.map { layer ->
        if (layer.id == id) when (layer) {
            is com.sanix.imageeditor.core.model.ImageLayer -> layer.copy(transform = transform)
            is com.sanix.imageeditor.core.model.TextLayer -> layer.copy(transform = transform)
            is com.sanix.imageeditor.core.model.ShapeLayer -> layer.copy(transform = transform)
        } else layer
    }),
)
