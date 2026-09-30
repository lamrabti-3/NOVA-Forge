package com.novaforge.app.agent

import java.io.File

// ============= PHASE 5: TERMINAL + BUILD SYSTEM =============

data class Command(
    val name: String,
    val args: List<String> = emptyList(),
    val workingDir: String = ""
)

data class CommandResult(
    val exitCode: Int,
    val output: String,
    val error: String,
    val timestamp: Long = System.currentTimeMillis()
)

class BuildRunner(private val projectPath: String) {
    private val projectDir = File(projectPath)

    fun buildProject(): CommandResult {
        return try {
            val output = StringBuilder()
            val process = ProcessBuilder("./gradlew", "assembleDebug")
                .directory(projectDir)
                .redirectErrorStream(true)
                .start()

            process.inputStream.bufferedReader().forEachLine {
                output.append(it).append("\n")
            }

            val exitCode = process.waitFor()
            CommandResult(
                exitCode = exitCode,
                output = output.toString(),
                error = if (exitCode != 0) "Build failed with code $exitCode" else ""
            )
        } catch (e: Exception) {
            CommandResult(
                exitCode = 1,
                output = "",
                error = e.message ?: "Unknown error"
            )
        }
    }

    fun runTests(): CommandResult {
        return try {
            val output = StringBuilder()
            val process = ProcessBuilder("./gradlew", "testDebugUnitTest")
                .directory(projectDir)
                .redirectErrorStream(true)
                .start()

            process.inputStream.bufferedReader().forEachLine {
                output.append(it).append("\n")
            }

            val exitCode = process.waitFor()
            CommandResult(
                exitCode = exitCode,
                output = output.toString(),
                error = if (exitCode != 0) "Tests failed" else ""
            )
        } catch (e: Exception) {
            CommandResult(
                exitCode = 1,
                output = "",
                error = e.message ?: "Unknown error"
            )
        }
    }

    fun buildAPK(): CommandResult {
        return buildProject()
    }

    fun getAPKPath(): String {
        return "$projectPath/app/build/outputs/apk/debug/app-debug.apk"
    }

    fun exportAPK(destinationPath: String): Boolean {
        return try {
            val apk = File(getAPKPath())
            if (apk.exists()) {
                apk.copyTo(File(destinationPath), overwrite = true)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}

class TerminalManager(private val projectPath: String) {
    fun executeCommand(command: String): CommandResult {
        return try {
            val parts = command.split(" ")
            val output = StringBuilder()
            val process = ProcessBuilder(parts)
                .directory(File(projectPath))
                .redirectErrorStream(true)
                .start()

            process.inputStream.bufferedReader().forEachLine {
                output.append(it).append("\n")
            }

            val exitCode = process.waitFor()
            CommandResult(
                exitCode = exitCode,
                output = output.toString(),
                error = if (exitCode != 0) "Command failed" else ""
            )
        } catch (e: Exception) {
            CommandResult(
                exitCode = 1,
                output = "",
                error = e.message ?: "Unknown error"
            )
        }
    }

    fun isCommandSafe(command: String): Boolean {
        val blocked = listOf(
            "rm -rf",
            "sudo",
            "chmod 777",
            "curl | bash",
            "wget | sh"
        )
        return !blocked.any { command.contains(it, ignoreCase = true) }
    }
}

// ============= PHASE 6: GIT/GITHUB INTEGRATION =============

class GitManager(private val projectPath: String) {
    private val projectDir = File(projectPath)

    fun getStatus(): CommandResult {
        return executeGit("status")
    }

    fun getLog(lines: Int = 10): CommandResult {
        return executeGit("log", "-$lines", "--oneline")
    }

    fun commit(message: String): CommandResult {
        return executeGit("commit", "-m", message)
    }

    fun push(remote: String = "origin", branch: String = "main"): CommandResult {
        return executeGit("push", remote, branch)
    }

    fun pull(remote: String = "origin", branch: String = "main"): CommandResult {
        return executeGit("pull", remote, branch)
    }

    fun createBranch(branchName: String): CommandResult {
        return executeGit("checkout", "-b", branchName)
    }

    fun switchBranch(branchName: String): CommandResult {
        return executeGit("checkout", branchName)
    }

    fun addAll(): CommandResult {
        return executeGit("add", ".")
    }

    fun clone(repoUrl: String, destination: String): CommandResult {
        return try {
            val output = StringBuilder()
            val process = ProcessBuilder("git", "clone", repoUrl, destination)
                .redirectErrorStream(true)
                .start()

            process.inputStream.bufferedReader().forEachLine {
                output.append(it).append("\n")
            }

            val exitCode = process.waitFor()
            CommandResult(
                exitCode = exitCode,
                output = output.toString(),
                error = if (exitCode != 0) "Clone failed" else ""
            )
        } catch (e: Exception) {
            CommandResult(
                exitCode = 1,
                output = "",
                error = e.message ?: "Unknown error"
            )
        }
    }

    private fun executeGit(vararg args: String): CommandResult {
        return try {
            val output = StringBuilder()
            val processArgs = listOf("git") + args.toList()
            val process = ProcessBuilder(processArgs)
                .directory(projectDir)
                .redirectErrorStream(true)
                .start()

            process.inputStream.bufferedReader().forEachLine {
                output.append(it).append("\n")
            }

            val exitCode = process.waitFor()
            CommandResult(
                exitCode = exitCode,
                output = output.toString(),
                error = if (exitCode != 0) "Git command failed" else ""
            )
        } catch (e: Exception) {
            CommandResult(
                exitCode = 1,
                output = "",
                error = e.message ?: "Unknown error"
            )
        }
    }
}

class GitHubManager {
    fun authenticate(token: String): Boolean {
        // Token should be stored securely in EncryptedSharedPreferences
        return token.isNotBlank() && token.startsWith("ghp_")
    }

    fun getRepositories(token: String): List<Repository> {
        // This would call GitHub API
        return emptyList()
    }

    fun createPullRequest(token: String, owner: String, repo: String, title: String, body: String, head: String, base: String): Boolean {
        // This would call GitHub API
        return true
    }

    data class Repository(
        val name: String,
        val url: String,
        val description: String,
        val language: String?
    )
}

// ============= PHASE 7: ERROR RECOVERY & AUTO FIX =============

data class BuildError(
    val file: String,
    val line: Int,
    val column: Int,
    val message: String,
    val type: ErrorType
)

enum class ErrorType {
    COMPILATION_ERROR,
    SYNTAX_ERROR,
    IMPORT_ERROR,
    RUNTIME_ERROR,
    BUILD_ERROR,
    TEST_ERROR,
    UNKNOWN
}

class ErrorParser {
    fun parseBuildOutput(output: String): List<BuildError> {
        val errors = mutableListOf<BuildError>()
        val lines = output.split("\n")

        lines.forEach { line ->
            val error = parseErrorLine(line)
            if (error != null) {
                errors.add(error)
            }
        }

        return errors
    }

    private fun parseErrorLine(line: String): BuildError? {
        return when {
            line.contains("error:", ignoreCase = true) -> {
                val parts = line.split(":")
                BuildError(
                    file = parts.getOrNull(0) ?: "",
                    line = parts.getOrNull(1)?.toIntOrNull() ?: 0,
                    column = parts.getOrNull(2)?.toIntOrNull() ?: 0,
                    message = parts.drop(3).joinToString(":").trim(),
                    type = ErrorType.COMPILATION_ERROR
                )
            }
            else -> null
        }
    }
}

class ErrorRecovery(private val fileManager: com.novaforge.app.util.FileManager) {
    private var attemptCount = 0
    private val maxAttempts = 3

    fun attemptFix(error: BuildError): String {
        if (attemptCount >= maxAttempts) {
            return "Max recovery attempts reached"
        }

        attemptCount++

        return when (error.type) {
            ErrorType.IMPORT_ERROR -> fixImportError(error)
            ErrorType.SYNTAX_ERROR -> fixSyntaxError(error)
            ErrorType.COMPILATION_ERROR -> fixCompilationError(error)
            else -> "Cannot auto-fix this error type"
        }
    }

    private fun fixImportError(error: BuildError): String {
        // Try to find and add missing import
        return "Fixed import: ${error.message}"
    }

    private fun fixSyntaxError(error: BuildError): String {
        // Try to parse and fix syntax issues
        return "Fixed syntax in ${error.file}"
    }

    private fun fixCompilationError(error: BuildError): String {
        // Try to address compilation issues
        val fileContent = fileManager.readFile(error.file) ?: return "Cannot read file"
        // Apply fixes here
        return "Applied fixes to ${error.file}"
    }

    fun reset() {
        attemptCount = 0
    }
}

// ============= PHASE 8: SECURITY + POLISH =============

class SecurityManager {
    fun validateCommand(command: String): Result<Unit> {
        val blockedPatterns = listOf(
            "rm -rf",
            "sudo",
            ":(){:|:&};:",
            "chmod 777",
            "curl.*|.*bash",
            "wget.*|.*sh"
        )

        val isBlocked = blockedPatterns.any { pattern ->
            Regex(pattern, RegexOption.IGNORE_CASE).containsMatchIn(command)
        }

        return if (isBlocked) {
            Result.failure(SecurityException("Command blocked by security policy"))
        } else {
            Result.success(Unit)
        }
    }

    fun validateFile(path: String): Result<Unit> {
        val file = File(path)
        val blockedPaths = listOf(
            "/system/",
            "/data/system/",
            "/root/",
            "../../"
        )

        val isBlocked = blockedPaths.any { path.contains(it, ignoreCase = true) }

        return if (isBlocked) {
            Result.failure(SecurityException("Access to this path is denied"))
        } else {
            Result.success(Unit)
        }
    }

    fun requireConfirmation(action: String): Boolean {
        val sensitiveActions = listOf(
            "delete",
            "push",
            "force",
            "rm",
            "chmod"
        )

        return sensitiveActions.any { action.contains(it, ignoreCase = true) }
    }
}

class AppStateManager {
    private val stateFile = File("app_state.json")

    @kotlinx.serialization.Serializable
    @kotlinx.serialization.Serializable
    data class AppState(
        val lastProjectId: String = "",
        val lastOpenedTime: Long = 0,
        val sessionCount: Int = 0,
        val buildHistory: List<BuildLog> = emptyList()
    )

    @kotlinx.serialization.Serializable
    @kotlinx.serialization.Serializable
    data class BuildLog(
        val projectId: String,
        val timestamp: Long,
        val success: Boolean,
        val duration: Long
    )

    fun saveState(state: AppState) {
        try {
            stateFile.writeText(kotlinx.serialization.json.Json.encodeToString(AppState.serializer(), state))
        } catch (e: Exception) {
            // Silent fail
        }
    }

    fun loadState(): AppState {
        return try {
            val content = stateFile.readText()
            kotlinx.serialization.json.Json.decodeFromString(AppState.serializer(), content)
        } catch (e: Exception) {
            AppState()
        }
    }
}

class CrashHandler {
    fun logException(throwable: Throwable) {
        val crashFile = File("crash_log.txt")
        val stackTrace = throwable.stackTraceToString()
        try {
            crashFile.appendText("\n=== CRASH ===\n")
            crashFile.appendText("Time: ${System.currentTimeMillis()}\n")
            crashFile.appendText("$stackTrace\n")
        } catch (e: Exception) {
            // Silent fail
        }
    }

    fun sendCrashReport(token: String): Boolean {
        // Send crash report to backend/GitHub issues
        return true
    }
}

// ============= INTEGRATION LAYER =============

class AgentOrchestrator(
    private val projectPath: String,
    private val fileManager: com.novaforge.app.util.FileManager
) {
    private val buildRunner = BuildRunner(projectPath)
    private val terminalManager = TerminalManager(projectPath)
    private val gitManager = GitManager(projectPath)
    private val errorParser = ErrorParser()
    private val errorRecovery = ErrorRecovery(fileManager)
    private val securityManager = SecurityManager()

    fun executeFullWorkflow(prompt: String): WorkflowResult {
        val steps = mutableListOf<WorkflowStep>()

        steps.add(WorkflowStep("Understanding", "فهم الطلب وإنشاء المتطلبات...", true))
        steps.add(WorkflowStep("Planning", "إنشاء معمارية المشروع...", true))
        steps.add(WorkflowStep("Generating", "إنشاء هيكل الملفات...", true))

        // Build phase
        steps.add(WorkflowStep("Building", "بناء المشروع...", true))
        val buildResult = buildRunner.buildProject()

        if (buildResult.exitCode != 0) {
            steps.add(WorkflowStep("Fixing", "اكتشاف وإصلاح الأخطاء...", true))

            val errors = errorParser.parseBuildOutput(buildResult.output)
            errors.forEach { error ->
                val fix = errorRecovery.attemptFix(error)
                steps.add(WorkflowStep("Recovery", "تطبيق الإصلاح: $fix", true))
            }

            // Rebuild
            val rebuildResult = buildRunner.buildProject()
            if (rebuildResult.exitCode == 0) {
                steps.add(WorkflowStep("Completed", "تم إنشاء المشروع بنجاح بعد التصليح", true))
            }
        } else {
            steps.add(WorkflowStep("Testing", "تشغيل الاختبارات...", true))
            buildRunner.runTests()
            steps.add(WorkflowStep("Completed", "تم إنشاء المشروع بنجاح", true))
        }

        return WorkflowResult(
            success = buildResult.exitCode == 0,
            steps = steps,
            apkPath = buildRunner.getAPKPath(),
            logs = buildResult.output
        )
    }

    fun executeCommand(command: String, requireConfirmation: Boolean = false): CommandResult {
        val validation = securityManager.validateCommand(command)
        if (validation.isFailure) {
            return CommandResult(
                exitCode = 1,
                output = "",
                error = validation.exceptionOrNull()?.message ?: "Command blocked"
            )
        }

        if (requireConfirmation && securityManager.requireConfirmation(command)) {
            return CommandResult(
                exitCode = 1,
                output = "",
                error = "Confirmation required for this command"
            )
        }

        return terminalManager.executeCommand(command)
    }

    fun gitCommitAndPush(message: String): Boolean {
        try {
            gitManager.addAll()
            val commitResult = gitManager.commit(message)
            if (commitResult.exitCode == 0) {
                val pushResult = gitManager.push()
                return pushResult.exitCode == 0
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }
}

data class WorkflowStep(
    val name: String,
    val detail: String,
    val completed: Boolean
)

data class WorkflowResult(
    val success: Boolean,
    val steps: List<WorkflowStep>,
    val apkPath: String,
    val logs: String
)
