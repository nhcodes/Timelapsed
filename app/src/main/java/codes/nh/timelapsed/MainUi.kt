package codes.nh.timelapsed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import codes.nh.timelapsed.permission.PermissionDialog
import codes.nh.timelapsed.screen.CreateTimelapseScreen
import codes.nh.timelapsed.screen.DeleteTimelapseScreen
import codes.nh.timelapsed.screen.MenuTimelapseScreen
import codes.nh.timelapsed.screen.PreviewTimelapseScreen
import codes.nh.timelapsed.screen.Screen
import codes.nh.timelapsed.screen.ScreenType
import codes.nh.timelapsed.screen.StartTimelapseScreen
import codes.nh.timelapsed.screen.StopTimelapseScreen
import codes.nh.timelapsed.theme.SYSTEM_BARS_ELEVATION
import codes.nh.timelapsed.timelapse.Timelapse
import codes.nh.timelapsed.timelapse.TimelapseList
import codes.nh.timelapsed.timelapse.TimelapseViewModel
import codes.nh.timelapsed.utils.PopupMessage
import codes.nh.timelapsed.utils.log
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(
    mainViewModel: MainViewModel = viewModel(),
    timelapseViewModel: TimelapseViewModel = viewModel()
) {
    Surface {
        Column(modifier = Modifier.fillMaxSize()) {

            TopAppBar()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                LaunchedEffect(timelapseViewModel) {
                    timelapseViewModel.loadTimelapseList()
                    log("loaded timelapses")
                }

                val coroutineScope = rememberCoroutineScope()

                TimelapseList(
                    timelapseList = timelapseViewModel.getTimelapseList(),
                    isTimelapseListLoaded = timelapseViewModel.isTimelapseListLoaded(),
                    onClickTimelapse = { timelapse ->
                        coroutineScope.launch {
                            mainViewModel.openScreen(Screen(ScreenType.MENU_TIMELAPSE, timelapse))
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                val screen = mainViewModel.getScreen()
                if (screen != null) {
                    ModalBottomSheet(
                        sheetState = mainViewModel.sheetState,
                        dragHandle = null,
                        onDismissRequest = { mainViewModel.updateClosedScreen() }
                    ) {
                        when (screen.type) {

                            ScreenType.MENU_TIMELAPSE -> {
                                val timelapse = screen.data as Timelapse
                                MenuTimelapseScreen(
                                    timelapse = timelapse,
                                    onClickScreen = { newScreenType ->
                                        coroutineScope.launch {
                                            mainViewModel.openScreen(
                                                Screen(newScreenType, timelapse)
                                            )
                                        }
                                    }
                                )
                            }

                            ScreenType.CREATE_TIMELAPSE -> {
                                CreateTimelapseScreen(
                                    onCreate = { name ->
                                        coroutineScope.launch { mainViewModel.closeScreen() }
                                        timelapseViewModel.createTimelapse(name)
                                        mainViewModel.showPopupMessage("Timelapse $name created")
                                    }
                                )
                            }

                            ScreenType.DELETE_TIMELAPSE -> {
                                val timelapse = screen.data as Timelapse
                                DeleteTimelapseScreen(
                                    timelapse = timelapse,
                                    onDelete = {
                                        coroutineScope.launch { mainViewModel.closeScreen() }
                                        timelapseViewModel.deleteTimelapse(timelapse)
                                        val name = timelapse.directory.name
                                        mainViewModel.showPopupMessage("Timelapse $name deleted")
                                    }
                                )
                            }

                            ScreenType.START_TIMELAPSE -> {
                                val timelapse = screen.data as Timelapse
                                StartTimelapseScreen(
                                    onStart = { interval ->
                                        coroutineScope.launch { mainViewModel.closeScreen() }
                                        timelapseViewModel.startTimelapse(timelapse, interval)
                                        val name = timelapse.directory.name
                                        mainViewModel.showPopupMessage("Timelapse $name started ($interval seconds)")
                                    }
                                )
                            }

                            ScreenType.STOP_TIMELAPSE -> {
                                StopTimelapseScreen(
                                    onStop = {
                                        coroutineScope.launch { mainViewModel.closeScreen() }
                                        timelapseViewModel.stopTimelapse()
                                        mainViewModel.showPopupMessage("Timelapse stopped")
                                    }
                                )
                            }

                            ScreenType.PREVIEW_TIMELAPSE -> {
                                val timelapse = screen.data as Timelapse
                                PreviewTimelapseScreen(
                                    timelapse = timelapse
                                )
                            }

                            ScreenType.EXPORT_TIMELAPSE -> {
                                Text("EXPORT_TIMELAPSE")
                                /*val timelapse = screen.data as Timelapse
                                ExportTimelapseScreen(
                                    timelapse = timelapse,
                                    onExport = { success ->
                                        mainViewModel.closeScreen()
                                        val name = timelapse.directory.name
                                        val message = if (success)
                                            "Timelapse $name successfully exported"
                                        else "Timelapse $name could not be exported"
                                        mainViewModel.showSnackbar(message)
                                    }
                                )*/
                            }

                        }
                    }
                }

                if (timelapseViewModel.isTimelapseStarted()) {
                    FloatingActionButton(
                        onClick = {
                            coroutineScope.launch {
                                mainViewModel.openScreen(Screen(ScreenType.STOP_TIMELAPSE))
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_stop),
                            contentDescription = "icon_stop"
                        )
                    }
                } else {
                    FloatingActionButton(
                        onClick = {
                            coroutineScope.launch {
                                mainViewModel.openScreen(Screen(ScreenType.CREATE_TIMELAPSE))
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_create),
                            contentDescription = "icon_create"
                        )
                    }
                }

                PermissionDialog()

                val popupMessage = mainViewModel.getPopupMessage()
                PopupMessage(
                    message = popupMessage,
                    onSwipeDown = { px -> if (px > 15) mainViewModel.hidePopupMessage() },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp)
                )

            }

        }
    }
}

@Composable
private fun TopAppBar() {
    Surface(tonalElevation = SYSTEM_BARS_ELEVATION.dp, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.app_name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}