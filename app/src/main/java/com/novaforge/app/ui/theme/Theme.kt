package com.novaforge.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NovaColors = darkColorScheme(
    primary = NovaGold,
    onPrimary = NovaBackground,
    background = NovaBackground,
    onBackground = NovaText,
    surface = NovaPanel,
    onSurface = NovaText
)

@Composable
fun NovaForgeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NovaColors,
        content = content
    )
}
