package codes.nh.timelapsed.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PopupMessage(message: String?, modifier: Modifier = Modifier) {
    AnimatedVisibility(visible = message != null, modifier = modifier) {

        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = message ?: "",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }

    }
}