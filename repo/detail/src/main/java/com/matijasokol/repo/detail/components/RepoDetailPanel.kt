package com.matijasokol.repo.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList

@Composable
fun RepoDetailPanel(
    stats: ImmutableList<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        stats.take(4).chunked(2).forEach { rowStats ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                rowStats.forEach { stat ->
                    MetricCard(stat = stat, modifier = Modifier.weight(1f))
                }
            }
        }

        if (stats.size > UPDATED_INDEX) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                MetadataCard(stats[LANGUAGE_INDEX], Modifier.weight(1f))
                MetadataCard(stats[UPDATED_INDEX], Modifier.weight(1f))
            }
        }

        stats.getOrNull(DESCRIPTION_INDEX)?.let { DescriptionCard(it) }
    }
}

@Composable
private fun MetricCard(stat: String, modifier: Modifier = Modifier) {
    val (label, value) = stat.toLabelAndValue()
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
            )
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
private fun MetadataCard(stat: String, modifier: Modifier = Modifier) {
    val (label, value) = stat.toLabelAndValue()
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun DescriptionCard(stat: String) {
    val (label, value) = stat.toLabelAndValue()
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = value,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

private fun String.toLabelAndValue(): Pair<String, String> =
    when (val separator = indexOf(':')) {
        -1 -> "" to this
        else -> substring(0, separator) to substring(separator + 1).trim()
    }

private const val LANGUAGE_INDEX = 4
private const val DESCRIPTION_INDEX = 5
private const val UPDATED_INDEX = 6
