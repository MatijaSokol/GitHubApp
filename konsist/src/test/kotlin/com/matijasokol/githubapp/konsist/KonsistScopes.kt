package com.matijasokol.githubapp.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.KoClassDeclaration
import com.lemonappdev.konsist.api.declaration.KoFileDeclaration
import com.lemonappdev.konsist.api.declaration.KoInterfaceDeclaration

internal val productionScope = Konsist.scopeFromProduction()

internal fun productionClasses(): List<KoClassDeclaration> = productionScope
    .classes(includeNested = false, includeLocal = false)

internal fun productionInterfaces(): List<KoInterfaceDeclaration> = productionScope
    .interfaces(includeNested = false)

internal fun viewModelClasses(): List<KoClassDeclaration> = productionClasses()
    .filter { clazz ->
        clazz.hasParent { parent -> parent.name == "ViewModel" } &&
            clazz.containingFile.hasImportWithName(ANDROIDX_VIEW_MODEL_IMPORT)
    }

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
