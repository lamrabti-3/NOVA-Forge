package com.novaforge.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.novaforge.app.data.ProjectRepository
import com.novaforge.app.ui.screens.HomeScreen
import com.novaforge.app.ui.screens.ProjectsHomeScreen
import com.novaforge.app.ui.screens.SettingsHomeScreen
import com.novaforge.app.ui.theme.NovaBackground

sealed class NavigationScreen {
    data object Home : NavigationScreen()
    data object Projects : NavigationScreen()
    data object Settings : NavigationScreen()
}

@Composable
fun NovaForgeApp() {
    val context = LocalContext.current
    val projectRepository = remember {
        ProjectRepository(context)
    }

    var currentScreen by remember {
        mutableStateOf<NavigationScreen>(NavigationScreen.Home)
    }

    var prompt by remember {
        mutableStateOf("")
    }

    var buildState by remember {
        mutableStateOf("Ready")
    }

    var projects by remember {
        mutableStateOf(projectRepository.getProjects())
    }

    Scaffold(
        containerColor = NovaBackground
    ) { padding ->

        when (val screen = currentScreen) {

            NavigationScreen.Home -> {
                HomeScreen(
                    prompt = prompt,
                    onPromptChange = { prompt = it },
                    buildState = buildState,
                    onBuildClick = {
                        buildState = if (prompt.isBlank()) {
                            "Enter a project idea"
                        } else {
                            "Building..."
                        }

                        projects = projectRepository.getProjects()
                    },
                    onOpenProjects = {
                        currentScreen = NavigationScreen.Projects
                    },
                    onOpenSettings = {
                        currentScreen = NavigationScreen.Settings
                    }
                )
            }

            NavigationScreen.Projects -> {
                ProjectsHomeScreen(
                    modifier = Modifier.padding(padding),
                    projects = projects,
                    onProjectCreated = {
                        projects = projectRepository.getProjects()
                    },
                    onBack = {
                        currentScreen = NavigationScreen.Home
                    }
                )
            }

            NavigationScreen.Settings -> {
                SettingsHomeScreen(
                    modifier = Modifier.padding(padding),
                    onBack = {
                        currentScreen = NavigationScreen.Home
                    }
                )
            }
        }
    }
}
