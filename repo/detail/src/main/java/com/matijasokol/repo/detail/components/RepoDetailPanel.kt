package com.matijasokol.repo.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matijasokol.repo.detail.R
import kotlinx.collections.immutable.ImmutableList

@Composable
fun RepoDetailPanel(
    stats: ImmutableList<String>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp, vertical = 20.dp)
            .background(
                color = when (isSystemInDarkTheme()) {
                    true -> Color(50, 50, 50)
                    false -> Color(218, 218, 218)
                },
                shape = RoundedCornerShape(8.dp),
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        stickyHeader {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = when (isSystemInDarkTheme()) {
                            true -> Color(100, 100, 100)
                            false -> Color(180, 180, 180)
                        },
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp,
                        ),
                    )
                    .padding(16.dp),
                text = context.getString(R.string.repo_detail_panel_stats),
                fontSize = 20.sp,
            )
        }

        items(stats) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = it,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
            )

            if (stats.indexOf(it) != stats.lastIndex) {
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.White,
                )
            }
        }
    }
}
