package codes.nh.timelapsed.permission

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import codes.nh.timelapsed.utils.log
import codes.nh.timelapsed.utils.registerActivityResultLauncher


/*
https://developer.android.com/training/scheduling/alarms
https://developer.android.com/guide/components/activities/background-starts
https://developer.android.com/training/permissions/requesting
*/
class PermissionManager(private val activity: ComponentActivity) {

    init {
        log("init PermissionManager")
    }

    private val allPermissions = arrayOf(
        Permission(
            name = android.Manifest.permission.CAMERA,
            description = "To take pictures",
        ),
        Permission(
            name = android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            description = "To save pictures",
            required = { it < Build.VERSION_CODES.R }
        ),
        SpecialPermission(
            name = android.Manifest.permission.SCHEDULE_EXACT_ALARM,
            description = "To take pictures at a specific interval",
            required = { it >= Build.VERSION_CODES.S },
            checkGranted = { hasExactAlarmPermission() },
            requestAction = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
        ),
        SpecialPermission(
            name = android.Manifest.permission.SYSTEM_ALERT_WINDOW,
            description = "To open the camera from the background",
            required = { it >= Build.VERSION_CODES.Q },
            checkGranted = { hasOverlayPermission() },
            requestAction = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
        )
    )

    private val missingPermissionsState = mutableStateOf(checkMissingPermissions())

    fun getMissingPermissions() = missingPermissionsState.value

    private fun checkMissingPermissions(): List<Permission> {
        return allPermissions.filter { permission -> !permission.isGranted(activity) }
    }

    fun requestPermissions() {
        val missingPermissions = getMissingPermissions()

        val missingNormalPermissions = missingPermissions.filter { it !is SpecialPermission }
        if (missingNormalPermissions.isNotEmpty()) {
            val requestPermissions = missingNormalPermissions.map { it.name }.toTypedArray()
            registerPermissionsRequester(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ).launch(requestPermissions)
            return
        }

        val missingSpecialPermissions = missingPermissions.mapNotNull { it as? SpecialPermission }
        if (missingSpecialPermissions.isNotEmpty()) {
            val requestIntent = missingSpecialPermissions.first().getRequestIntent(activity)
            registerPermissionsRequester(
                contract = ActivityResultContracts.StartActivityForResult()
            ).launch(requestIntent)
        }
    }

    //requester

    private var currentPermissionsRequester: ActivityResultLauncher<*>? = null

    private fun <I, O> registerPermissionsRequester(contract: ActivityResultContract<I, O>): ActivityResultLauncher<I> {
        val requester = activity.registerActivityResultLauncher(
            contract = contract,
            callback = { result ->
                //to avoid bugs with the result, just check again if all the permissions are granted
                missingPermissionsState.value = checkMissingPermissions()
                unregisterPermissionsRequester()
            }
        )
        currentPermissionsRequester = requester
        return requester
    }

    private fun unregisterPermissionsRequester() {
        currentPermissionsRequester?.unregister()
        currentPermissionsRequester = null
    }

    //special permissions

    private fun hasExactAlarmPermission(): Boolean {
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return alarmManager.canScheduleExactAlarms()
    }

    private fun hasOverlayPermission(): Boolean {
        return Settings.canDrawOverlays(activity)
    }

}