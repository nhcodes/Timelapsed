package codes.nh.timelapsed.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/*
https://google.github.io/accompanist/systemuicontroller/
*/

//color

val primaryColor = Color(0xFFD4AF37)
val onPrimaryColor = Color(0xFF000000)
val missingColor = Color(0xFFFF0000)
val highlightColor = Color(0xFF00FF00)

private val darkColorScheme = darkColorScheme(
    primary = primaryColor,
    onPrimary = onPrimaryColor,
    primaryContainer = primaryColor,
    onPrimaryContainer = onPrimaryColor,
    inversePrimary = missingColor,
    secondary = primaryColor,
    onSecondary = onPrimaryColor,
    secondaryContainer = primaryColor,
    onSecondaryContainer = onPrimaryColor,
    tertiary = primaryColor,
    onTertiary = onPrimaryColor,
    tertiaryContainer = primaryColor,
    onTertiaryContainer = onPrimaryColor,
    background = Color(0xFF171717),
    //onBackground = missingColor,
    surface = Color(0xFF202124),
    //onSurface = missingColor,
    //surfaceVariant = missingColor,
    //onSurfaceVariant = missingColor,
    surfaceTint = Color.Transparent,
    inverseSurface = missingColor,
    inverseOnSurface = missingColor,
    error = missingColor,
    onError = missingColor,
    errorContainer = missingColor,
    onErrorContainer = missingColor,
    //outline = missingColor,
    //outlineVariant = missingColor,
    //scrim = missingColor,
)

private val lightColorScheme = lightColorScheme(
    primary = primaryColor,
    onPrimary = onPrimaryColor,
    primaryContainer = primaryColor,
    onPrimaryContainer = onPrimaryColor,
    inversePrimary = missingColor,
    secondary = primaryColor,
    onSecondary = onPrimaryColor,
    secondaryContainer = primaryColor,
    onSecondaryContainer = onPrimaryColor,
    tertiary = primaryColor,
    onTertiary = onPrimaryColor,
    tertiaryContainer = primaryColor,
    onTertiaryContainer = onPrimaryColor,
    //background = Color(0xFF171717),
    //onBackground = missingColor,
    //surface = Color(0xFF202124),
    //onSurface = missingColor,
    //surfaceVariant = missingColor,
    //onSurfaceVariant = missingColor,
    surfaceTint = Color.Transparent,
    inverseSurface = missingColor,
    inverseOnSurface = missingColor,
    error = missingColor,
    onError = missingColor,
    errorContainer = missingColor,
    onErrorContainer = missingColor,
    //outline = missingColor,
    //outlineVariant = missingColor,
    //scrim = missingColor,
)

//typography

private val typography = Typography()

//theme

const val SYSTEM_BARS_ELEVATION = 2

@Composable
fun MainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(systemUiController, darkTheme) {
        systemUiController.setSystemBarsColor(
            color = colorScheme.surfaceColorAtElevation(SYSTEM_BARS_ELEVATION.dp),
            /*darkIcons = !darkTheme,
            isNavigationBarContrastEnforced = false*/
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )

}