package ua.steniuk.quotesapp.data.onboarding

import com.russhwolf.settings.Settings
import ua.steniuk.quotesapp.domain.onboarding.OnboardingRepository

class AppOnboardingRepository(
    private val settings: Settings
) : OnboardingRepository {

    override fun isOnboardingCompleted(): Boolean =
        settings.getBoolean(KEY_ONBOARDING_COMPLETED, false)

    override fun setOnboardingCompleted() {
        settings.putBoolean(KEY_ONBOARDING_COMPLETED, true)
    }

    private companion object {
        const val KEY_ONBOARDING_COMPLETED = "onboarding_done"
    }
}