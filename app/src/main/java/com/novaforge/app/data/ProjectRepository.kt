package com.novaforge.app.data

import android.content.Context
import com.novaforge.app.model.ProjectSpec
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ProjectRepository(context: Context) {

    private val prefs = context.getSharedPreferences(
        "nova_projects",
        Context.MODE_PRIVATE
    )

    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun getProjects(): List<ProjectSpec> {
        val raw = prefs.getString(KEY_PROJECTS, null) ?: return emptyList()

        return try {
            json.decodeFromString<List<ProjectSpec>>(raw)
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveProjects(projects: List<ProjectSpec>) {
        prefs.edit()
            .putString(KEY_PROJECTS, json.encodeToString(projects))
            .apply()
    }

    fun addProject(project: ProjectSpec) {
        saveProjects(getProjects() + project)
    }

    fun deleteProject(id: String) {
        saveProjects(getProjects().filterNot { it.id == id })
    }

    companion object {
        private const val KEY_PROJECTS = "projects"
    }
}
