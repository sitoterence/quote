package com.example.quote.domain

import kotlinx.coroutines.flow.StateFlow

// REPOSITORY PATTERN (Module 6): a contract that says WHAT we can do, not HOW.
// The rest of the app only knows this interface.
interface QuoteRepository {

    // Favorites the user saved. A StateFlow so the UI updates by itself.
    val favorites: StateFlow<List<Quote>>

    // suspend = may wait for the internet. forceRefresh = ignore the cache.
    suspend fun getQuotes(forceRefresh: Boolean = false): List<Quote>

    // Add the quote to favorites, or remove it if it is already there.
    fun toggleFavorite(quote: Quote)
}