package com.matijasokol.githubapp.konsist

import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.jupiter.api.Test

class ViewModelKonsistTest {

    @Test
    fun `every view model constructor parameter has name derived from parameter type`() {
        viewModelClasses()
            .flatMap { viewModel -> viewModel.primaryConstructor?.parameters.orEmpty() }
            // Hilt @Assisted parameters (e.g. `destination: Destination.RepoDetail`) keep their natural names.
            .filterNot { parameter -> parameter.hasAnnotationWithName(ASSISTED_ANNOTATION) }
            .assertTrue { parameter -> parameter.hasNameDerivedFromType() }
    }
}

private const val ASSISTED_ANNOTATION = "Assisted"
