package ua.steniuk.quotesapp

import androidx.compose.runtime.*
import co.touchlab.kermit.Logger
import ua.steniuk.quotesapp.theme.QuotesTheme

@Composable
fun App() {
    QuotesTheme {
        var showOnboarding by remember { mutableStateOf(!AppSettings.isOnboardingDone) }

        LaunchedEffect(Unit) {
            Logger.withTag("App").i { "App started, showOnboarding = $showOnboarding" }
        }

        if (showOnboarding) {
            OnboardingScreen(onFinish = {
                AppSettings.isOnboardingDone = true
                showOnboarding = false
            })
        } else {
            HomeScreen(onShowOnboarding = {
                AppSettings.isOnboardingDone = false
                showOnboarding = true
            })
        }
    }
}