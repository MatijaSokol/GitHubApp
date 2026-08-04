package com.matijasokol.repo.list.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.matijasokol.core.domain.SortOrder
import com.matijasokol.repo.domain.RepoSortType
import com.matijasokol.repo.list.R
import kotlinx.coroutines.launch

@Composable
fun RepoSortBottomBar(
    appliedSortType: RepoSortType,
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    onSortTypeClicked: (RepoSortType) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val pressProgress = remember { Animatable(0f) }
    val containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.62f)
    val contentDescription = stringResource(R.string.repo_list_sort_options_content_description)

    Row(
        modifier = modifier
            .drawBackdrop(
                backdrop = backdrop,
                shape = { RoundedCornerShape(30.dp) },
                effects = {
                    vibrancy()
                    blur(8.dp.toPx())
                    lens(18.dp.toPx(), 28.dp.toPx())
                },
                layerBlock = {
                    val scale = lerp(1f, 1f + 12.dp.toPx() / size.width, pressProgress.value)
                    scaleX = scale
                    scaleY = scale
                },
                onDrawSurface = { drawRect(containerColor) },
            )
            .pointerInput(scope) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)
                    scope.launch { pressProgress.animateTo(1f, spring(0.5f, 300f, 0.001f)) }
                    waitForUpOrCancellation()
                    scope.launch { pressProgress.animateTo(0f, spring(0.5f, 300f, 0.001f)) }
                }
            }
            .height(64.dp)
            .fillMaxWidth()
            .semantics { this.contentDescription = contentDescription }
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SortOption(
            label = stringResource(R.string.repo_list_sort_stars),
            selected = appliedSortType is RepoSortType.Stars,
            order = appliedSortType.order,
            modifier = Modifier.weight(1f).fillMaxHeight(),
            onOrderClick = { onSortTypeClicked(RepoSortType.Stars(it)) },
        )
        SortOption(
            label = stringResource(R.string.repo_list_sort_forks),
            selected = appliedSortType is RepoSortType.Forks,
            order = appliedSortType.order,
            modifier = Modifier.weight(1f).fillMaxHeight(),
            onOrderClick = { onSortTypeClicked(RepoSortType.Forks(it)) },
        )
        SortOption(
            label = stringResource(R.string.repo_list_sort_updated),
            selected = appliedSortType is RepoSortType.Updated,
            order = appliedSortType.order,
            modifier = Modifier.weight(1f).fillMaxHeight(),
            onOrderClick = { onSortTypeClicked(RepoSortType.Updated(it)) },
        )
    }
}

@Composable
private fun SortOption(
    label: String,
    selected: Boolean,
    order: SortOrder,
    modifier: Modifier = Modifier,
    onOrderClick: (SortOrder) -> Unit,
) {
    val labelColor = when (selected) {
        true -> MaterialTheme.colorScheme.primary
        false -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(26.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Row(modifier = Modifier.matchParentSize()) {
            DirectionAction(
                label = label,
                order = SortOrder.Ascending,
                selected = selected && order == SortOrder.Ascending,
                modifier = Modifier.weight(1f).fillMaxHeight(),
                onClick = { onOrderClick(SortOrder.Ascending) },
            )
            DirectionAction(
                label = label,
                order = SortOrder.Descending,
                selected = selected && order == SortOrder.Descending,
                modifier = Modifier.weight(1f).fillMaxHeight(),
                onClick = { onOrderClick(SortOrder.Descending) },
            )
        }
        Text(
            text = label,
            color = labelColor,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun DirectionAction(
    label: String,
    order: SortOrder,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val direction = stringResource(
        when (order) {
            SortOrder.Ascending -> R.string.repo_list_sort_ascending
            SortOrder.Descending -> R.string.repo_list_sort_descending
        },
    )
    val contentColor = when (selected) {
        true -> MaterialTheme.colorScheme.primary
        false -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier,
        contentAlignment = when (order) {
            SortOrder.Ascending -> Alignment.CenterStart
            SortOrder.Descending -> Alignment.CenterEnd
        },
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .size(36.dp)
                .clip(RoundedCornerShape(18.dp))
                .clickable(onClick = onClick)
                .semantics { contentDescription = "$label $direction" },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.sort_arrow),
                contentDescription = null,
                modifier = Modifier
                    .size(if (selected) 24.dp else 18.dp)
                    .rotate(if (order == SortOrder.Ascending) 0f else 180f),
                tint = contentColor,
            )
        }
    }
}
