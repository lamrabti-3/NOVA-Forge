package com.novaforge.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novaforge.app.data.ProjectRepository
import com.novaforge.app.ui.theme.NovaBackground
import com.novaforge.app.ui.theme.NovaGold
import com.novaforge.app.ui.theme.NovaPanel
import com.novaforge.app.ui.theme.NovaText

sealed class NavigationScreen {
    object Home : NavigationScreen()
    object Projects : NavigationScreen()
    object Settings : NavigationScreen()
}

@Composable
fun NovaForgeApp() {
    val context = LocalContext.current
    val projectRepository = remember { ProjectRepository(context) }
    
    var currentScreen by remember { mutableStateOf<NavigationScreen>(NavigationScreen.Home) }
    var prompt by remember { mutableStateOf("") }
    var buildState by remember { mutableStateOf("Ready") }
    var projects by remember { mutableStateOf(projectRepository.getProjects()) }

    Scaffold(
        containerColor = NovaBackground,
        topBar = {
            SmallTopAppBar(
                title = { Text("NOVA Forge", color = NovaText) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = NovaBackground
                )
            )
        }
    ) { padding ->
        when (currentScreen) {
            NavigationScreen.Home -> {
                HomeScreen(
                    modifier = Modifier.padding(padding),
                    prompt = prompt,
                    onPromptChange = { prompt = it },
                    buildState = buildState,
                    onBuildClick = { 
                        buildState = "Building..."
                        projects = projectRepository.getProjects()
                    },
                    onNavigation = { currentScreen = it }
                )
            }
            NavigationScreen.Projects -> {
                ProjectsHomeScreen(
                    modifier = Modifier.padding(padding),
                    projects = projects,
                    onProjectCreated = { projects = projectRepository.getProjects() },
                    onBack = { currentScreen = NavigationScreen.Home }
                )
            }
            NavigationScreen.Settings -> {
                SettingsHomeScreen(
                    modifier = Modifier.padding(padding),
                    onBack = { currentScreen = NavigationScreen.Home }
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    prompt: String,
    onPromptChange: (String) -> Unit,
    buildState: String,
    onBuildClick: () -> Unit,
    onNavigation: (NavigationScreen) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "NOVA Forge",
            style = MaterialTheme.typography.headlineLarge,
            color = NovaText
        )

        Text(
            text = "Describe it. Build it. Run it.",
            style = MaterialTheme.typography.bodyLarge,
            color = NovaGold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NovaPanel),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "[ ماذا تريد أن أبني لك؟ ]",
                    style = MaterialTheme.typography.bodyLarge,
                    color = NovaText
                )

                BasicTextField(
                    value = prompt,
                    onValueChange = onPromptChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(
                            color = Color(0xFF0E1820),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = NovaGold.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .padding(16.dp)
                ) { innerTextField ->
                    if (prompt.isEmpty()) {
                        Text("اكتب فكرة المشروع...", color = NovaText.copy(alpha = 0.55f))
                    }
                    innerTextField()
                }

                Button(
                    onClick = onBuildClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NovaGold,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Build", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }

                Text(
                    text = "Status: $buildState",
                    color = NovaText.copy(alpha = 0.8f)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionButton("New Project", Icons.Filled.Add) { 
                onNavigation(NavigationScreen.Projects)
            }
            QuickActionButton("Projects", Icons.Filled.Folder) { 
                onNavigation(NavigationScreen.Projects)
            }
            QuickActionButton("Terminal", Icons.Filled.Terminal) { }
            QuickActionButton("GitHub", Icons.Filled.Code) { }
            QuickActionButton("Settings", Icons.Filled.Settings) { 
                onNavigation(NavigationScreen.Settings)
            }
        }

        AgentStageCard(title = "Understanding", detail = "فهم الطلب...", active = true)
        AgentStageCard(title = "Planning", detail = "إنشاء الخطة...", active = false)
        AgentStageCard(title = "Generating", detail = "إنشاء الملفات...", active = false)
        AgentStageCard(title = "Testing", detail = "الاختبار...", active = false)
    }
}

@Composable
fun ProjectsHomeScreen(
    modifier: Modifier = Modifier,
    projects: List<Any>,
    onProjectCreated: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Projects",
                style = MaterialTheme.typography.headlineMedium,
                color = NovaText
            )
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = NovaGold),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Back", color = Color.Black)
            }
        }
        Text("Projects screen coming soon...", color = NovaText)
    }
}

@Composable
fun SettingsHomeScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                color = NovaText
            )
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = NovaGold),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Back", color = Color.Black)
            }
        }
        Text("Settings screen coming soon...", color = NovaText)
    }
}

@Composable
fun QuickActionButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .weight(1f)
            .clickable { onClick() },
        color = NovaPanel,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = NovaGold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = label, color = NovaText, fontSize = 11.sp)
        }
    }
}

@Composable
fun AgentStageCard(title: String, detail: String, active: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (active) Color(0xFF17222A) else NovaPanel
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        if (active) NovaGold else Color.Gray,
                        shape = RoundedCornerShape(50)
                    )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, color = NovaText, style = MaterialTheme.typography.titleMedium)
                Text(detail, color = NovaText.copy(alpha = 0.72f), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
