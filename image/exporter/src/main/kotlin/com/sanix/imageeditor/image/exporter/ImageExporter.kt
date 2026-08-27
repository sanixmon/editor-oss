package com.sanix.imageeditor.image.exporter

import com.sanix.imageeditor.core.model.Project

enum class ExportFormat { PNG, JPEG, WEBP }
data class ExportResult(val location: String)

interface ImageExporter {
    suspend fun export(project: Project, format: ExportFormat): ExportResult
}
