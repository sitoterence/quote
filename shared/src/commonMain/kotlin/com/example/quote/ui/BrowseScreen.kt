package com.example.quote.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quote.domain.Quote
import com.example.quote.presentation.QuoteUiState
import kotlinx.coroutines.flow.Flow

// This screen does not know about the ViewModel. It only receives values and
// callbacks, so it is easy to read and easy to trace.
@Composable
fun BrowseScreen(
    state: QuoteUiState,
    favoriteIds: Set<Int>,
    events: Flow<String>,
    onSearch: (String) -> Unit,
    onRefresh: () -> Unit,
    onQuoteClick: (Int) -> Unit,
    onToggleFavorite: (Quote) -> Unit
) {
    var query by remember { mutableStateOf("") }     // text in the search box
    var banner by remember { mutableStateOf("") }    // latest one-time message

    // Runs once when the screen appears: listen to the SharedFlow of messages.
    LaunchedEffect(Unit) {
        events.collect { message -> banner = message }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("QuoteVault", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = query,
            onValueChange = { query = it; onSearch(it) },   // update box + filter list
            label = { Text("Search quotes or authors") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = onRefresh) { Text("Refresh from server") }

        if (banner.isNotEmpty()) {
            Text(banner, color = MaterialTheme.colorScheme.primary)
        }

        // THE KEY PART: draw the UI from the sealed class state.
        // Exactly one branch runs; the compiler checks we covered every state.
        when (state) {
            is QuoteUiState.Loading -> CircularProgressIndicator()
            is QuoteUiState.Success ->
                QuoteList(state.quotes, favoriteIds, onQuoteClick, onToggleFavorite)
            is QuoteUiState.Error -> {
                Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                Button(onClick = onRefresh) { Text("Try again") }
            }
        }
    }
}