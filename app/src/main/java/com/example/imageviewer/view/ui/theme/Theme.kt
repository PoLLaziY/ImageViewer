package com.example.imageviewer.view.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = DarkPrimaryGray,
    onPrimary = SurfaceGray,
    secondary = DarkOrange,
    onSecondary = SurfaceGray,
    surface = DarkSurfaceGray,
    background = DarkSurfaceGray
)

private val LightColorPalette = lightColors(
    primary = PrimaryGray,
    onPrimary = White,
    secondary = Orange,
    onSecondary = White,
    surface = SurfaceGray,
    background = SurfaceGray

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ImageViewerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {

    val primary: Color by animateColorAsState(
        targetValue = if (darkTheme) DarkColorPalette.primary
        else LightColorPalette.primary
    )
    val onPrimary: Color by animateColorAsState(
        targetValue = if (darkTheme) DarkColorPalette.onPrimary
        else LightColorPalette.onPrimary
    )
    val secondary by animateColorAsState(
        targetValue = if (darkTheme) DarkColorPalette.secondary
        else LightColorPalette.secondary
    )
    val onSecondary by animateColorAsState(
        targetValue = if (darkTheme) DarkColorPalette.onSecondary
        else LightColorPalette.onSecondary
    )
    val surface by animateColorAsState(
        targetValue = if (darkTheme) DarkColorPalette.surface
        else LightColorPalette.surface
    )
    val background by animateColorAsState(
        targetValue = if (darkTheme) DarkColorPalette.background
        else LightColorPalette.background
    )


    val colors = if (darkTheme) {
        DarkColorPalette.copy(
            primary = primary,
            onPrimary = onPrimary,
            secondary = secondary,
            onSecondary = onSecondary,
            surface = surface,
            background = background
        )
    } else {
        LightColorPalette.copy(
            primary = primary,
            onPrimary = onPrimary,
            secondary = secondary,
            onSecondary = onSecondary,
            surface = surface,
            background = background
        )
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}