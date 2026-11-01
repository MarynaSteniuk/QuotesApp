package ua.steniuk.quotesapp.presentation.onboarding

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import ua.steniuk.quotesapp.domain.onboarding.SaveOnboardingCompletedUseCase

class OnboardingViewModel(
    private val saveOnboardingCompletedUseCase: SaveOnboardingCompletedUseCase
) : ViewModel() {

    private val log = Logger.withTag("Onboarding")

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    private val _events = Channel<OnboardingEvent>(capacity = Channel.BUFFERED)
    val events: Flow<OnboardingEvent> = _events.receiveAsFlow()

    fun onAction(action: OnboardingAction) {
        when (action) {
            is OnboardingAction.OnPageChanged -> onPageChanged(action.page)
            OnboardingAction.OnSkipClick,
            OnboardingAction.OnGetStartedClick -> completeOnboarding()
        }
    }

    private fun onPageChanged(page: Int) {
        log.i { "Current page: $page" }
        _state.update { it.copy(currentPage = page) }
    }

    private fun completeOnboarding() {
        saveOnboardingCompletedUseCase()
        log.i { "Onboarding completed" }
        _events.trySend(OnboardingEvent.NavigateToAuth)
    }
}