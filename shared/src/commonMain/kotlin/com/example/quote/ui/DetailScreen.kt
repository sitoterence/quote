package com.example.quote.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quote.domain.Quote

// "Quote?" can be null (not found), so we handle both cases.
@Composable
fun DetailScreen(
    quote: Quote?,
    isFavorite: Boolean,
    onToggleFavorite: (Quote) -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (quote == null) {
            Text("Quote not found")
        } else {
            Text("\"${quote.quote}\"", style = MaterialTheme.typography.headlineSmall)
            Text("- ${quote.author}")
            Button(onClick = { onToggleFavorite(quote) }) {
                Text(if (isFavorite) "Remove from favorites" else "Save to favorites")
            }
        }
        OutlinedButton(onClick = onBack) { Text("Back") }
    }
}