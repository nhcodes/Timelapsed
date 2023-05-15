package codes.nh.timelapsed.timelapse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import codes.nh.timelapsed.utils.AsyncImage
import codes.nh.timelapsed.utils.getTestTimelapses

@Composable
fun TimelapseList(
    timelapseList: List<Timelapse>,
    isTimelapseListLoaded: Boolean,
    onClickTimelapse: (timelapse: Timelapse) -> Unit,
    modifier: Modifier = Modifier
) {

    if (!isTimelapseListLoaded) {
        Box(contentAlignment = Alignment.Center, modifier = modifier) {
            CircularProgressIndicator()
        }
        return
    }

    if (timelapseList.isEmpty()) {
        Box(contentAlignment = Alignment.Center, modifier = modifier.alpha(0.5f)) {
            Text(text = "No timelapses yet.", textAlign = TextAlign.Center)
        }
        return
    }

    LazyColumn(modifier = modifier) {
        items(timelapseList) { timelapse ->

            TimelapseCard(
                timelapse = timelapse,
                height = 100.dp,
                onClick = { onClickTimelapse(timelapse) },
                modifier = Modifier.padding(16.dp)
            )

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimelapseCard(
    timelapse: Timelapse,
    height: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(onClick = onClick, modifier = modifier.height(height)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {

            val lastEntry = timelapse.entries.firstOrNull()

            AsyncImage(
                file = lastEntry?.file,
                contentDescription = "thumbnail",
                contentScale = ContentScale.Crop,
                sampleSize = 4,
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxHeight()
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {

                Text(
                    text = timelapse.directory.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Pictures: ${timelapse.entries.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.alpha(0.5f)
                )

                val lastEntryName = lastEntry?.file?.name ?: "/"
                Text(
                    text = "Last: $lastEntryName",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.alpha(0.5f)
                )

            }

        }
    }
}

@Preview
@Composable
private fun TimelapseListPreview() {
    TimelapseList(
        timelapseList = getTestTimelapses(),
        isTimelapseListLoaded = true,
        onClickTimelapse = {}
    )
}