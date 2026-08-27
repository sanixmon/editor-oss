package com.sanix.imageeditor.feature.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EditorScreen() {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("←  Project", style = MaterialTheme.typography.titleMedium)
            Text("↶  ↷     Export", style = MaterialTheme.typography.titleMedium)
        }
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text("CANVAS", modifier = Modifier.fillMaxWidth(), color = Color.Gray)
        }
        Text(
            "Layers   Brush   Text   Crop   More",
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}
