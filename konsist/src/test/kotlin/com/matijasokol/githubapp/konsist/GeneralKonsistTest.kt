package com.matijasokol.githubapp.konsist

import com.lemonappdev.konsist.api.verify.assertFalse
import org.junit.jupiter.api.Test

class GeneralKonsistTest {

    @Test
    fun `no property should have 'm' prefix`() {
        projectScope
            .properties(includeNested = true)
            .assertFalse { property -> property.name.matches(HUNGARIAN_FIELD_REGEX) }
    }

    @Test
    fun `no class should use Android util logging`() {
        projectScope
            .files
            .assertFalse(
                additionalMessage = "android.util.Log is not allowed. " +
                    "Log through AppLogger, which is backed by Timber.",
            ) { file -> file.hasImportWithName(ANDROID_LOG_IMPORT) }
    }

    @Test
    fun `Timber is used only in app logging setup`() {
        projectScope
            .files
            .filterNot { file -> file.path.normalizedPath().contains(TIMBER_ALLOWED_PATH) }
            .assertFalse(
                additionalMessage = "Timber is allowed only in app's logging setup. " +
                    "Inject AppLogger and log through it instead.",
            ) { file -> file.imports.any { import -> import.name.startsWith(TIMBER_PACKAGE) } }
    }

    @Test
    fun `BuildConfig is read only in BuildConfigModule`() {
        productionScope
            .files
            .filterNot { file -> file.path.normalizedPath().endsWith(BUILD_CONFIG_ALLOWED_FILE) }
            .assertFalse(
                additionalMessage = "BuildConfig is read only in app's di/BuildConfigModule. " +
                    "Inject AppBuildConfig instead.",
            ) { file -> file.text.contains(BUILD_CONFIG_USAGE_REGEX) }
    }

    @Test
    fun `kotlinx Dispatchers are imported only in DispatchersModule`() {
        productionScope
            .files
            .filterNot { file -> file.path.normalizedPath().endsWith(DISPATCHERS_ALLOWED_FILE) }
            .assertFalse(
                additionalMessage = "kotlinx.coroutines.Dispatchers is not used directly; " +
                    "app's di/DispatchersModule provides AppDispatchers (default, io, main, mainImmediate). " +
                    "Inject AppDispatchers (in composables, read LocalAppDispatchers.current) instead.",
            ) { file ->
                file.imports.any { import ->
                    import.name == DISPATCHERS_IMPORT || import.name.startsWith("$DISPATCHERS_IMPORT.")
                }
            }
    }

    @Test
    fun `no empty files allowed`() {
        projectScope
            .files
            .assertFalse { file -> file.text.isBlank() }
    }
}

private val HUNGARIAN_FIELD_REGEX = "^m[A-Z].*".toRegex()
private const val ANDROID_LOG_IMPORT = "android.util.Log"
private const val TIMBER_PACKAGE = "timber."
private const val TIMBER_ALLOWED_PATH = "/app/src/main/java/com/matijasokol/githubapp/logging/"
private val BUILD_CONFIG_USAGE_REGEX = "\\bBuildConfig\\.".toRegex()
private const val BUILD_CONFIG_ALLOWED_FILE = "/app/src/main/java/com/matijasokol/githubapp/di/BuildConfigModule.kt"
private const val DISPATCHERS_IMPORT = "kotlinx.coroutines.Dispatchers"
private const val DISPATCHERS_ALLOWED_FILE = "/app/src/main/java/com/matijasokol/githubapp/di/DispatchersModule.kt"
