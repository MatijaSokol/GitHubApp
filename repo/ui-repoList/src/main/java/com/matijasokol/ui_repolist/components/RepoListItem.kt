package com.matijasokol.ui_repolist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.matijasokol.components.RoundedImage
import com.matijasokol.repo_domain.model.Author
import com.matijasokol.repo_domain.model.Repo
import com.matijasokol.ui_repolist.RepoInfoPanel

@Composable
fun RepoListItem(
    repo: Repo,
    imageLoader: ImageLoader,
    onItemClick: (Repo) -> Unit,
    onImageClick: (Author) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(12.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick(repo) }
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoundedImage(
                    imageUrl = repo.author.image,
                    contentDescription = repo.author.name,
                    imageLoader = imageLoader,
                    onClick = { onImageClick(repo.author) }
                )

                Column(
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    RepoInfoPanel(
                        repo = repo
                    )
                }
            }

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = buildAnnotatedString {
                    append("${repo.author.name}/")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(repo.name)
                    }
                },
                textAlign = TextAlign.Center
            )
        }
    }
}