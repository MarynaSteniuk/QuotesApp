package ua.steniuk.quotesapp.presentation.auth

import org.jetbrains.compose.resources.StringResource

data class AuthState(
    val username: String = "",
    val password: String = "",
    val usernameError: StringResource? = null,
    val passwordError: StringResource? = null,
)

sealed class AuthAction {
    data class OnUsernameChange(val value: String) : AuthAction()
    data class OnPasswordChange(val value: String) : AuthAction()
    data object OnSignInClick : AuthAction()
}

sealed class AuthEvent {
    data object NavigateToHome : AuthEvent()
}