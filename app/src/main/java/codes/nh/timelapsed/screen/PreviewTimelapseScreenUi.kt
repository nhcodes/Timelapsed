package codes.nh.timelapsed.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import codes.nh.timelapsed.image.AsyncImage
import codes.nh.timelapsed.timelapse.Timelapse
import codes.nh.timelapsed.utils.getTestTimelapses
import kotlin.math.roundToInt

@Composable
fun PreviewTimelapseScreen(timelapse: Timelapse) {
    val screenType = ScreenType.PREVIEW_TIMELAPSE
    Screen(screenType = screenType) {

        if (timelapse.entries.isEmpty()) {
            Text(
                text = "No pictures.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            return@Screen
        }

        Box {

            val sliderIndex = remember { mutableStateOf(0) }
            val valueRange = remember { 0f..(timelapse.entries.size - 1f) }

            val entry = timelapse.entries[sliderIndex.value]

            AsyncImage(
                file = entry.file,
                contentDescription = entry.name,
                contentScale = ContentScale.FillWidth,
                resetIfFileChanges = false,
                modifier = Modifier.fillMaxSize()
            )

            Slider(
                value = sliderIndex.value.toFloat(),
                onValueChange = { sliderIndex.value = it.roundToInt() },
                valueRange = valueRange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.BottomCenter)
            )

        }

        /*LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(timelapse.entries) { entry ->

                AsyncImage(
                    file = entry.file,
                    contentDescription = entry.name,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )

            }
        }*/

    }
}

@Preview
@Composable
private fun PreviewTimelapseScreenPreview() {
    PreviewTimelapseScreen(timelapse = getTestTimelapses().first())
}