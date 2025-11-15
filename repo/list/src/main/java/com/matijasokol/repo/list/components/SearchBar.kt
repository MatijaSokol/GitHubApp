package com.matijasokol.repo.list.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
        elevation = elevation,
        shape = RoundedCornerShape(32.dp),
    ) {
        BasicTextField(
            value = text,
            onValueChange = onTextChanged,
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colors.onSurface,
            ),
            cursorBrush = SolidColor(value = MaterialTheme.colors.onSurface),
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
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
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
                        IconButton(
                            onClick = onClearClicked,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Clear",
                            )
                        }
                    }
                }
            },
            singleLine = true,
        )
    }
}
