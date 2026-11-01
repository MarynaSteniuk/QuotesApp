package ua.steniuk.quotesapp.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ua.steniuk.quotesapp.presentation.common.ObserveEvents
import ua.steniuk.quotesapp.resources.*

@Composable
fun AuthScreen(
    onNavigateToHome: () -> Unit,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEvents(viewModel.events) { event ->
        when (event) {
            AuthEvent.NavigateToHome -> onNavigateToHome()
        }
    }

    AuthContent(state = state, onAction = viewModel::onAction)
}

@Composable
private fun AuthContent(
    state: AuthState,
    onAction: (AuthAction) -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    val focusManager = LocalFocusManager.current

    val signIn = {
        focusManager.clearFocus()
        onAction(AuthAction.OnSignInClick)
    }

    val title = stringResource(Res.string.auth_title)
    val accent = stringResource(Res.string.auth_accent)

    val backgroundBrush = Brush.verticalGradient(
        listOf(
            colors.background,
            colors.primaryContainer.copy(alpha = 0.12f),
            colors.tertiaryContainer.copy(alpha = 0.35f),
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        AuthIllustration()

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .clip(CircleShape)
                .background(colors.primaryContainer.copy(alpha = 0.18f))
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🌸", fontSize = 12.sp)
            Spacer(Modifier.width(6.dp))
            Text(
                text = stringResource(Res.string.auth_chip).uppercase(),
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp, letterSpacing = 1.2.sp),
                color = colors.primary
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = buildAnnotatedString {
                append(title)
                append(" ")
                withStyle(SpanStyle(color = colors.primaryContainer)) { append(accent) }
            },
            style = MaterialTheme.typography.headlineMedium,
            color = colors.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.auth_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = colors.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        val cardShape = RoundedCornerShape(28.dp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, cardShape, spotColor = colors.primary)
                .background(colors.surfaceContainerLowest, cardShape)
                .padding(20.dp)
        ) {
            FieldLabel(stringResource(Res.string.auth_username))
            AuthTextField(
                value = state.username,
                onValueChange = { onAction(AuthAction.OnUsernameChange(it)) },
                leadingEmoji = "👤",
                isError = state.usernameError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) }
                )
            )
            ErrorMessage(state.usernameError)

            Spacer(Modifier.height(16.dp))

            FieldLabel(stringResource(Res.string.auth_password))
            AuthTextField(
                value = state.password,
                onValueChange = { onAction(AuthAction.OnPasswordChange(it)) },
                leadingEmoji = "🔒",
                isError = state.passwordError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { signIn() }
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            ErrorMessage(state.passwordError)

            Spacer(Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(10.dp, CircleShape, spotColor = colors.primaryContainer)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(colors.primaryContainer, colors.secondaryContainer)
                        )
                    )
                    .clickable { signIn() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.auth_sign_in) + "  →",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp),
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.primaryContainer.copy(alpha = 0.12f), RoundedCornerShape(28.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(colors.surfaceContainerLowest, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("♡", color = colors.primary, fontSize = 18.sp)
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = stringResource(Res.string.auth_quote),
                style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                color = colors.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun AuthIllustration() {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = Modifier.size(160.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        listOf(colors.primaryContainer.copy(alpha = 0.35f), Color.Transparent)
                    ),
                    CircleShape
                )
        )
        Box(
            Modifier
                .size(116.dp)
                .shadow(16.dp, CircleShape, spotColor = colors.primary)
                .background(colors.surfaceContainerLowest, CircleShape)
                .padding(5.dp)
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(Res.drawable.auth_flower),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 22.dp, bottom = 22.dp)
                .size(38.dp)
                .shadow(6.dp, CircleShape, spotColor = colors.primary)
                .background(colors.surfaceContainerLowest, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("✨", fontSize = 18.sp)
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    leadingEmoji: String,
    isError: Boolean,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val colors = MaterialTheme.colorScheme
    val fieldBackground = colors.primaryContainer.copy(alpha = 0.08f)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = { Text(leadingEmoji, fontSize = 18.sp) },
        singleLine = true,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        shape = RoundedCornerShape(14.dp),
        textStyle = MaterialTheme.typography.bodyLarge,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = fieldBackground,
            unfocusedContainerColor = fieldBackground,
            errorContainerColor = colors.error.copy(alpha = 0.05f),
            focusedBorderColor = colors.primary,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = colors.primary,
        )
    )
}

@Composable
private fun ErrorMessage(error: StringResource?) {
    if (error != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .background(
                    MaterialTheme.colorScheme.error.copy(alpha = 0.08f),
                    RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("❗", fontSize = 12.sp)
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(error),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}