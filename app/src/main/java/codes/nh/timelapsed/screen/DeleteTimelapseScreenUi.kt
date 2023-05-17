package codes.nh.timelapsed.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import codes.nh.timelapsed.timelapse.Timelapse
import codes.nh.timelapsed.utils.getTestTimelapses

@Composable
fun DeleteTimelapseScreen(timelapse: Timelapse, onDelete: () -> Unit) {
    val screenType = ScreenType.DELETE_TIMELAPSE
    Screen(screenType = screenType) {

        Text(
            text = "Really delete timelapse ${timelapse.name} and all its pictures?",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )

        OutlinedButton(
            onClick = onDelete,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = screenType.action)
        }

    }
}

@Preview
@Composable
private fun DeleteTimelapseScreenPreview() {
    DeleteTimelapseScreen(timelapse = getTestTimelapses().first(), onDelete = {})
}