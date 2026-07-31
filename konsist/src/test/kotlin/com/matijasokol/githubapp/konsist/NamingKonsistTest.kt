package com.matijasokol.githubapp.konsist

import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.jupiter.api.Test

class NamingKonsistTest {

    @Test
    fun `use cases use UseCase suffix and expose operator invoke`() {
        useCaseClasses()
            .assertTrue { useCase ->
                useCase.name.endsWith("UseCase") &&
                    useCase.hasFunction(includeNested = false, includeLocal = false) { function ->
                        function.name == "invoke" &&
                            function.hasPublicOrDefaultModifier &&
                            function.hasOperatorModifier
                    } &&
                    useCase.countFunctions(includeNested = false, includeLocal = false) { function ->
                        function.hasPublicOrDefaultModifier
                    } == 1
            }
    }

    @Test
    fun `view models use ViewModel suffix and Hilt annotation`() {
        viewModelClasses()
            .assertTrue { viewModel ->
                viewModel.name.endsWith("ViewModel") && viewModel.hasAnnotationWithName("HiltViewModel")
            }
    }

    @Test
    fun `view models have a single constructor`() {
        viewModelClasses()
            .assertTrue { viewModel -> viewModel.constructors.size == 1 }
    }

    @Test
    fun `view models do not depend on navigator`() {
        viewModelClasses()
            .assertTrue { viewModel ->
                viewModel.hasAllProperties(includeNested = false) { property ->
                    property.type?.name != "Navigator"
                }
            }
    }

    @Test
    fun `events and actions are sealed interfaces or classes`() {
        listOf(productionClasses(), productionInterfaces())
            .flatten()
            .filter { it.name.endsWith("Event") || it.name.endsWith("Action") }
            .assertTrue { it.hasSealedModifier }
    }

    @Test
    fun `state declarations are data classes or sealed state hierarchies`() {
        productionClasses()
            .filter { it.name.endsWith("State") }
            .assertTrue { state -> state.hasDataModifier || state.hasSealedModifier }
    }

    @Test
    fun `data classes use only val properties`() {
        productionClasses()
            .filter { it.hasDataModifier }
            .assertTrue { dataClass ->
                dataClass.hasAllProperties(includeNested = false) { property -> property.isVal }
            }
    }
}
