package com.matijasokol.ui_repodetail.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matijasokol.ui_repodetail.R
import com.matijasokol.ui_repodetail.test.TAG_REPO_DETAIL_INFO_TEXT

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RepoDetailPanel(
    stats: List<String>
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp, vertical = 20.dp)
            .background(
                color = Color(218, 218, 218),
                shape = RoundedCornerShape(8.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stickyHeader {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(180, 180, 180),
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .padding(16.dp),
                text = context.getString(R.string.repo_detail_panel_stats),
                fontSize = 20.sp
            )
        }

        items(stats) {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .testTag(TAG_REPO_DETAIL_INFO_TEXT),
                text = it,
                fontSize = 20.sp
            )

            if (stats.indexOf(it) != stats.lastIndex) {
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.White
                )
            }
        }
    }
}