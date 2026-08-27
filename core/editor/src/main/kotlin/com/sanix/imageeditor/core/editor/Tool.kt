package com.sanix.imageeditor.core.editor

import com.sanix.imageeditor.core.model.EditorObject

data class ToolContext(
    val selectedObjectId: String? = null,
)

interface EditorTool {
    val id: String
    val displayName: String
    fun create(context: ToolContext): EditorObject
}
