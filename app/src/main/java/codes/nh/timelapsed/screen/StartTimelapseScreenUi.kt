package codes.nh.timelapsed.screen

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import codes.nh.timelapsed.camera.CameraManager
import codes.nh.timelapsed.picker.Picker
import codes.nh.timelapsed.picker.PickerState
import codes.nh.timelapsed.picker.rememberPickerState
import codes.nh.timelapsed.utils.log

@Composable
fun StartTimelapseScreen(onStart: (Long) -> Unit) {
    val screenType = ScreenType.START_TIMELAPSE
    Screen(screenType = screenType) {
        Box {

            val activity = LocalContext.current as ComponentActivity
            val camera = remember { CameraManager(activity) }

            DisposableEffect(null) { //todo

                log("starting camera")
                camera.startCamera()

                onDispose {
                    log("stopping camera")
                    camera.stopCamera()
                }

            }

            AndroidView(
                factory = { camera.previewView },
                modifier = Modifier.fillMaxSize()
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {

                Text(
                    text = "Select an interval, position your phone and click start.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 32.dp)
                )

                Text(
                    text = "Take a picture every",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )

                val valuesPickerState = rememberPickerState()
                val unitsPickerState = rememberPickerState()

                val units = remember {
                    linkedMapOf(
                        "seconds" to 1L,
                        "minutes" to 60L,
                        "hours" to 3600L,
                        "days" to 86400L,
                    )
                }

                IntervalSelector(
                    valuesPickerState = valuesPickerState,
                    unitsPickerState = unitsPickerState,
                    values = (1..99).map { it.toString() },
                    defaultValue = 0, //1
                    units = units.keys.toList(),
                    defaultUnit = 1, //minutes
                    modifier = Modifier.fillMaxWidth()
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val intervalSeconds = (valuesPickerState.selectedItem.toLongOrNull() ?: 0) *
                            (units[unitsPickerState.selectedItem] ?: 0)
                    if (intervalSeconds < 5L) {

                        Text(
                            text = "The interval needs to be at least 5 seconds",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )

                    } else {

                        OutlinedButton(
                            onClick = { onStart(intervalSeconds) },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = screenType.action)
                        }

                    }
                }

            }

        }
    }
}

@Composable
private fun IntervalSelector(
    valuesPickerState: PickerState,
    unitsPickerState: PickerState,
    values: List<String>,
    defaultValue: Int,
    units: List<String>,
    defaultUnit: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Picker(
            state = valuesPickerState,
            items = values,
            visibleItemsCount = 3,
            startIndex = defaultValue,
            modifier = Modifier.weight(0.3f),
            textModifier = Modifier.padding(8.dp),
            textStyle = TextStyle(fontSize = 24.sp)
        )
        Picker(
            state = unitsPickerState,
            items = units,
            visibleItemsCount = 3,
            startIndex = defaultUnit,
            modifier = Modifier.weight(0.7f),
            textModifier = Modifier.padding(8.dp),
            textStyle = TextStyle(fontSize = 24.sp)
        )
    }
}

/*
@Composable
private fun IntervalSelector(
    onSelect: (Long) -> Unit,
    modifier: Modifier = Modifier,
    values: IntRange = 1..99,
    defaultValue: Int = 1, // 1
    units: Array<Pair<String, Long>> = arrayOf(
        "seconds" to 1L,
        "minutes" to 60L,
        "hours" to 3600L,
        "days" to 86400L,
    ),
    defaultUnit: Int = 1 // minutes
) {

    var currentValue = remember { defaultValue }
    var currentUnit = remember { defaultUnit }

    Row(modifier = modifier) {

        AndroidView(
            factory = { context ->
                val numberPicker = NumberPicker(context)
                numberPicker.minValue = values.first
                numberPicker.maxValue = values.last
                numberPicker.value = currentValue
                numberPicker.setFormatter { number -> String.format("%02d", number) }
                numberPicker.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
                numberPicker.setOnValueChangedListener { _, _, newValue ->
                    currentValue = newValue
                    onSelect(currentValue * units[currentUnit].second)
                }
                numberPicker
            },
            modifier = Modifier.weight(0.3f)
        )

        AndroidView(
            factory = { context ->
                val numberPicker = NumberPicker(context)
                numberPicker.minValue = 0
                numberPicker.maxValue = units.size - 1
                numberPicker.value = currentUnit
                numberPicker.displayedValues = units.map { it.first }.toTypedArray()
                numberPicker.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
                numberPicker.setOnValueChangedListener { _, _, newUnit ->
                    currentUnit = newUnit
                    onSelect(currentValue * units[currentUnit].second)
                }
                numberPicker
            },
            modifier = Modifier.weight(0.7f)
        )

    }
}
*/

@Preview
@Composable
private fun StartTimelapseScreenPreview() { //todo fix if possible
    StartTimelapseScreen(onStart = {})
}