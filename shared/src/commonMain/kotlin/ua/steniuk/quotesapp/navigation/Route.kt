package ua.steniuk.quotesapp.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Onboarding : Route

    @Serializable
    data object Auth : Route

    @Serializable
    data object Home : Route
}