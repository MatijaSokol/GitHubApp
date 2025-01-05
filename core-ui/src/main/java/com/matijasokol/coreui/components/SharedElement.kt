@file:Suppress("CompositionLocalAllowlist", "ModifierComposable")

package com.matijasokol.coreui.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier

val LocalAnimatedContentScope = compositionLocalOf<AnimatedContentScope?> { null }
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

@Composable
fun Modifier.withSharedElement(
    key: Any,
): Modifier {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedContentScope = LocalAnimatedContentScope.current

    return if (sharedTransitionScope != null && animatedContentScope != null) {
        with(sharedTransitionScope) {
            sharedElement(
                state = rememberSharedContentState(key = key),
                animatedVisibilityScope = animatedContentScope,
            )
        }
    } else {
        Modifier
    }
}

@Composable
fun Modifier.withSharedBounds(
    key: Any,
): Modifier {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedContentScope = LocalAnimatedContentScope.current

    return this then if (sharedTransitionScope != null && animatedContentScope != null) {
        with(sharedTransitionScope) {
            sharedBounds(
                sharedContentState = rememberSharedContentState(key = key),
                animatedVisibilityScope = animatedContentScope,
            )
        }
    } else {
        Modifier
    }
}
