package ua.steniuk.quotesapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import ua.steniuk.quotesapp.presentation.auth.AuthScreen
import ua.steniuk.quotesapp.presentation.home.HomeScreen
import ua.steniuk.quotesapp.presentation.onboarding.OnboardingScreen

private val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Route.Onboarding::class, Route.Onboarding.serializer())
            subclass(Route.Auth::class, Route.Auth.serializer())
            subclass(Route.Home::class, Route.Home.serializer())
        }
    }
}

@Composable
fun AppNavigation(startRoute: Route) {
    val backStack = rememberNavBackStack(navConfig, startRoute)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Route.Onboarding> {
                OnboardingScreen(
                    onNavigateToAuth = {
                        backStack.add(Route.Auth)
                        backStack.remove(Route.Onboarding)
                    }
                )
            }
            entry<Route.Auth> {
                AuthScreen(
                    onNavigateToHome = {
                        backStack.add(Route.Home)
                        backStack.remove(Route.Auth)
                    }
                )
            }
            entry<Route.Home> {
                HomeScreen()
            }
        }
    )
}