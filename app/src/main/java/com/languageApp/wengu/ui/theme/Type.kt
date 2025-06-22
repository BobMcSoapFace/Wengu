package com.languageApp.wengu.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
val moduleLabel = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Normal,
    textAlign = TextAlign.Center,
    fontSize = 16.sp,
)
val moduleLabelBold = moduleLabel + TextStyle(
    fontWeight = FontWeight.Bold,
)
val titleLabel = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.SemiBold,
    textAlign = TextAlign.Left,
    fontSize = 38.sp,
)
val descriptionText = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    textAlign = TextAlign.Left,
    fontSize = 12.sp,
)