package com.matijasokol.githubapp.konsist

import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Test

class MviKonsistTest {

    @Test
    fun `feature packages with view models declare MVI companions`() {
        featurePackagesWithViewModels().forEach { packageName ->
            val packageFiles = productionScope.files.filter { it.packagee?.name == packageName }

            packageFiles
                .any { file ->
                    file.hasClassOrInterface(
                        includeNested = false,
                        includeLocal = false,
                    ) { it.name.endsWith("State") }
                }.shouldBeTrue()

            packageFiles
                .any { file ->
                    file.hasClassOrInterface(includeNested = false) { it.name.endsWith("Event") }
                }.shouldBeTrue()

            packageFiles
                .any { file ->
                    file.hasClassOrInterface(includeNested = false) { it.name.endsWith("Action") }
                }.shouldBeTrue()

            packageFiles
                .any { file ->
                    file.hasClass(includeNested = false, includeLocal = false) { it.name.endsWith("UiMapper") }
                }.shouldBeTrue()
        }
    }

    private fun featurePackagesWithViewModels(): List<String> = viewModelClasses()
        .filter { it.resideInPackage("com.matijasokol.repo..") }
        .mapNotNull { it.packagee?.name }
        .distinct()
}
