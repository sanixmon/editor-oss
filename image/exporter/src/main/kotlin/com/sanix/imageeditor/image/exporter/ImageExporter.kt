package com.sanix.imageeditor.image.exporter

enum class ExportFormat { PNG, JPEG, WEBP }
data class RenderedImage(val bytes: ByteArray, val width: Int, val height: Int)
data class ExportResult(val location: String)

interface ImageExporter {
    suspend fun export(image: RenderedImage, format: ExportFormat): ExportResult
}
