package com.example.calendarbyourselvesdacs3.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

)


sealed interface AppColors {
    val calendarBackground: Color
    val calendarContent: Color
    val calendarContentIsToday: Color
    val outOfMonthBackground: Color
    val inMonthBackground: Color
    val eventCountBackground: Color
    val eventCountForeground: Color
    val currentDayBackground: Color
    val textColorIsCurrentDay: Color
    val circleBoxAtHomeScreen: Color
    val dividerColor: Color

    data object Light : AppColors {
        override val calendarContent = Color(0xFF4E4E4E)
        override val calendarContentIsToday = Color.White
        override val calendarBackground = Color(0xFFDFDFDF)
        override val outOfMonthBackground = Color(0xFFF1F1F1)
        override val currentDayBackground = Color(0xFFF4EBFC)
        override val inMonthBackground = Color.White
        override val eventCountBackground = Color(0xFFFF3C3C)
        override val eventCountForeground = Color(0xFFFFFFFF)
        override val textColorIsCurrentDay = Color.Black
        override val circleBoxAtHomeScreen = Color.Black
        override val dividerColor = Color.Black
    }

    data object Dark : AppColors {
        override val calendarContent = Color.White
        override val calendarContentIsToday = Color.Black
        override val calendarBackground = Color(0xFF111111)
        override val outOfMonthBackground = Color.Black
        override val currentDayBackground = Color(0xFFf3f6f4)
        override val inMonthBackground = Color.Black
        override val eventCountBackground = Color(0xFFFF3C3C)
        override val eventCountForeground = Color(0xFFFFFFFF)
        override val textColorIsCurrentDay = Color.White
        override val circleBoxAtHomeScreen = Color.LightGray
        override val dividerColor = Color.LightGray
    }
}


val LocalAppColors = compositionLocalOf<AppColors> { AppColors.Dark }

@Composable
fun CalendarByOurselvesDACS3Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
    val (colorScheme, appColors) = when {
        darkTheme -> DarkColorScheme to AppColors.Dark
        else -> LightColorScheme to AppColors.Light
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }






    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}