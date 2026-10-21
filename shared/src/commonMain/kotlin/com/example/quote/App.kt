package com.example.quote

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quote.data.remote.QuoteRepositoryImpl
import com.example.quote.data.remote.QuoteApi
import com.example.quote.data.remote.createHttpClient
import com.example.quote.domain.GetQuotesUseCase
import com.example.quote.domain.SearchQuotesUseCase
import com.example.quote.domain.ToggleFavoriteUseCase
import com.example.quote.presentation.QuoteUiState
import com.example.quote.presentation.QuoteViewModel
import com.example.quote.ui.BrowseScreen
import com.example.quote.ui.DetailScreen
import com.example.quote.ui.Destination
import com.example.quote.ui.FavoritesScreen
import com.example.quote.ui.NavTabs

@Composable
fun App() {
    // ---- dependency injection by hand (same as Step 7) ----
    val scope = rememberCoroutineScope()
    val repository = remember { QuoteRepositoryImpl(QuoteApi(createHttpClient())) }
    val viewModel = remember {
        QuoteViewModel(
            getQuotes = GetQuotesUseCase(repository),
            searchQuotes = SearchQuotesUseCase(),
            toggleFavorite = ToggleFavoriteUseCase(repository),
            favorites = repository.favorites,
            scope = scope
        )
    }

    val uiState by viewModel.uiState.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val favoriteIds = favorites.map { it.id }.toSet()

    // ---- NAVIGATION STATE: which screen are we on? ----
    var screen by remember { mutableStateOf<Destination>(Destination.Browse) }

    LaunchedEffect(Unit) { viewModel.load() }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val current = screen
            Column(modifier = Modifier.fillMaxSize()) {

                // The screen takes all the space above the tab bar.
                Box(modifier = Modifier.weight(1f)) {
                    when (current) {
                        is Destination.Browse -> BrowseScreen(
                            state = uiState,
                            favoriteIds = favoriteIds,
                            events = viewModel.events,
                            onSearch = { viewModel.onSearch(it) },
                            onRefresh = { viewModel.load(forceRefresh = true) },
                            onQuoteClick = { id -> screen = Destination.Detail(id) },   // go + pass id
                            onToggleFavorite = { viewModel.onToggleFavorite(it) }
                        )

                        is Destination.Favorites -> FavoritesScreen(
                            favorites = favorites,
                            onQuoteClick = { id -> screen = Destination.Detail(id) },
                            onToggleFavorite = { viewModel.onToggleFavorite(it) }
                        )

                        is Destination.Settings ->
                            Text("Settings come in Step 9", modifier = Modifier.padding(16.dp))

                        is Destination.Detail -> {
                            // Use the id we navigated with to find the quote.
                            val quote =
                                (uiState as? QuoteUiState.Success)?.quotes
                                    ?.firstOrNull { it.id == current.quoteId }
                                    ?: favorites.firstOrNull { it.id == current.quoteId }
                            DetailScreen(
                                quote = quote,
                                isFavorite = current.quoteId in favoriteIds,
                                onToggleFavorite = { viewModel.onToggleFavorite(it) },
                                onBack = { screen = Destination.Browse }               // go back
                            )
                        }
                    }
                }

                // Tabs are hidden on the detail screen.
                if (current !is Destination.Detail) {
                    NavTabs(current = current, onSelect = { screen = it })
                }
            }
        }
    }
}