package com.matijasokol.githubapp.konsist

import com.lemonappdev.konsist.api.verify.assertFalse
import org.junit.jupiter.api.Test

class TestKonsistTest {

    @Test
    fun `unit tests don't use JUnit4 Test annotation`() {
        // Checks only unit-test source sets (src/test, src/testFree, src/testPaid, src/testFixtures, ...).
        // Instrumented tests in src/androidTest are excluded because they use JUnit 4 on purpose.
        projectScope
            .functions()
            .filter { function -> function.path.normalizedPath().contains(UNIT_TEST_SOURCE_SET_REGEX) }
            .assertFalse { function ->
                function.hasAnnotation { annotation ->
                    annotation.fullyQualifiedName == JUNIT4_TEST_ANNOTATION ||
                        annotation.text.startsWith("@$JUNIT4_TEST_ANNOTATION")
                }
            }
    }
}

private val UNIT_TEST_SOURCE_SET_REGEX = "/src/test[A-Za-z]*/".toRegex()
private const val JUNIT4_TEST_ANNOTATION = "org.junit.Test"
