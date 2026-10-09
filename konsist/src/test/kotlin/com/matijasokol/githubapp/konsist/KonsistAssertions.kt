package com.matijasokol.githubapp.konsist

import com.lemonappdev.konsist.api.declaration.KoClassDeclaration
import com.lemonappdev.konsist.api.declaration.KoParameterDeclaration

/**
 * True when a `<ClassName>Test` class exists in a test source set and declares a class-level [SUT_PROPERTY_NAME]
 * property holding an instance of this class.
 */
internal fun KoClassDeclaration.hasMatchingTestClassWithSut(): Boolean =
    testClasses(testPropertyName = SUT_PROPERTY_NAME).any { testClass ->
        testClass.name == "$name$TEST_CLASS_SUFFIX" &&
            testClass.hasProperty(includeNested = false) { property -> property.name == SUT_PROPERTY_NAME }
    }

/** True when the parameter is named after its type, e.g. `repoService: RepoService`. */
internal fun KoParameterDeclaration.hasNameDerivedFromType(): Boolean =
    name.replaceFirstChar { it.titlecase() } == type.sourceType

private const val SUT_PROPERTY_NAME = "sut"
private const val TEST_CLASS_SUFFIX = "Test"
