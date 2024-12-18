package com.matijasokol.repo.list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.matijasokol.coreui.components.TextWithIcon
import com.matijasokol.repo.list.R
import com.matijasokol.repodomain.model.Repo

@Composable
fun RepoInfoPanel(
    repo: Repo,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .wrapContentWidth(
                align = Alignment.End,
                unbounded = true,
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.End,
    ) {
        TextWithIcon(
            text = repo.watchersCount.toString(),
            imageVector = ImageVector.vectorResource(id = R.drawable.watch),
        )

        Divider(thickness = 1.dp, color = Color.Black)

        TextWithIcon(
            text = repo.forksCount.toString(),
            imageVector = ImageVector.vectorResource(id = R.drawable.fork),
        )

        Divider(thickness = 1.dp, color = Color.Black)

        TextWithIcon(
            text = repo.issuesCount.toString(),
            imageVector = ImageVector.vectorResource(id = R.drawable.issue),
        )
    }
}
