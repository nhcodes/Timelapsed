package codes.nh.timelapsed.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CreateTimelapseScreen(onCreate: (String) -> Unit) {
    val screenType = ScreenType.CREATE_TIMELAPSE
    Screen(screenType = screenType) {

        val focusManager = LocalFocusManager.current

        val timelapseName = remember { mutableStateOf("") }

        OutlinedTextField(
            label = { Text(text = "Enter a name") },
            value = timelapseName.value,
            onValueChange = { newValue ->
                timelapseName.value = newValue
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedButton(
            onClick = { onCreate(timelapseName.value) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = screenType.action)
        }

    }
}

@Preview
@Composable
private fun CreateTimelapseScreenPreview() {
    CreateTimelapseScreen(onCreate = {})
}