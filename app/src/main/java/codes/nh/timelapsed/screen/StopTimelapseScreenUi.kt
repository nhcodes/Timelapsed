package codes.nh.timelapsed.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StopTimelapseScreen(onStop: () -> Unit, onDismiss: () -> Unit) {
    val screenType = ScreenType.STOP_TIMELAPSE
    Screen(screenType = screenType, onDismiss = onDismiss) {

        Text(
            text = "Currently there is a timelapse running.",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )

        OutlinedButton(
            onClick = onStop,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = screenType.action)
        }

    }
}

@Preview
@Composable
private fun StopTimelapseScreenPreview() {
    StopTimelapseScreen(onStop = {}, onDismiss = {})
}