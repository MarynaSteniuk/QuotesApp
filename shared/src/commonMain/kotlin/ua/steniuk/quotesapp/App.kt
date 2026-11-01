package ua.steniuk.quotesapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import ua.steniuk.quotesapp.navigation.AppNavigation
import ua.steniuk.quotesapp.presentation.main.MainViewModel
import ua.steniuk.quotesapp.theme.QuotesTheme

@Composable
fun App() {
    QuotesTheme {
        val viewModel: MainViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        state.startRoute?.let { startRoute ->
            AppNavigation(startRoute = startRoute)
        }
    }
}