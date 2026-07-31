package com.matijasokol.githubapp.konsist

import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.jupiter.api.Test

class ArchitectureKonsistTest {

    @Test
    fun `package layers have correct dependencies`() {
        productionScope.assertArchitecture {
            val core = Layer("Core", "com.matijasokol.core..")
            val coreUi = Layer("Core UI", "com.matijasokol.coreui..")
            val domain = Layer("Repo Domain", "com.matijasokol.repo.domain..")
            val datasource = Layer("Repo Datasource", "com.matijasokol.repo.datasource..")
            val list = Layer("Repo List", "com.matijasokol.repo.list..")
            val detail = Layer("Repo Detail", "com.matijasokol.repo.detail..")
            val app = Layer("App", "com.matijasokol.githubapp..")

            core.dependsOnNothing()
            coreUi.dependsOn(core)
            domain.dependsOn(core)
            datasource.dependsOn(core, domain)
            list.dependsOn(core, coreUi, domain)
            detail.dependsOn(core, coreUi, domain)
            app.dependsOn(core, coreUi, domain, datasource, list, detail)
        }
    }

    @Test
    fun `domain module does not depend on Android datasource or UI packages`() {
        productionScope
            .files
            .filter { it.path.normalizedPath().contains("/repo/domain/src/main/") }
            .assertFalse { file ->
                file.imports.any { import ->
                    forbiddenDomainImports.any { forbiddenImport ->
                        import.name.startsWith("$forbiddenImport.")
                    }
                }
            }
    }

    @Test
    fun `compose functions live only in UI modules`() {
        productionScope
            .functions()
            .filter { it.hasAnnotationWithName("Composable") }
            .assertTrue { function ->
                composeModulePathSegments.any { pathSegment ->
                    function.path.normalizedPath().contains(pathSegment)
                }
            }
    }

    @Test
    fun `wildcard imports are not used`() {
        productionScope
            .imports
            .assertFalse { it.isWildcard }
    }

    @Test
    fun `package names are lowercase`() {
        productionScope
            .packages
            .assertTrue { it.name.matches(LOWERCASE_PACKAGE_REGEX) }
    }

    @Test
    fun `package declarations match file paths`() {
        productionScope
            .packages
            .assertTrue { it.hasMatchingPath }
    }
}

private val forbiddenDomainImports = listOf(
    "android",
    "androidx",
    "com.matijasokol.coreui",
    "com.matijasokol.githubapp",
    "com.matijasokol.repo.datasource",
    "com.matijasokol.repo.datasourcetest",
    "com.matijasokol.repo.detail",
    "com.matijasokol.repo.list",
)

private val composeModulePathSegments = listOf(
    "/app/src/",
    "/core-ui/src/",
    "/repo/list/src/",
    "/repo/detail/src/",
)

private val LOWERCASE_PACKAGE_REGEX = "^[a-z.]+$".toRegex()
