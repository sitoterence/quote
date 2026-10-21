package com.example.quote.ui

// Every screen the app can show, as a sealed class.
sealed class Destination {
    object Browse : Destination()
    object Favorites : Destination()
    object Settings : Destination()
    data class Detail(val quoteId: Int) : Destination()   // carries the id (the argument)
}