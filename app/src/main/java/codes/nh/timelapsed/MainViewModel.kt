package codes.nh.timelapsed

import android.app.Application
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import codes.nh.timelapsed.screen.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MainViewModel(application: Application) : AndroidViewModel(application) {

    //screen

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
        if(getScreen() == null) return
        sheetState.hide()
        activeScreen.value = null
    }

    fun updateClosedScreen() {
        activeScreen.value = null
    }

    fun getScreen(): Screen? {
        return activeScreen.value
    }

    //snackbar

    private val activePopupMessage = mutableStateOf(null as String?)

    fun showPopupMessage(message: String, time: Long = 2500L) {
        activePopupMessage.value = message
        viewModelScope.launch {
            delay(time)
            if (getPopupMessage() == message) {
                hidePopupMessage()
            }
        }
    }

    private fun hidePopupMessage() {
        activePopupMessage.value = null
    }

    fun getPopupMessage(): String? {
        return activePopupMessage.value
    }

}