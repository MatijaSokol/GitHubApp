package com.matijasokol.coreui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.matijasokol.coreui.preview.GitHubAppPreviewContent
import com.matijasokol.coreui.preview.GitHubAppThemePreviews

@Composable
fun RoundedImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    borderColor: Color? = MaterialTheme.colorScheme.outline,
    borderWidth: Dp? = 2.dp,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val fallbackPainter = ColorPainter(MaterialTheme.colorScheme.surfaceContainerHighest)

    AsyncImage(
        model = imageUrl,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .run {
                if (borderColor != null && borderWidth != null) {
                    border(
                        width = borderWidth,
                        color = borderColor,
                        shape = CircleShape,
                    )
                } else {
                    this
                }
            }
            .clickable(enabled = enabled, onClick = onClick),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        placeholder = fallbackPainter,
        error = fallbackPainter,
        fallback = fallbackPainter,
    )
}

@GitHubAppThemePreviews
@Composable
private fun RoundedImagePreview() {
    GitHubAppPreviewContent {
        Row(
            modifier = Modifier.padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            RoundedImage(
                imageUrl = "",
                contentDescription = "Repository owner",
                onClick = {},
            )
            RoundedImage(
                imageUrl = "",
                contentDescription = "Disabled repository owner",
                borderColor = null,
                borderWidth = null,
                enabled = false,
                onClick = {},
            )
        }
    }
}
