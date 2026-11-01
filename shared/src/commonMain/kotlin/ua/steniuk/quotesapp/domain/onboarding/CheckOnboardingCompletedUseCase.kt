package ua.steniuk.quotesapp.domain.onboarding

class CheckOnboardingCompletedUseCase(
    private val repository: OnboardingRepository
) {
    operator fun invoke(): Boolean = repository.isOnboardingCompleted()
}