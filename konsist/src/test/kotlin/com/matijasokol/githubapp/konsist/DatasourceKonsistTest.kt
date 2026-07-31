package com.matijasokol.githubapp.konsist

import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.jupiter.api.Test

class DatasourceKonsistTest {

    @Test
    fun `datasource implementations implement matching domain contracts`() {
        datasourceClasses()
            .filter { datasourceContractImplementations.containsKey(it.name) }
            .assertTrue { implementation ->
                implementation.hasParent { parent ->
                    parent.name == datasourceContractImplementations.getValue(implementation.name)
                }
            }
    }

    @Test
    fun `datasource classes do not import UI packages`() {
        datasourceFiles()
            .assertFalse { file ->
                file.imports.any { import ->
                    forbiddenDatasourceImports.any { forbiddenImport ->
                        import.name.startsWith("$forbiddenImport.")
                    }
                }
            }
    }

    @Test
    fun `datasource DTOs use Dto suffix and Serializable annotation`() {
        datasourceClasses()
            .filter { it.resideInPackage("..network.model") }
            .assertTrue { dto ->
                dto.name.endsWith("Dto") && dto.hasAnnotationWithName("Serializable")
            }
    }
}

private val datasourceContractImplementations = mapOf(
    "RepoCacheImpl" to "RepoCache",
    "RepoServiceImpl" to "RepoService",
)

private val forbiddenDatasourceImports = listOf(
    "com.matijasokol.coreui",
    "com.matijasokol.repo.list",
    "com.matijasokol.repo.detail",
)
