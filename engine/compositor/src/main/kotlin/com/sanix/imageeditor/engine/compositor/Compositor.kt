package com.sanix.imageeditor.engine.compositor

import com.sanix.imageeditor.core.model.EditorObject
import com.sanix.imageeditor.core.model.Project

class Compositor {
    fun renderPlan(project: Project): List<EditorObject> =
        project.objects.filter { it.visible && it.opacity > 0f }
}
