@file:Suppress("CompositionLocalAllowlist", "ModifierComposable")

package com.matijasokol.coreui.components

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.LocalNavAnimatedContentScope

val LocalSharedTransitionScope =
    compositionLocalOf<SharedTransitionScope> { error("SharedTransitionScope not provided") }

@Composable
fun Modifier.withSharedElement(
    key: Any,
): Modifier {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedContentScope = LocalNavAnimatedContentScope.current

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
    val animatedContentScope = LocalNavAnimatedContentScope.current

    return with(sharedTransitionScope) {
        sharedBounds(
            sharedContentState = rememberSharedContentState(key = key),
            animatedVisibilityScope = animatedContentScope,
        )
    }
}
