package com.matijasokol.githubapp.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.KoClassDeclaration
import com.lemonappdev.konsist.api.declaration.KoFileDeclaration
import com.lemonappdev.konsist.api.declaration.KoInterfaceDeclaration

/**
 * Production sources only: every source set whose name doesn't contain "test" (main, free, paid, ...).
 */
internal val productionScope = Konsist.scopeFromProduction()

/**
 * All Kotlin sources, production and test (incl. androidTest); only build output (build/, .gradle/) is skipped.
 */
internal val projectScope = Konsist.scopeFromProject()

internal fun productionClasses(): List<KoClassDeclaration> = productionScope
    .classes(includeNested = false, includeLocal = false)

internal fun productionInterfaces(): List<KoInterfaceDeclaration> = productionScope
    .interfaces(includeNested = false)

internal fun viewModelClasses(): List<KoClassDeclaration> = productionClasses()
    .filter { clazz ->
        clazz.hasParent { parent -> parent.name == "ViewModel" } &&
            clazz.containingFile.hasImportWithName(ANDROIDX_VIEW_MODEL_IMPORT)
    }

internal fun mapperClasses(): List<KoClassDeclaration> = productionClasses()
    .filter { clazz -> clazz.name.endsWith(MAPPER_SUFFIX) }

internal fun useCaseClasses(): List<KoClassDeclaration> = productionClasses()
    .filter { it.path.normalizedPath().contains(DOMAIN_USECASE_PATH) }

internal fun datasourceFiles(): List<KoFileDeclaration> = productionScope
    .files
    .filter { it.path.normalizedPath().contains(DATASOURCE_MAIN_PATH) }

internal fun datasourceClasses(): List<KoClassDeclaration> = productionClasses()
    .filter { it.path.normalizedPath().contains(DATASOURCE_MAIN_PATH) }

internal fun String.normalizedPath(): String = replace('\\', '/')

private const val DATASOURCE_MAIN_PATH = "/repo/datasource/src/main/"
private const val DOMAIN_USECASE_PATH = "/repo/domain/src/main/java/com/matijasokol/repo/domain/usecase/"
private const val ANDROIDX_VIEW_MODEL_IMPORT = "androidx.lifecycle.ViewModel"
private const val MAPPER_SUFFIX = "Mapper"
