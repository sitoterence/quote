package com.example.quote.domain

// Rule 1: get the quotes from the repository.
class GetQuotesUseCase(private val repository: QuoteRepository) {
    // operator fun invoke = we can call the object like a function: getQuotes()
    suspend operator fun invoke(forceRefresh: Boolean = false): List<Quote> =
        repository.getQuotes(forceRefresh)
}

// Rule 2: search by quote text OR author name, ignoring upper/lower case.
class SearchQuotesUseCase {
    operator fun invoke(quotes: List<Quote>, query: String): List<Quote> {
        if (query.isBlank()) return quotes          // empty search = show everything
        return quotes.filter {
            it.quote.contains(query, ignoreCase = true) ||
                    it.author.contains(query, ignoreCase = true)
        }
    }
}

// Rule 3: save or un-save a favorite.
class ToggleFavoriteUseCase(private val repository: QuoteRepository) {
    operator fun invoke(quote: Quote) = repository.toggleFavorite(quote)
}