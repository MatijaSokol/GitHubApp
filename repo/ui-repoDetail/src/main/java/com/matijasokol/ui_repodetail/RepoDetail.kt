package com.matijasokol.ui_repodetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.ImageLoader
import com.matijasokol.components.RoundedImage

@Composable
fun RepoDetail(
    state: RepoDetailState,
    imageLoader: ImageLoader
) {
    Box(modifier = Modifier.fillMaxSize()
    ) {
        state.repo?.let { repo ->
            RoundedImage(
                imageUrl = repo.author.image,
                contentDescription = "asd",
                imageLoader = imageLoader,
                onClick = {}
            )
        }
    }
}