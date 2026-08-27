package com.sanix.imageeditor.core.history

interface History<T> {
    fun push(command: T)
    fun undo(): T?
    fun redo(): T?
    fun clear()
}

class CommandHistory<T> : History<T> {
    private val undoStack = ArrayDeque<T>()
    private val redoStack = ArrayDeque<T>()

    override fun push(command: T) {
        undoStack.addLast(command)
        redoStack.clear()
    }

    override fun undo(): T? = undoStack.removeLastOrNull()?.also(redoStack::addLast)
    override fun redo(): T? = redoStack.removeLastOrNull()?.also(undoStack::addLast)
    override fun clear() { undoStack.clear(); redoStack.clear() }
}
