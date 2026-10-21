package com.example.quote.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quote.domain.Quote

@Composable
fun FavoritesScreen(
    favorites: List<Quote>,
    onQuoteClick: (Int) -> Unit,
    onToggleFavorite: (Quote) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Favorites", style = MaterialTheme.typography.headlineMedium)
        // Same list as Browse. Every quote here is a favorite, so all ids are "saved".
        QuoteList(favorites, favorites.map { it.id }.toSet(), onQuoteClick, onToggleFavorite)
    }
}