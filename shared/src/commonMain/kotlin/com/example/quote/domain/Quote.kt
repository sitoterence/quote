package com.example.quote.domain


import kotlinx.serialization.Serializable

// @Serializable = "this class can be built from JSON".
// The property names MUST match the JSON field names: id, quote, author.
@Serializable
data class Quote(
    val id: Int,
    val quote: String,
    val author: String
)