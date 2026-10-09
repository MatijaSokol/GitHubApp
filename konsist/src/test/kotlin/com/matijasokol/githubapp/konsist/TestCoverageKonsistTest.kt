package com.matijasokol.githubapp.konsist

import com.lemonappdev.konsist.api.ext.list.withDeclarations
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.jupiter.api.Test

class TestCoverageKonsistTest {

    @Test
    fun `every view model has test`() {
        viewModelClasses()
            .withDeclarations()
            .assertTrue { viewModel -> viewModel.hasMatchingTestClassWithSut() }
    }

    @Test
    fun `every use case class has test`() {
        useCaseClasses()
            .assertTrue { useCase -> useCase.hasMatchingTestClassWithSut() }
    }
}
