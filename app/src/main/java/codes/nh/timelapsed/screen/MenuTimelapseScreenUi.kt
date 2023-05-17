package codes.nh.timelapsed.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import codes.nh.timelapsed.timelapse.Timelapse
import codes.nh.timelapsed.utils.getTestTimelapses

@Composable
fun MenuTimelapseScreen(
    timelapse: Timelapse,
    onClickScreen: (ScreenType) -> Unit
) {
    val screenType = ScreenType.MENU_TIMELAPSE
    Screen(screenType = screenType) {

        Text(
            text = "What to do with timelapse ${timelapse.name}?",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )

        //Divider(modifier = Modifier.padding(8.dp))

        val screenTypes = remember {
            arrayOf(
                ScreenType.START_TIMELAPSE,
                ScreenType.PREVIEW_TIMELAPSE,
                ScreenType.EXPORT_TIMELAPSE,
                ScreenType.DELETE_TIMELAPSE
            )
        }

        NavigationBar(tonalElevation = 0.dp) {
            screenTypes.forEach { type ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = type.iconId),
                            contentDescription = type.action
                        )
                    },
                    label = { Text(text = type.action) },
                    selected = false,
                    onClick = { onClickScreen(type) }
                )
            }
        }

    }
}

@Preview
@Composable
private fun MenuTimelapseScreenPreview() {
    MenuTimelapseScreen(timelapse = getTestTimelapses().first(), onClickScreen = {})
}