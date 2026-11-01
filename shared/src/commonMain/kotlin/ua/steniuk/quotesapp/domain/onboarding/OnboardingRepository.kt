package ua.steniuk.quotesapp.domain.onboarding

interface OnboardingRepository {
    fun isOnboardingCompleted(): Boolean
    fun setOnboardingCompleted()
}