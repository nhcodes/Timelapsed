package codes.nh.timelapsed.popup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay

@Composable
fun rememberPopupMessageState(): PopupMessageState {
    return remember { PopupMessageState() }
}

class PopupMessageState {

    private val activeMessage = mutableStateOf(null as String?)

    suspend fun showMessage(message: String, time: Long = 3000L) {
        activeMessage.value = message
        delay(time)
        if (getMessage() === message) {
            hideMessage()
        }
    }

    fun hideMessage() {
        activeMessage.value = null
    }

    fun getMessage(): String? {
        return activeMessage.value
    }

}