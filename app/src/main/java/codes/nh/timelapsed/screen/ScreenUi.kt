package codes.nh.timelapsed.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(
    screenType: ScreenType,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    /*val coroutineScope = rememberCoroutineScope()

    fun hideBottomSheet() {
        coroutineScope
            .launch { sheetState.hide() }
            .invokeOnCompletion { mainViewModel.closeScreen() }
    }*/

    ModalBottomSheet(sheetState = sheetState, dragHandle = null, onDismissRequest = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = screenType.iconId),
                    contentDescription = screenType.title
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = screenType.title)
            }

            Divider()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                content()

            }

        }
    }
}