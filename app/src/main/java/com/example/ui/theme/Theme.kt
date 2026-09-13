package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

val ApexFxColorScheme = darkColorScheme(
    primary = ApexPrimary,
    onPrimary = ApexOnPrimary,
    primaryContainer = ApexPrimaryContainer,
    onPrimaryContainer = ApexOnPrimaryContainer,
    secondary = ApexSecondary,
    onSecondary = ApexOnSecondary,
    secondaryContainer = ApexSecondaryContainer,
    onSecondaryContainer = ApexOnSecondaryContainer,
    tertiary = ApexTertiary,
    onTertiary = ApexOnTertiary,
    tertiaryContainer = ApexTertiaryContainer,
    onTertiaryContainer = ApexOnTertiaryContainer,
    error = ApexError,
    onError = ApexOnError,
    errorContainer = ApexErrorContainer,
    onErrorContainer = ApexOnErrorContainer,
    background = ApexSurface,
    onBackground = ApexOnSurface,
    surface = ApexSurface,
    onSurface = ApexOnSurface,
    surfaceVariant = ApexSurfaceHighest,
    onSurfaceVariant = ApexOnSurfaceVariant,
    outline = ApexOutline,
    outlineVariant = ApexOutlineVariant
)

@Composable
fun ApexFxTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ApexFxColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    ApexFxTheme(content = content)
}
