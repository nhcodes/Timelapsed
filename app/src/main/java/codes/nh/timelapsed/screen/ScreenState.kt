package codes.nh.timelapsed.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun rememberScreenState(): ScreenState {
    return remember { ScreenState() }
}

@OptIn(ExperimentalMaterial3Api::class)
class ScreenState {

    val sheetState = SheetState(
        skipPartiallyExpanded = true,
        initialValue = SheetValue.Hidden
    )

    private val activeScreen = mutableStateOf(null as Screen?)

    suspend fun openScreen(screen: Screen) {
        closeScreen()
        activeScreen.value = screen
        sheetState.show()
    }

    suspend fun closeScreen() {
        if (getScreen() == null) return
        sheetState.hide()
        activeScreen.value = null
    }

    fun updateClosedScreen() {
        activeScreen.value = null
    }

    fun getScreen(): Screen? {
        return activeScreen.value
    }

}