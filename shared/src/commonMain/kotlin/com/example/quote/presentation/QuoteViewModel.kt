package com.example.quote.presentation

import com.example.quote.domain.GetQuotesUseCase
import com.example.quote.domain.Quote
import com.example.quote.domain.SearchQuotesUseCase
import com.example.quote.domain.ToggleFavoriteUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuoteViewModel(
    private val getQuotes: GetQuotesUseCase,
    private val searchQuotes: SearchQuotesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    val favorites: StateFlow<List<Quote>>,      // comes from the repository
    private val scope: CoroutineScope          // where our coroutines run
) {
    // The full list we downloaded. Search filters THIS list.
    private var allQuotes: List<Quote> = emptyList()

    // STATEFLOW = the screen state. Always has a value. Private mutable, public read-only.
    private val _uiState = MutableStateFlow<QuoteUiState>(QuoteUiState.Loading)
    val uiState: StateFlow<QuoteUiState> = _uiState.asStateFlow()

    // SHAREDFLOW = one-time messages ("Quotes refreshed!"). Not stored.
    private val _events = MutableSharedFlow<String>()
    val events: SharedFlow<String> = _events.asSharedFlow()

    // ACTION: load (or refresh) the quotes. Pattern: Loading -> try Success / catch Error.
    fun load(forceRefresh: Boolean = false) {
        scope.launch {
            _uiState.value = QuoteUiState.Loading
            try {
                allQuotes = getQuotes(forceRefresh)
                _uiState.value = QuoteUiState.Success(allQuotes)
                if (forceRefresh) _events.emit("Quotes refreshed!")
            } catch (e: Exception) {
                _uiState.value = QuoteUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // ACTION: the user typed in the search box.
    fun onSearch(query: String) {
        if (allQuotes.isEmpty()) return            // nothing loaded yet
        _uiState.value = QuoteUiState.Success(searchQuotes(allQuotes, query))
    }

    // ACTION: the user tapped Save / Remove.
    fun onToggleFavorite(quote: Quote) {
        toggleFavorite(quote)
        scope.launch { _events.emit("Favorites updated") }
    }
}