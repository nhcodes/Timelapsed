package codes.nh.timelapsed

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import codes.nh.timelapsed.screen.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    //screen

    private val activeScreen = mutableStateOf(null as Screen?)

    fun openScreen(screen: Screen) {
        activeScreen.value = screen
    }

    fun closeScreen() {
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