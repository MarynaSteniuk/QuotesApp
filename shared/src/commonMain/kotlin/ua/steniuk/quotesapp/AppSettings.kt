package ua.steniuk.quotesapp

import co.touchlab.kermit.Logger
import com.russhwolf.settings.Settings

object AppSettings {
    private val settings: Settings = Settings()
    private val log = Logger.withTag("AppSettings")
    private const val KEY_ONBOARDING_DONE = "onboarding_done"

    var isOnboardingDone: Boolean
        get() = settings.getBoolean(KEY_ONBOARDING_DONE, false)
        set(value) {
            settings.putBoolean(KEY_ONBOARDING_DONE, value)
            log.i { "onboarding_done = $value" }
        }
}