package ua.steniuk.quotesapp.di

import com.russhwolf.settings.Settings
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ua.steniuk.quotesapp.data.onboarding.AppOnboardingRepository
import ua.steniuk.quotesapp.domain.onboarding.CheckOnboardingCompletedUseCase
import ua.steniuk.quotesapp.domain.onboarding.OnboardingRepository
import ua.steniuk.quotesapp.domain.onboarding.SaveOnboardingCompletedUseCase
import ua.steniuk.quotesapp.presentation.auth.AuthViewModel // НОВЕ
import ua.steniuk.quotesapp.presentation.main.MainViewModel
import ua.steniuk.quotesapp.presentation.onboarding.OnboardingViewModel

val appModule = module {
    single<Settings> { Settings() }

    singleOf(::AppOnboardingRepository) { bind<OnboardingRepository>() }

    factoryOf(::SaveOnboardingCompletedUseCase)
    factoryOf(::CheckOnboardingCompletedUseCase)

    viewModelOf(::MainViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::AuthViewModel) // НОВЕ
}

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}