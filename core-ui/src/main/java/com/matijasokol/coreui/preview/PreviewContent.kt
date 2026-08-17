package com.matijasokol.coreui.preview

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.matijasokol.coreui.components.LocalSharedTransitionScope
import com.matijasokol.coreui.theme.GitHubAppTheme

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun GitHubAppPreviewContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    GitHubAppTheme {
        Surface(modifier = modifier) {
            AnimatedContent(Unit) {
                SharedTransitionLayout {
                    CompositionLocalProvider(
                        LocalSharedTransitionScope provides this,
                        LocalNavAnimatedContentScope provides this@AnimatedContent,
                        content = content,
                    )
                }
            }
        }
    }
}
