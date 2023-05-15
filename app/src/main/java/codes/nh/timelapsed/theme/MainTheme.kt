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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/*
https://google.github.io/accompanist/systemuicontroller/
*/

//color

private val darkColorScheme = darkColorScheme(

)

private val lightColorScheme = lightColorScheme(

)

//typography

private val typography = Typography()

//theme

const val SYSTEM_BARS_ELEVATION = 2

@Composable
fun MainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
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