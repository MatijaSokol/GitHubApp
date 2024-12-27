package com.matijasokol.repo.list.components

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.matijasokol.coreui.components.RoundedImage
import com.matijasokol.coreui.components.withSharedBounds
import com.matijasokol.repo.domain.model.Author
import com.matijasokol.repo.domain.model.Repo
import com.matijasokol.repo.list.test.TAG_REPO_LIST_ITEM
import com.matijasokol.repo.list.test.TAG_REPO_NAME

@Suppress("ComposableParamOrder")
@Composable
fun RepoListItem(
    repo: Repo,
    modifier: Modifier = Modifier,
    onItemClick: (Repo) -> Unit,
    onImageClick: (Author) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onItemClick(repo) }
            .testTag(TAG_REPO_LIST_ITEM),
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(12.dp),
        elevation = 8.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RoundedImage(
                    modifier = Modifier.withSharedBounds(key = "${repo.author.image}/${repo.fullName}"),
                    imageUrl = repo.author.image,
                    contentDescription = repo.author.name,
                    onClick = { onImageClick(repo.author) },
                )

                Column(
                    modifier = Modifier
                        .padding(end = 8.dp),
                ) {
                    RepoInfoPanel(
                        repo = repo,
                    )
                }
            }

            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .withSharedBounds(key = "${repo.author.name}/${repo.name}")
                    .testTag(TAG_REPO_NAME),
                text = buildAnnotatedString {
                    append("${repo.author.name}/")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(repo.name)
                    }
                },
                textAlign = TextAlign.Center,
            )
        }
    }
}
