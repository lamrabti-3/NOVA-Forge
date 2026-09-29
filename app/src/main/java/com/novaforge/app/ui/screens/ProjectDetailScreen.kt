package com.novaforge.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.novaforge.app.model.ProjectSpec
import com.novaforge.app.ui.theme.*

@Composable
fun ProjectDetailScreen(
    project: ProjectSpec,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NovaBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = NovaGold)
        ) {
            Text("Back", color = androidx.compose.ui.graphics.Color.Black)
        }

        Text(
            text = project.name,
            style = MaterialTheme.typography.headlineMedium,
            color = NovaText
        )

        Text(
            text = project.description.ifBlank { "No description" },
            color = NovaText.copy(alpha = 0.75f)
        )

        Text(
            text = "Technology: ${project.technology}",
            color = NovaGold
        )

        Text(
            text = "Status: ${project.status}",
            color = NovaText.copy(alpha = 0.8f)
        )
    }
}