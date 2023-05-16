package codes.nh.timelapsed.popup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PopupMessage(
    state: PopupMessageState,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = state.getMessage() != null,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
        modifier = modifier
    ) {

        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .draggable(
                    state = rememberDraggableState(
                        onDelta = { px -> if (px > 15) state.hideMessage() }
                    ),
                    orientation = Orientation.Vertical
                )
        ) {
            Text(
                text = state.getMessage() ?: "",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }

    }
}