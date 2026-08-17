package com.matijasokol.repo.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.matijasokol.coreui.components.shimmerEffect
import com.matijasokol.coreui.preview.GitHubAppPreviewContent
import com.matijasokol.coreui.preview.GitHubAppThemePreviews

@Composable
fun ShimmerRepoListItem(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .shimmerEffect()
            .padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(46.dp).clip(CircleShape).shimmerEffect())
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Box(modifier = Modifier.fillMaxWidth(0.5f).height(8.dp).shimmerEffect())
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth(0.85f).height(14.dp).shimmerEffect())
            }
        }
        Spacer(modifier = Modifier.height(28.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            repeat(3) {
                Box(modifier = Modifier.size(width = 28.dp, height = 18.dp).shimmerEffect())
            }
        }
    }
}

@GitHubAppThemePreviews
@Composable
private fun ShimmerRepoListItemPreview() {
    GitHubAppPreviewContent {
        ShimmerRepoListItem(modifier = Modifier.padding(12.dp))
    }
}
