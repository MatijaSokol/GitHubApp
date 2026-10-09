package com.matijasokol.githubapp.konsist

import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.jupiter.api.Test

class UseCaseKonsistTest {

    @Test
    fun `every use case constructor parameter has name derived from parameter type`() {
        useCaseClasses()
            .flatMap { useCase -> useCase.primaryConstructor?.parameters.orEmpty() }
            .assertTrue { parameter -> parameter.hasNameDerivedFromType() }
    }

    @Test
    fun `use cases expose operator invoke as their only public declaration`() {
        useCaseClasses()
            .assertTrue { useCase ->
                useCase.hasFunction(includeNested = false, includeLocal = false) { function ->
                    function.name == "invoke" && function.hasPublicOrDefaultModifier && function.hasOperatorModifier
                } &&
                    useCase.numPublicOrDefaultDeclarations(includeNested = true, includeLocal = false) == 1
            }
    }
}
