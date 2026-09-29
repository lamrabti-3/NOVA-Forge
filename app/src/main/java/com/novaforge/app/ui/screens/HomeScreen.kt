package com.novaforge.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novaforge.app.ui.theme.*

@Composable
fun HomeScreen(
    prompt: String,
    onPromptChange: (String) -> Unit,
    buildState: String,
    onBuildClick: () -> Unit,
    onOpenProjects: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Scaffold(
        containerColor = NovaBackground,
        topBar = {
            SmallTopAppBar(
                title = { Text("NOVA Forge", color = NovaText) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = NovaBackground)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("NOVA Forge", style = MaterialTheme.typography.headlineLarge, color = NovaText)
            Text("Describe it. Build it. Run it.", style = MaterialTheme.typography.bodyLarge, color = NovaGold)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NovaPanel),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("[ ماذا تريد أن أبني لك؟ ]", style = MaterialTheme.typography.bodyLarge, color = NovaText)

                    BasicTextField(
                        value = prompt,
                        onValueChange = onPromptChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(Color(0xFF0E1820), RoundedCornerShape(18.dp))
                            .border(1.dp, NovaGold.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) { innerTextField ->
                        if (prompt.isEmpty()) {
                            Text("اكتب فكرة المشروع...", color = NovaText.copy(alpha = 0.55f))
                        }
                        innerTextField()
                    }

                    Button(
                        onClick = onBuildClick,
                        colors = ButtonDefaults.buttonColors(containerColor = NovaGold, contentColor = Color.Black),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Build", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }

                    Text("Status: $buildState", color = NovaText.copy(alpha = 0.8f))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionButton("New Project", Icons.Filled.Add) { }
                QuickActionButton("Projects", Icons.Filled.Folder) { onOpenProjects() }
                QuickActionButton("Terminal", Icons.Filled.Terminal) { }
                QuickActionButton("GitHub", Icons.Filled.Code) { }
                QuickActionButton("Settings", Icons.Filled.Settings) { onOpenSettings() }
            }

            AgentStageCard("Understanding", "فهم الطلب...", true)
            AgentStageCard("Planning", "إنشاء الخطة...", false)
            AgentStageCard("Generating", "إنشاء الملفات...", false)
            AgentStageCard("Testing", "تشغيل الاختبارات...", false)
        }
    }
}

@Composable
fun QuickActionButton(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
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
            Text(label, color = NovaText, fontSize = 11.sp)
        }
    }
}

@Composable
fun AgentStageCard(title: String, detail: String, active: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (active) Color(0xFF17222A) else NovaPanel),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(if (active) NovaGold else Color.Gray, RoundedCornerShape(50))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, color = NovaText, style = MaterialTheme.typography.titleMedium)
                Text(detail, color = NovaText.copy(alpha = 0.72f), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}