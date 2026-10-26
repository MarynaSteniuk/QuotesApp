package ua.steniuk.quotesapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform