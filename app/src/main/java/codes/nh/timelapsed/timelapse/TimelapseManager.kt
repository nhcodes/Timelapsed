package codes.nh.timelapsed.timelapse

import android.content.Context
import android.os.Environment
import codes.nh.timelapsed.R
import codes.nh.timelapsed.utils.getTimeString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class TimelapseManager(context: Context) {

    private val dispatcher = Dispatchers.IO

    private val picturesDirectory =
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: context.filesDir
    private val timelapsesDirectory = File(picturesDirectory, "timelapses")

    suspend fun getTimelapses() = withContext(dispatcher) {
        timelapsesDirectory
            .listFiles()
            ?.filter { file -> file.isDirectory }
            ?.map { directory -> getTimelapseFromDirectory(directory) }
            ?.sortedByDescending { timelapse -> timelapse.entries.firstOrNull()?.name }
            ?: emptyList()
    }

    private suspend fun getTimelapseFromDirectory(directory: File) = withContext(dispatcher) {
        val entries = directory
            .listFiles()
            ?.filter { file -> file.isFile }
            ?.map { file -> TimelapseEntry(file) }
            ?.sortedByDescending { entry -> entry.name }
            ?: emptyList()
        Timelapse(directory, entries)
    }

    private fun getTimelapseDirectoryFromName(name: String) = File(timelapsesDirectory, name)

    fun getTimelapseEntryOutputFileFromName(name: String): File {
        val timelapseDirectory = getTimelapseDirectoryFromName(name)
        val outputFileName = "${getTimeString(format = "yyyy_MM_dd_HH_mm_ss")}.jpg"
        return File(timelapseDirectory, outputFileName)
    }

    suspend fun createTimelapse(name: String) = withContext(dispatcher) {
        val timelapseDirectory = getTimelapseDirectoryFromName(name)
        if (timelapseDirectory.exists()) return@withContext null
        if (!timelapseDirectory.mkdirs()) return@withContext null
        getTimelapseFromDirectory(timelapseDirectory)
    }

    suspend fun deleteTimelapse(timelapse: Timelapse) = withContext(dispatcher) {
        val timelapseDirectory = timelapse.directory
        if (!timelapseDirectory.exists()) return@withContext false
        timelapseDirectory.deleteRecursively()
    }

}