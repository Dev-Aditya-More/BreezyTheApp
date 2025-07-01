package com.example.breezytheapp.ui.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    lastSearch: String = "",
    onSearch: (String) -> Unit
) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(lastSearch))
    }
    var error by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            if (error) error = false
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Enter city name") },
        leadingIcon = {
            IconButton(onClick = {
                if (text.text.trim().isNotEmpty()) {
                    onSearch(text.text.trim())
                } else {
                    error = true
                }
            }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        },
        trailingIcon = {
            if (text.text.isNotEmpty()) {
                IconButton(onClick = {
                    text = TextFieldValue("")
                    error = false
                }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
            }
        },
        isError = error,
        singleLine = true,
        supportingText = {
            if (error) Text("Please enter a city", color = MaterialTheme.colorScheme.error)
        }
    )
}
