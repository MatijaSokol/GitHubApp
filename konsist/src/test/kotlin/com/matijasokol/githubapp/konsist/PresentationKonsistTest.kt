package com.matijasokol.githubapp.konsist

import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.jupiter.api.Test

class PresentationKonsistTest {

    @Test
    fun `only UiText resolves string resources in composables`() {
        productionScope.files
            .filter { file -> file.hasImportWithName(STRING_RESOURCE_IMPORT) }
            .assertTrue { file -> file.path.replace('\\', '/').endsWith(UI_TEXT_PATH) }
    }

    @Test
    fun `state and list UI models use ImmutableList for exposed collections`() {
        productionClasses()
            .filter { uiModelPackageNames.any(it::resideInPackage) }
            .filter { uiModelSuffixes.any(it.name::endsWith) }
            .flatMap { uiModel -> uiModel.properties(includeNested = false) }
            .filter { property -> property.text.contains(PLAIN_LIST_TYPE_DECLARATION) }
            .assertTrue { property -> property.text.contains(IMMUTABLE_LIST_TYPE_DECLARATION) }
    }

    @Test
    fun `view models expose StateFlow state`() {
        viewModelClasses()
            .flatMap { viewModel -> viewModel.properties(includeNested = false) }
            .filter { property -> property.name == "state" }
            .assertTrue { property -> property.text.contains(STATE_FLOW_TYPE_DECLARATION) }
    }

    @Test
    fun `view models expose Flow actions`() {
        viewModelClasses()
            .flatMap { viewModel -> viewModel.properties(includeNested = false) }
            .filter { property -> property.name == "actions" }
            .assertTrue { property -> property.text.contains(FLOW_TYPE_DECLARATION) }
    }

    @Test
    fun `view models and mappers do not resolve Android resources`() {
        (viewModelClasses() + mapperClasses())
            .assertFalse { clazz ->
                forbiddenResourceImports.any(clazz.containingFile::hasImportWithName)
            }
    }

    @Test
    fun `view models have onEvent event as only public event entry point`() {
        viewModelClasses()
            .assertTrue { viewModel ->
                viewModel.hasFunction(includeNested = false, includeLocal = false) { function ->
                    function.name == "onEvent" &&
                        function.hasPublicOrDefaultModifier &&
                        function.parameters.size == 1 &&
                        function.parameters.first().type.name.endsWith("Event")
                } &&
                    viewModel.countFunctions(includeNested = false, includeLocal = false) { function ->
                        function.hasPublicOrDefaultModifier && function.name != "onEvent"
                    } == 0
            }
    }
}

private val uiModelPackageNames = listOf(
    "com.matijasokol.repo.list..",
    "com.matijasokol.repo.detail..",
)

private val uiModelSuffixes = listOf("State", "Item", "Ui")

private const val PLAIN_LIST_TYPE_DECLARATION = ": List<"
private const val IMMUTABLE_LIST_TYPE_DECLARATION = ": ImmutableList<"
private const val STATE_FLOW_TYPE_DECLARATION = ": StateFlow<"
private const val FLOW_TYPE_DECLARATION = ": Flow<"
private val forbiddenResourceImports = listOf(
    "android.content.Context",
    "android.content.res.Resources",
    "androidx.compose.ui.res.stringResource",
)

private const val STRING_RESOURCE_IMPORT = "androidx.compose.ui.res.stringResource"
private const val UI_TEXT_PATH = "/coreui/text/UiText.kt"
