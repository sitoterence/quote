package com.example.quote.presentation

import com.example.quote.domain.Quote

// SEALED CLASS: the browse screen is in exactly ONE of these states at a time.
//   object     -> state with no data
//   data class -> state that carries data
sealed class QuoteUiState {
    object Loading : QuoteUiState()
    data class Success(val quotes: List<Quote>) : QuoteUiState()
    data class Error(val message: String) : QuoteUiState()
}