package com.sanix.imageeditor.feature.editor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.sanix.imageeditor.core.model.CanvasSize
import com.sanix.imageeditor.core.model.ImageObject
import com.sanix.imageeditor.core.model.ObjectId
import com.sanix.imageeditor.core.model.Project
import com.sanix.imageeditor.engine.opengl.EditorGLSurfaceView
import com.sanix.imageeditor.image.decoder.AndroidImageDecoder
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@Composable
fun EditorScreen() {
    val context = LocalContext.current
    val scope = remember { MainScope() }
    var project by remember { mutableStateOf(Project("untitled", CanvasSize(1, 1))) }
    var surface by remember { mutableStateOf<EditorGLSurfaceView?>(null) }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult
        scope.launch {
            val decoder = AndroidImageDecoder(context.contentResolver)
            val bitmap = decoder.decodeBitmap(com.sanix.imageeditor.image.decoder.ImageSource(uri.toString()))
            val objectId = ObjectId("photo-${uri.hashCode()}")
            project = Project("photo-project", CanvasSize(bitmap.width, bitmap.height), listOf(ImageObject(objectId, imageId = objectId.value)))
            surface?.setProject(project)
            surface?.uploadBitmap(objectId.value, bitmap)
        }
    }
    DisposableEffect(Unit) { onDispose { scope.cancel() } }

    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { picker.launch("image/*") }) { Text("Open image") }
            Text("GPU preview", style = MaterialTheme.typography.titleMedium)
        }
        AndroidView(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            factory = { EditorGLSurfaceView(it).also { view -> surface = view; view.setProject(project) } },
            update = { surface = it },
        )
        Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { surface?.updateCamera(surface?.currentCamera()?.zoomedBy(1.15f) ?: return@Button) }) { Text("Zoom +") }
            Button(onClick = { surface?.updateCamera(surface?.currentCamera()?.zoomedBy(0.87f) ?: return@Button) }) { Text("Zoom -") }
            Button(onClick = { surface?.rotateBy(15f) }) { Text("Rotate") }
        }
        Text("Drag to pan • pinch to zoom • Rotate uses GPU matrix", Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
    }
}
