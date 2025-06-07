@file:Suppress("CompositionLocalAllowlist", "ModifierComposable")

package com.matijasokol.coreui.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier

val LocalAnimatedContentScope = compositionLocalOf<AnimatedContentScope> { error("AnimatedContentScope not provided") }
val LocalSharedTransitionScope =
    compositionLocalOf<SharedTransitionScope> { error("SharedTransitionScope not provided") }

@Composable
fun Modifier.withSharedElement(
    key: Any,
): Modifier {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedContentScope = LocalAnimatedContentScope.current

    return with(sharedTransitionScope) {
        sharedElement(
            sharedContentState = rememberSharedContentState(key = key),
            animatedVisibilityScope = animatedContentScope,
        )
    }
}

@Composable
fun Modifier.withSharedBounds(
    key: Any,
): Modifier {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedContentScope = LocalAnimatedContentScope.current

    return with(sharedTransitionScope) {
        sharedBounds(
            sharedContentState = rememberSharedContentState(key = key),
            animatedVisibilityScope = animatedContentScope,
        )
    }
}
