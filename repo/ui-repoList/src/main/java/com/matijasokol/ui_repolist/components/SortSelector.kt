package com.matijasokol.ui_repolist.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@ExperimentalAnimationApi
@Composable
fun SortSelector(
    descString: String,
    ascString: String,
    isEnabled: Boolean,
    isDescSelected: Boolean,
    isAscSelected: Boolean,
    onUpdateRepoSortTypeDesc: () -> Unit,
    onUpdateRepoSortTypeAsc: () -> Unit
) {
    // Descending Order
    AnimatedVisibility(visible = isEnabled) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 8.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null, // disable the highlight
                    enabled = isEnabled,
                    onClick = {
                        onUpdateRepoSortTypeDesc()
                    }
                )
        ) {
            Checkbox(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically),
                enabled = isEnabled,
                checked = isEnabled && isDescSelected,
                onCheckedChange = {
                    onUpdateRepoSortTypeDesc()
                },
                colors = CheckboxDefaults.colors(MaterialTheme.colors.primary)
            )
            Text(
                text = descString,
                style = MaterialTheme.typography.body1
            )
        }
    }

    // Ascending Order
    AnimatedVisibility(visible = isEnabled) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 8.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null, // disable the highlight
                    enabled = isEnabled,
                    onClick = {
                        onUpdateRepoSortTypeAsc()
                    }
                )
        ) {
            Checkbox(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically),
                enabled = isEnabled,
                checked = isEnabled && isAscSelected,
                onCheckedChange = {
                    onUpdateRepoSortTypeAsc()
                },
                colors = CheckboxDefaults.colors(MaterialTheme.colors.primary)
            )
            Text(
                text = ascString,
                style = MaterialTheme.typography.body1
            )
        }
    }
}