package com.example.quote.data.remote

import com.example.quote.domain.Quote
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// The "all quotes" JSON is a WRAPPER: { quotes: [...], total, skip, limit }.
// This class matches that shape so Ktor can convert it.
@Serializable
data class QuotesResponse(
    val quotes: List<Quote>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

// Builds the HTTP client. install(ContentNegotiation) = "convert JSON automatically".
// ktor picks the right engine (CIO on desktop, Js on web) from your Gradle dependencies.
// QuoteApi.kt (commonMain)
internal fun HttpClientConfig<*>.configure() {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}

expect fun createHttpClient(): HttpClient    // each platform provides its own

class QuoteApi(private val client: HttpClient) {

    suspend fun fetchQuotes(): List<Quote> {
        val response = client.get("https://dummyjson.com/quotes?limit=0")

        // Module 9: check the STATUS, not only exceptions.
        if (response.status != HttpStatusCode.OK) {
            throw Exception("Server error: ${response.status}")
        }
        return response.body<QuotesResponse>().quotes   // JSON -> QuotesResponse -> list
    }
}