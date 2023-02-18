package com.matijasokol.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun RoundedImage(
    imageUrl: String,
    contentDescription: String?,
    size: Dp = 64.dp,
    imageLoader: ImageLoader,
    borderColor: Color? = Color.Black,
    borderWidth: Dp? = 2.dp,
    onClick: () -> Unit
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .build(),
        imageLoader = imageLoader
    )
    Image(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .apply {
                if (borderColor != null && borderWidth != null) {
                    border(
                        width = borderWidth,
                        color = borderColor,
                        shape = CircleShape
                    )
                }
            }
            .clickable { onClick() },
        painter = painter,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
    )
}