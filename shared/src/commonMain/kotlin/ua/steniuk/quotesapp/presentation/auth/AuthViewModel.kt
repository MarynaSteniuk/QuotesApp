package ua.steniuk.quotesapp.presentation.auth

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.StringResource
import ua.steniuk.quotesapp.resources.*

class AuthViewModel : ViewModel() {

    private val log = Logger.withTag("Auth")

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _events = Channel<AuthEvent>(capacity = Channel.BUFFERED)
    val events: Flow<AuthEvent> = _events.receiveAsFlow()

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.OnUsernameChange ->
                _state.update { it.copy(username = action.value, usernameError = null) }
            is AuthAction.OnPasswordChange ->
                _state.update { it.copy(password = action.value, passwordError = null) }
            AuthAction.OnSignInClick -> signIn()
        }
    }

    private fun signIn() {
        val current = _state.value
        val usernameError = validateUsername(current.username)
        val passwordError = validatePassword(current.password)

        _state.update { it.copy(usernameError = usernameError, passwordError = passwordError) }

        if (usernameError == null && passwordError == null) {
            log.i { "Sign in: ${current.username}" }
            _events.trySend(AuthEvent.NavigateToHome)
        } else {
            log.w { "Validation failed" }
        }
    }

    private fun validateUsername(username: String): StringResource? = when {
        username.isBlank() -> Res.string.error_username_empty
        username.trim().length < 3 -> Res.string.error_username_short
        else -> null
    }

    private fun validatePassword(password: String): StringResource? = when {
        password.isBlank() -> Res.string.error_password_empty
        password.length < 6 -> Res.string.error_password_short
        else -> null
    }
}