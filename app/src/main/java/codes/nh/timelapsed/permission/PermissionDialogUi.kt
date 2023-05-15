package codes.nh.timelapsed.permission

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import codes.nh.timelapsed.R
import codes.nh.timelapsed.utils.closeAppWithErrorMessage
import codes.nh.timelapsed.utils.getTestPermissions

@Composable
fun PermissionDialog() {

    val activity = LocalContext.current as ComponentActivity
    val permissionManager = remember(activity) { PermissionManager(activity) }

    val missingPermissions = permissionManager.getMissingPermissions()
    if (missingPermissions.isNotEmpty()) {
        PermissionDialog(
            missingPermissions = missingPermissions,
            onClickDeny = { activity.closeAppWithErrorMessage("The app does not work without these permissions") },
            onClickAccept = { permissionManager.requestPermissions() }
        )
    }

}

@Composable
private fun PermissionDialog(
    missingPermissions: List<Permission>,
    onClickDeny: () -> Unit,
    onClickAccept: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        icon = {
            Icon(
                painter = painterResource(R.drawable.icon_permission),
                contentDescription = "icon_permission"
            )
        },
        title = { Text(text = "Grant permissions") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {

                Text(text = "The app needs the following permissions in order to work:")
                missingPermissions.forEach { permission ->
                    Text(
                        text = permission.name.removePrefix("android.permission."),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    Text(
                        text = permission.description,
                        modifier = Modifier.alpha(0.5f)
                    )
                }

            }
        },
        dismissButton = {
            Button(onClick = onClickDeny) {
                Text(text = "Deny")
            }
        },
        confirmButton = {
            Button(onClick = onClickAccept) {
                Text(text = "Accept")
            }
        }
    )
}

@Preview
@Composable
private fun PermissionDialogPreview() {
    PermissionDialog(
        missingPermissions = getTestPermissions(),
        onClickDeny = {},
        onClickAccept = {}
    )
}