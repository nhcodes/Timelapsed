package codes.nh.timelapsed.camera

import android.os.Build
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
import android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import codes.nh.timelapsed.timelapse.TIMELAPSE_NAME_KEY
import codes.nh.timelapsed.timelapse.TimelapseManager
import codes.nh.timelapsed.utils.log


class CameraActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        log("CameraActivity onCreate()")
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true)
            setShowWhenLocked(true)
        } else {
            window.addFlags(FLAG_TURN_SCREEN_ON or FLAG_SHOW_WHEN_LOCKED)
        }

        val timelapseName = intent.extras?.getString(TIMELAPSE_NAME_KEY)!!
        val timelapseFileManager = TimelapseManager(applicationContext)
        val outputFile = timelapseFileManager.getTimelapseEntryOutputFileFromName(timelapseName)

        val cameraManager = CameraManager(this)
        cameraManager.startCamera {

            //Handler(Looper.getMainLooper()).postDelayed({}, 1000L)

            cameraManager.takePicture(outputFile) { success, message ->
                log("takePicture: success=$success message=$message")
                cameraManager.stopCamera()
                finishAndRemoveTask()
            }

        }

        setContent {

            BackHandler { }

            AndroidView(factory = { cameraManager.previewView }, modifier = Modifier.fillMaxSize())

        }

    }
}