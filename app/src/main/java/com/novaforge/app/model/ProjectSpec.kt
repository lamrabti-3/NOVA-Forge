package com.novaforge.app.model

import kotlinx.serialization.Serializable

@Serializable
data class ProjectSpec(
    val id: String,
    val name: String,
    val description: String = "",
    val technology: String = "Android",
    val status: String = "Ready"
)
