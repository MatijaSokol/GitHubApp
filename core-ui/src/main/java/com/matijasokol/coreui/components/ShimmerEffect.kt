package com.matijasokol.coreui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Suppress("ModifierComposable")
@Composable
fun Modifier.shimmerEffect(
    durationMillis: Int = 1000,
): Modifier {
    val transition = rememberInfiniteTransition(label = SHIMMER_TRANSITION_LABEL)

    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = SHIMMER_TRANSITION_LABEL,
    )

    return this then drawBehind {
        drawRect(
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(x = translateAnimation, y = translateAnimation),
                end = Offset(x = translateAnimation + 100f, y = translateAnimation + 100f),
            ),
        )
    }
}

private val shimmerColors = listOf(
    Color(0xFFB8B5B5),
    Color(0xFF8F8B8B),
    Color(0xFFB8B5B5),
)

private const val SHIMMER_TRANSITION_LABEL = "shimmer"
