package ua.steniuk.quotesapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ua.steniuk.quotesapp.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "QuotesApp",
        ) {
            App()
        }
    }
}