package codes.nh.timelapsed.utils

import android.Manifest
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import codes.nh.timelapsed.permission.Permission
import codes.nh.timelapsed.timelapse.Timelapse
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

fun log(message: Any?) {
    Log.e("Timelapsed", "$message")
}

fun getTimeString(
    timestamp: Long = System.currentTimeMillis(),
    format: String = "dd/MM/yyyy HH:mm:ss"
): String {
    val dateFormat = SimpleDateFormat(format)
    val date = Date(timestamp)
    return dateFormat.format(date)
}

/*
activity.registerForActivityResult -> Exception:
LifecycleOwner is attempting to register while current state is RESUMED.
LifecycleOwners must call register before they are STARTED.
*/
fun <I, O> ComponentActivity.registerActivityResultLauncher(
    contract: ActivityResultContract<I, O>,
    callback: ActivityResultCallback<O>
): ActivityResultLauncher<I> {
    val key = UUID.randomUUID().toString()
    return activityResultRegistry.register(key, contract, callback)
}

fun Activity.closeAppWithErrorMessage(message: String) {
    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    finish()
}

fun getTestTimelapses(): List<Timelapse> {
    return listOf(
        Timelapse(File("path", "test"), emptyList()),
        Timelapse(File("path", "some longer timelapse name"), emptyList()),
    )
}

fun getTestPermissions(): List<Permission> {
    return listOf(
        Permission(
            Manifest.permission.CAMERA,
            "Necessary to take pictures"
        ),
        Permission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            "Lorem ipsum this is a test with a longer description lorem ipsum aaaaaaaaaaaaaa"
        ),
    )
}

//https://developer.android.com/jetpack/compose/side-effects#disposableeffect
@Composable
fun LifecycleListener(onChange: (Lifecycle.Event) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            onChange(event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}