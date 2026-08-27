package com.sanix.imageeditor.data.project

import com.sanix.imageeditor.core.model.Project

interface ProjectRepository {
    suspend fun load(id: String): Project?
    suspend fun save(project: Project)
}

interface ProjectSerializer {
    fun serialize(project: Project): String
    fun deserialize(value: String): Project
}
