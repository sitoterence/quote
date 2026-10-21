package com.example.quote.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quote.domain.Quote

@Composable
fun QuoteList(
    quotes: List<Quote>,
    favoriteIds: Set<Int>,                 // which quotes are already saved
    onQuoteClick: (Int) -> Unit,           // tapped a quote -> gives its id
    onToggleFavorite: (Quote) -> Unit
) {
    if (quotes.isEmpty()) {
        Text("Nothing here yet.")
    } else {
        // LazyColumn only draws the rows you can see (fast).
        LazyColumn {
            items(quotes) { quote ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onQuoteClick(quote.id) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("\"${quote.quote}\"")
                        Text("- ${quote.author}", style = MaterialTheme.typography.labelMedium)
                        TextButton(onClick = { onToggleFavorite(quote) }) {
                            Text(if (quote.id in favoriteIds) "Remove from favorites" else "Save to favorites")
                        }
                    }
                }
            }
        }
    }
}