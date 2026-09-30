package com.novaforge.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.novaforge.app.ui.theme.*

@Composable
fun SettingsHomeScreen(
    modifier: Modifier = Modifier,
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
                "Settings",
                style = MaterialTheme.typography.headlineMedium,
                color = NovaText
            )

            TextButton(onClick = onBack) {
                Text("Back", color = NovaGold)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = NovaPanel
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "NOVA Forge",
                    color = NovaText,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    "AI-powered Android development environment.",
                    color = NovaMuted
                )

                HorizontalDivider()

                Text(
                    "Version 1.0.0",
                    color = NovaGold
                )
            }
        }
    }
}
