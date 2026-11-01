package ua.steniuk.quotesapp.presentation.main

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ua.steniuk.quotesapp.domain.onboarding.CheckOnboardingCompletedUseCase
import ua.steniuk.quotesapp.navigation.Route

data class MainState(
    val startRoute: Route? = null
)

class MainViewModel(
    private val checkOnboardingCompletedUseCase: CheckOnboardingCompletedUseCase
) : ViewModel() {

    private val log = Logger.withTag("Main")

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        checkOnboarding()
    }

    private fun checkOnboarding() {
        val isCompleted = checkOnboardingCompletedUseCase()
        log.i { "Onboarding completed: $isCompleted" }
        _state.update {
            it.copy(startRoute = if (isCompleted) Route.Auth else Route.Onboarding)
        }
    }
}