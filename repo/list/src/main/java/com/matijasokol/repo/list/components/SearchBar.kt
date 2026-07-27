package com.matijasokol.repo.list.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.matijasokol.repo.list.R

@Suppress("ComposableParamOrder")
@Composable
fun SearchBar(
    text: String,
    placeholderText: String,
    elevation: Dp,
    modifier: Modifier = Modifier,
    onTextChanged: (String) -> Unit,
    onClearClicked: () -> Unit,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(elevation),
        tonalElevation = elevation,
        shadowElevation = elevation,
        shape = RoundedCornerShape(32.dp),
    ) {
        BasicTextField(
            value = text,
            onValueChange = onTextChanged,
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onSurface,
            ),
            cursorBrush = SolidColor(value = MaterialTheme.colorScheme.onSurface),
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
                            contentDescription = stringResource(R.string.repo_list_search_content_description),
                            // Added to be same size as IconButton, but without ripple effect
                            modifier = Modifier.minimumInteractiveComponentSize(),
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Box {
                            innerTextField()

                            if (text.isEmpty()) {
                                Text(
                                    text = placeholderText,
                                    color = Color.Gray.copy(alpha = 0.5f),
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
                                    contentDescription = stringResource(
                                        R.string.repo_list_clear_search_content_description,
                                    ),
                                )
                            }
                        }
                    }
                }
            },
            singleLine = true,
        )
    }
}
