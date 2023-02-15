package com.matijasokol.ui_repodetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.ImageLoader

@Composable
fun RepoDetail(
    state: RepoDetailState,
    imageLoader: ImageLoader
) {
    Box(modifier = Modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = state.message
        )
    }
}