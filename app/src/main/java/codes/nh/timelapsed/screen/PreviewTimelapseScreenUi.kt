package codes.nh.timelapsed.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import codes.nh.timelapsed.timelapse.Timelapse
import codes.nh.timelapsed.utils.AsyncImage
import codes.nh.timelapsed.utils.getTestTimelapses

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

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(timelapse.entries) { entry ->

                AsyncImage(
                    file = entry.file,
                    contentDescription = entry.file.name,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )

            }
        }

    }
}

@Preview
@Composable
private fun PreviewTimelapseScreenPreview() {
    PreviewTimelapseScreen(timelapse = getTestTimelapses().first())
}