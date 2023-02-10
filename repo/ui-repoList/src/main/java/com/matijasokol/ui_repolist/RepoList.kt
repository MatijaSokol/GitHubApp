package com.matijasokol.ui_repolist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RepoList(
    repos: List<String>
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(repos) {
            Text(text = it)
        }
    }
}