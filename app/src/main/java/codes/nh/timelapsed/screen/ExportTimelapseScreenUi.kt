package codes.nh.timelapsed.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import codes.nh.timelapsed.export.ExportManager
import codes.nh.timelapsed.timelapse.Timelapse
import codes.nh.timelapsed.utils.getTestTimelapses
import kotlinx.coroutines.launch
import java.io.File


@Composable
fun ExportTimelapseScreen(timelapse: Timelapse, onExport: (File?) -> Unit) {
    val screenType = ScreenType.EXPORT_TIMELAPSE
    Screen(screenType = screenType) {

        if (timelapse.entries.isEmpty()) {
            Text(
                text = "No pictures.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            return@Screen
        }

        val coroutineScope = rememberCoroutineScope()

        val videoWidth = remember { mutableStateOf("500") }
        val videoHeight = remember { mutableStateOf("500") }
        val videoFrameRate = remember { mutableStateOf("30") }

        val exportManager = remember { ExportManager() }

        LinearProgressIndicator(
            progress = exportManager.progressState.value,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            label = { Text(text = "Enter video width") },
            value = videoWidth.value,
            onValueChange = { newValue -> videoWidth.value = newValue },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedTextField(
            label = { Text(text = "Enter video height") },
            value = videoHeight.value,
            onValueChange = { newValue -> videoHeight.value = newValue },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedTextField(
            label = { Text(text = "Enter video frame rate") },
            value = videoFrameRate.value,
            onValueChange = { newValue -> videoFrameRate.value = newValue },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedButton(
            onClick = {
                coroutineScope.launch {
                    val file = exportManager.exportAsVideo(
                        timelapse,
                        videoWidth.value.toInt(),
                        videoHeight.value.toInt(),
                        videoFrameRate.value.toInt()
                    )
                    onExport(file)
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Export as video")
        }

        Text(
            text = "or", modifier = Modifier
                .alpha(0.5f)
                .padding(8.dp)
        )

        OutlinedButton(
            onClick = {
                coroutineScope.launch {
                    val file = exportManager.exportAsZip(timelapse)
                    onExport(file)
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Export as zip")
        }

    }
}

@Preview
@Composable
private fun ExportTimelapseScreenPreview() {
    ExportTimelapseScreen(timelapse = getTestTimelapses().first(), onExport = {})
}