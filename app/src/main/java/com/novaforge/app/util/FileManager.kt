package com.novaforge.app.util

import java.io.File

class FileManager(private val projectPath: String) {
    private val rootDir = File(projectPath)

    fun readFile(path: String): String? {
        val file = File(rootDir, path)
        return if (file.exists() && file.isFile) {
            try {
                file.readText()
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    fun writeFile(path: String, content: String): Boolean {
        val file = File(rootDir, path)
        return try {
            file.parentFile?.mkdirs()
            file.writeText(content)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun createFile(path: String, content: String = ""): Boolean {
        return writeFile(path, content)
    }

    fun deleteFile(path: String): Boolean {
        val file = File(rootDir, path)
        return try {
            file.deleteRecursively()
        } catch (e: Exception) {
            false
        }
    }

    fun listFiles(path: String = ""): List<FileInfo> {
        val dir = File(rootDir, path)
        return if (dir.exists() && dir.isDirectory) {
            dir.listFiles()?.sortedWith(compareBy({ !it.isDirectory }, { it.name }))?.map { file ->
                FileInfo(
                    name = file.name,
                    path = if (path.isEmpty()) file.name else "$path/${file.name}",
                    isDirectory = file.isDirectory,
                    size = file.length(),
                    lastModified = file.lastModified()
                )
            } ?: emptyList()
        } else {
            emptyList()
        }
    }

    fun createDirectory(path: String): Boolean {
        return File(rootDir, path).mkdirs()
    }

    fun searchFiles(query: String): List<String> {
        val matches = mutableListOf<String>()
        rootDir.walkTopDown().forEach { file ->
            if (file.isFile && file.name.contains(query, ignoreCase = true)) {
                matches.add(file.relativeTo(rootDir).path)
            }
        }
        return matches
    }
}

data class FileInfo(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val size: Long,
    val lastModified: Long
)
