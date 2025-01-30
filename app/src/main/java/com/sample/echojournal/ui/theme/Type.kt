package com.sample.echojournal.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.GoogleFont

import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.unit.sp
import com.sample.echojournal.R


val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val InterFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Inter"),
        fontProvider = provider,
    )
)

val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = InterFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = InterFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = InterFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = InterFontFamily, fontSize = 26.sp, lineHeight = 32.sp),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = InterFontFamily, fontSize = 22.sp, lineHeight = 26.sp),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = InterFontFamily, fontSize = 16.sp, lineHeight = 24.sp),
    titleLarge = baseline.titleLarge.copy(fontFamily = InterFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = InterFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = InterFontFamily, fontSize = 13.sp, lineHeight = 18.sp),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = InterFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = InterFontFamily, fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = baseline.bodySmall.copy(fontFamily = InterFontFamily, fontSize = 12.sp, lineHeight = 16.sp),
    labelLarge = baseline.labelLarge.copy(fontFamily = InterFontFamily, fontSize = 14.sp, lineHeight = 20.sp),
    labelMedium = baseline.labelMedium.copy(fontFamily = InterFontFamily, fontSize = 12.sp),
    labelSmall = baseline.labelSmall.copy(fontFamily = InterFontFamily),
)
