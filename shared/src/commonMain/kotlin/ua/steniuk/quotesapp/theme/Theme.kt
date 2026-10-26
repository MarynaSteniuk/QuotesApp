package ua.steniuk.quotesapp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import ua.steniuk.quotesapp.resources.*

@Composable
fun appTypography(): Typography {
    val comfortaa = FontFamily(
        Font(Res.font.comfortaa_regular, FontWeight.Normal),
        Font(Res.font.comfortaa_bold, FontWeight.Bold),
    )

    val nunitoSans = FontFamily(
        Font(Res.font.nunito_sans_regular, FontWeight.Normal),
    )

    val base = Typography()
    return base.copy(
        headlineMedium = base.headlineMedium.copy(fontFamily = comfortaa, fontWeight = FontWeight.Bold),
        titleLarge = base.titleLarge.copy(fontFamily = comfortaa, fontWeight = FontWeight.Bold),
        bodyLarge = base.bodyLarge.copy(fontFamily = nunitoSans),
        bodyMedium = base.bodyMedium.copy(fontFamily = nunitoSans),
        labelLarge = base.labelLarge.copy(fontFamily = comfortaa, fontWeight = FontWeight.Bold),
    )
}

@Composable
fun QuotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = appTypography(),
        content = content
    )
}