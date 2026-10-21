package com.example.quote

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform