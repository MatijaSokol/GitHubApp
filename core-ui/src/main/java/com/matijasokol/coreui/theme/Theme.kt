package com.matijasokol.coreui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * On Android 12 (API 31) and newer, when [dynamicColor] is `true`, colors come from the user's wallpaper
 * (Material You). On older devices, or when [dynamicColor] is `false`, the static brand palettes are used.
 * Previews and UI tests pass `dynamicColor = false` so they render deterministically.
 */
@Composable
fun GitHubAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val (colorScheme, shimmerColors) = remember(darkTheme, dynamicColor, context) {
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val dynamicScheme = if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
                dynamicScheme to dynamicScheme.dynamicShimmerColors()
            }
            darkTheme -> DarkColorPalette to StaticShimmerColors
            else -> LightColorPalette to StaticShimmerColors
        }
    }

    CompositionLocalProvider(LocalShimmerColors provides shimmerColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = GitHubAppTypography,
            shapes = GitHubAppShapes,
            content = content,
        )
    }
}

/**
 * Gradient colors used by `Modifier.shimmerEffect`. They are not part of [ColorScheme], so the theme provides them:
 * the static palettes keep the original neutral grays, dynamic schemes derive them from wallpaper-based roles.
 */
internal val LocalShimmerColors = staticCompositionLocalOf { StaticShimmerColors }

private val StaticShimmerColors: ImmutableList<Color> = persistentListOf(
    shimmerBase,
    shimmerHighlight,
    shimmerBase,
)

private fun ColorScheme.dynamicShimmerColors(): ImmutableList<Color> = persistentListOf(
    surfaceContainerHighest,
    outlineVariant,
    surfaceContainerHighest,
)

private val LightColorPalette = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryVariantLight,
    onPrimaryContainer = onSurfaceLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryVariantLight,
    onSecondaryContainer = onSurfaceLight,
    error = errorLight,
    onError = onErrorLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
)

private val DarkColorPalette = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryVariantDark,
    onPrimaryContainer = onSurfaceDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryVariantDark,
    onSecondaryContainer = onSurfaceDark,
    error = errorDark,
    onError = onErrorDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
)
