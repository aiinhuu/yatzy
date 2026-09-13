package com.example.yatzy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.yatzy.R // WICHTIG: Hier muss dein aktueller Projektname stehen!

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Roboto"),
        fontProvider = provider
    )
)

val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Montserrat"),
        fontProvider = provider,
        weight = FontWeight.W600
    )
)

val modakFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Modak"),
        fontProvider = provider,
        weight = FontWeight.ExtraBold
    )
)

// Standard Material 3 Werte als Basis
private val baseline = Typography()

// Hier weisen wir die neuen Schriftarten den Text-Styles zu
val Typography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = modakFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily)
)