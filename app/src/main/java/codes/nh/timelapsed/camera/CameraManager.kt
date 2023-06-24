package codes.nh.timelapsed.camera

import android.util.Size
import androidx.activity.ComponentActivity
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import java.io.File

/*
https://developer.android.com/reference/kotlin/androidx/camera/view/LifecycleCameraController
*/
class CameraManager(private val activity: ComponentActivity) {

    val previewView = PreviewView(activity)

    private val controller = LifecycleCameraController(activity)

    fun startCamera(resolution: Int? = null, readyListener: () -> Unit = {}) {
        //controller.imageCaptureMode = ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY

        if (resolution != null) {
            val outputSize = CameraController.OutputSize(Size(resolution, resolution)) //todo
            controller.previewTargetSize = outputSize
            controller.imageCaptureTargetSize = outputSize
        }

        controller.bindToLifecycle(activity)
        previewView.controller = controller

        /*val executor = ContextCompat.getMainExecutor(context)
        Controller.initializationFuture.addListener(readyListener, executor)*/

        previewView.previewStreamState.observe(activity) { state ->
            if (state == PreviewView.StreamState.STREAMING) readyListener()
        }
    }

    fun stopCamera() {
        controller.unbind()
    }

    fun takePicture(outputFile: File, callback: (Boolean, String) -> Unit) {
        val output = ImageCapture.OutputFileOptions.Builder(outputFile).build()
        val executor = ContextCompat.getMainExecutor(activity)
        controller.takePicture(output, executor, object : ImageCapture.OnImageSavedCallback {

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                callback(true, outputFileResults.savedUri.toString())
            }

            override fun onError(exception: ImageCaptureException) {
                callback(false, exception.imageCaptureError.toString())
            }

        })
    }
}