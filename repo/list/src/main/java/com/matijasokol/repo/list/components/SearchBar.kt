package com.matijasokol.repo.list.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.matijasokol.coreui.preview.GitHubAppPreviewContent
import com.matijasokol.coreui.preview.GitHubAppThemePreviews
import com.matijasokol.repo.list.R

@Composable
fun SearchBar(
    text: String,
    placeholderText: String,
    searchContentDescription: String,
    clearSearchContentDescription: String,
    onTextChanged: (String) -> Unit,
    onClearClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(20.dp),
    ) {
        BasicTextField(
            value = text,
            onValueChange = onTextChanged,
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            ),
            cursorBrush = SolidColor(value = MaterialTheme.colorScheme.onSurface),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.search),
                            contentDescription = searchContentDescription,
                            modifier = Modifier.minimumInteractiveComponentSize(),
                            tint = MaterialTheme.colorScheme.primary,
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Box {
                            innerTextField()

                            if (text.isEmpty()) {
                                Text(
                                    text = placeholderText,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                )
                            }
                        }
                    }

                    if (text.isNotEmpty()) {
                        Surface(
                            modifier = Modifier.size(48.dp),
                            onClick = onClearClicked,
                            shape = CircleShape,
                            color = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.clear),
                                    contentDescription = clearSearchContentDescription,
                                )
                            }
                        }
                    }
                }
            },
        )
    }
}

@GitHubAppThemePreviews
@Composable
private fun SearchBarPreview() {
    GitHubAppPreviewContent {
        SearchBar(
            modifier = Modifier.padding(12.dp),
            text = "compose",
            placeholderText = "Search repositories",
            searchContentDescription = "Search",
            clearSearchContentDescription = "Clear search",
            onTextChanged = {},
            onClearClicked = {},
        )
    }
}
