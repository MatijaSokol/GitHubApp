package com.matijasokol.githubapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun GitHubAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}

private val LightColorPalette = lightColors(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryVariant = primaryVariantLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryVariant = secondaryVariantLight,
    error = errorLight,
    onError = onErrorLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
)

private val DarkColorPalette = darkColors(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryVariant = primaryVariantDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryVariant = secondaryVariantDark,
    error = errorDark,
    onError = onErrorDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
)
