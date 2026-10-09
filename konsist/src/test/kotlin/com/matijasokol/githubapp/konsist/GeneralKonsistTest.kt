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
