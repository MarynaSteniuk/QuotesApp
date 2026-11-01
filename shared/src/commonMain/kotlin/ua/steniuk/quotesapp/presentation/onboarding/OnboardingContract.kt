package ua.steniuk.quotesapp.presentation.onboarding

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import ua.steniuk.quotesapp.resources.*

data class OnboardingPage(
    val image: DrawableResource,
    val chipEmoji: String,
    val badgeEmoji: String,
    val chip: StringResource,
    val title: StringResource,
    val titleAccent: StringResource,
    val description: StringResource,
    val quote: StringResource,
)

private val onboardingPages = listOf(
    OnboardingPage(
        image = Res.drawable.onboarding_1,
        chipEmoji = "✨",
        badgeEmoji = "🌸",
        chip = Res.string.onboarding_chip_1,
        title = Res.string.onboarding_title_1,
        titleAccent = Res.string.onboarding_accent_1,
        description = Res.string.onboarding_desc_1,
        quote = Res.string.onboarding_quote_1,
    ),
    OnboardingPage(
        image = Res.drawable.onboarding_2,
        chipEmoji = "🔔",
        badgeEmoji = "🌙",
        chip = Res.string.onboarding_chip_2,
        title = Res.string.onboarding_title_2,
        titleAccent = Res.string.onboarding_accent_2,
        description = Res.string.onboarding_desc_2,
        quote = Res.string.onboarding_quote_2,
    ),
    OnboardingPage(
        image = Res.drawable.onboarding_3,
        chipEmoji = "🌸",
        badgeEmoji = "💗",
        chip = Res.string.onboarding_chip_3,
        title = Res.string.onboarding_title_3,
        titleAccent = Res.string.onboarding_accent_3,
        description = Res.string.onboarding_desc_3,
        quote = Res.string.onboarding_quote_3,
    ),
    OnboardingPage(
        image = Res.drawable.onboarding_4,
        chipEmoji = "💗",
        badgeEmoji = "🦋",
        chip = Res.string.onboarding_chip_4,
        title = Res.string.onboarding_title_4,
        titleAccent = Res.string.onboarding_accent_4,
        description = Res.string.onboarding_desc_4,
        quote = Res.string.onboarding_quote_4,
    ),
)

data class OnboardingState(
    val pages: List<OnboardingPage> = onboardingPages,
    val currentPage: Int = 0,
) {
    val isLastPage: Boolean get() = currentPage == pages.lastIndex
}

sealed class OnboardingAction {
    data class OnPageChanged(val page: Int) : OnboardingAction()
    data object OnSkipClick : OnboardingAction()
    data object OnGetStartedClick : OnboardingAction()
}

sealed class OnboardingEvent {
    data object NavigateToAuth : OnboardingEvent()
}