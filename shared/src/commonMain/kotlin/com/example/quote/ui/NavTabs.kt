package com.example.quote.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NavTabs(current: Destination, onSelect: (Destination) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TabButton("Browse", current is Destination.Browse) { onSelect(Destination.Browse) }
        TabButton("Favorites", current is Destination.Favorites) { onSelect(Destination.Favorites) }
        TabButton("Settings", current is Destination.Settings) { onSelect(Destination.Settings) }
    }
}

@Composable
private fun TabButton(text: String, selected: Boolean, onClick: () -> Unit) {
    // The selected tab is a filled button, the others are outlined.
    if (selected) Button(onClick = onClick) { Text(text) }
    else OutlinedButton(onClick = onClick) { Text(text) }
}