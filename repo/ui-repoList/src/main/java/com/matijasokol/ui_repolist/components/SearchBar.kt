package com.matijasokol.ui_repolist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    value: String,
    modifier: Modifier,
    onTextChanged: (String) -> Unit,
    onClearClicked: () -> Unit
) {
    TextField(
        modifier = modifier
            .padding(8.dp),
        value = value,
        onValueChange = onTextChanged,
        label = { Text(text = "Search") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        ),
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
        trailingIcon = {
            if (value.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear",
                    modifier = Modifier.clickable {
                        onClearClicked()
                    }
                )
            }
        },
        textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
        colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface)
    )
}