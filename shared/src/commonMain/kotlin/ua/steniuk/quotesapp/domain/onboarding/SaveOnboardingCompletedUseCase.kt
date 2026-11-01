package ua.steniuk.quotesapp.domain.onboarding

class SaveOnboardingCompletedUseCase(
    private val repository: OnboardingRepository
) {
    operator fun invoke() {
        repository.setOnboardingCompleted()
    }
}