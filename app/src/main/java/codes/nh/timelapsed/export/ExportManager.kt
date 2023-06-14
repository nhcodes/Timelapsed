package codes.nh.timelapsed.export

import android.os.Environment
import androidx.compose.runtime.mutableStateOf
import codes.nh.timelapsed.timelapse.Timelapse
import codes.nh.timelapsed.utils.getTimeString
import codes.nh.timelapsed.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


class ExportManager {

    private val dispatcher = Dispatchers.IO

    private val moviesDirectory =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)

    private val documentsDirectory =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

    val progressState = mutableStateOf(0f)

    suspend fun exportAsVideo(
        timelapse: Timelapse,
        videoWidth: Int ,
        videoHeight: Int ,
        videoFrameRate: Int,
    ): File? = withContext(dispatcher) {
        try {

            log("started exportAsVideo")
            val startTime = System.currentTimeMillis()
            progressState.value = 0f

            val images = timelapse.entries.map { it.file }

            moviesDirectory.mkdirs()
            val fileName = "timelapse_${getTimeString(format = "yyyy_MM_dd_HH_mm_ss")}.avi"
            val outputFile = File(moviesDirectory, fileName) //todo scan

            val mjpegGenerator =
                MJPEGGenerator(outputFile, videoWidth, videoHeight, videoFrameRate.toDouble(), images.size)
            images.forEachIndexed { i, file ->
                mjpegGenerator.addImage(file.readBytes())
                progressState.value = (i + 1f) / images.size
            }
            mjpegGenerator.finishAVI()

            val time = System.currentTimeMillis() - startTime
            log("finished in $time ms")

            return@withContext outputFile

        } catch (e: Exception) {
            log("error while generating video: ${e.message}")
        }
        return@withContext null
    }

    suspend fun exportAsZip(timelapse: Timelapse): File? = withContext(dispatcher) {
        try {

            log("started exportAsZip")
            val startTime = System.currentTimeMillis()
            progressState.value = 0f

            val images = timelapse.entries.map { it.file }

            documentsDirectory.mkdirs()
            val fileName = "timelapse_${getTimeString(format = "yyyy_MM_dd_HH_mm_ss")}.zip"
            val outputFile = File(documentsDirectory, fileName) //todo scan

            ZipOutputStream(outputFile.outputStream()).use { out ->
                images.sortedBy { it.name }.forEachIndexed { i, image ->
                    val zipEntry = ZipEntry(image.name)
                    out.putNextEntry(zipEntry)
                    image.inputStream().use { it.copyTo(out) }
                    progressState.value = (i + 1f) / images.size
                }
            }

            val time = System.currentTimeMillis() - startTime
            log("finished in $time ms")

            return@withContext outputFile

        } catch (e: Exception) {
            log("error while generating zip: ${e.message}")
        }
        return@withContext null
    }

}