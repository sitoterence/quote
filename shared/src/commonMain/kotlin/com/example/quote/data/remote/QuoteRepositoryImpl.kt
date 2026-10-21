package com.example.quote.data.remote

import com.example.quote.data.remote.QuoteApi
import com.example.quote.domain.Quote
import com.example.quote.domain.QuoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// The real worker behind the QuoteRepository contract.
// DEPENDENCY INJECTION: it RECEIVES the api, it does not build it.
class QuoteRepositoryImpl(private val api: QuoteApi) : QuoteRepository {

    // CACHE (Module 9): a saved copy so we do not download again and again.
    private var cache: List<Quote> = emptyList()

    // private + Mutable: only this class changes it. public + read-only: others only watch it.
    private val _favorites = MutableStateFlow<List<Quote>>(emptyList())
    override val favorites: StateFlow<List<Quote>> = _favorites.asStateFlow()

    override suspend fun getQuotes(forceRefresh: Boolean): List<Quote> {
        if (cache.isNotEmpty() && !forceRefresh) return cache   // 1. use the saved copy
        cache = api.fetchQuotes()                                // 2. otherwise download
        return cache
    }

    override fun toggleFavorite(quote: Quote) {
        val current = _favorites.value
        _favorites.value =
            if (current.any { it.id == quote.id }) {
                current.filter { it.id != quote.id }   // already saved -> remove
            } else {
                current + quote                        // not saved -> add
            }
    }
}