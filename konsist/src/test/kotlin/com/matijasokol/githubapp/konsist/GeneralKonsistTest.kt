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
            .assertFalse { file -> file.hasImportWithName(ANDROID_LOG_IMPORT) }
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
