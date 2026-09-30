package com.novaforge.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.novaforge.app.model.ProjectSpec
import com.novaforge.app.ui.theme.*

@Composable
fun ProjectsHomeScreen(
    modifier: Modifier = Modifier,
    projects: List<ProjectSpec>,
    onProjectCreated: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaBackground)
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Projects",
                style = MaterialTheme.typography.headlineMedium,
                color = NovaText
            )

            TextButton(onClick = onBack) {
                Text("Back", color = NovaGold)
            }
        }

        if (projects.isEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = NovaPanel
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        "No projects yet",
                        color = NovaText,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "Build your first project from the NOVA home screen.",
                        color = NovaMuted
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = projects,
                    key = { it.id }
                ) { project ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = NovaPanel
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {
                            Text(
                                project.name,
                                color = NovaText,
                                style = MaterialTheme.typography.titleLarge
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                project.description,
                                color = NovaMuted
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                "${project.technology} • ${project.status}",
                                color = NovaGold
                            )
                        }
                    }
                }
            }
        }
    }
}
