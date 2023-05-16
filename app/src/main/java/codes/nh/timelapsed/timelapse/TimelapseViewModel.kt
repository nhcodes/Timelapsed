package codes.nh.timelapsed.timelapse

import android.app.Application
import android.os.Bundle
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import codes.nh.timelapsed.camera.CameraActivity
import codes.nh.timelapsed.repeatingactivity.RepeatingActivity
import kotlinx.coroutines.launch

const val TIMELAPSE_NAME_KEY = "timelapseName"

class TimelapseViewModel(application: Application) : AndroidViewModel(application) {

    //timelapse files

    private val timelapseManager = TimelapseManager(application)

    private var timelapseListState = mutableStateListOf<Timelapse>()

    private var timelapseListLoadedState = mutableStateOf(false)

    fun loadTimelapseList() {
        viewModelScope.launch {
            timelapseListState.addAll(timelapseManager.getTimelapses())
            timelapseListLoadedState.value = true
        }
    }

    fun getTimelapseList() = timelapseListState.toList()

    fun isTimelapseListLoaded() = timelapseListLoadedState.value

    suspend fun createTimelapse(name: String): Boolean {
        val timelapse = timelapseManager.createTimelapse(name)
        if (timelapse == null) return false
        timelapseListState.add(timelapse)
        return true
    }

    suspend fun deleteTimelapse(timelapse: Timelapse): Boolean {
        val success = timelapseManager.deleteTimelapse(timelapse)
        if (!success) return false
        timelapseListState.remove(timelapse)
        return true
    }

    //repeating camera activity

    private val isTimelapseStartedState = mutableStateOf(RepeatingActivity.isActive(application))

    fun startTimelapse(timelapse: Timelapse, intervalSeconds: Long) {
        val repeatingActivity = RepeatingActivity(
            CameraActivity::class.java,
            Bundle().apply { putString(TIMELAPSE_NAME_KEY, timelapse.directory.name) },
            intervalSeconds
        )
        repeatingActivity.start(getApplication())
        updateStartedState()
    }

    fun stopTimelapse() {
        RepeatingActivity.stop(getApplication())
        updateStartedState()
    }

    fun isTimelapseStarted(): Boolean {
        return isTimelapseStartedState.value
    }

    private fun updateStartedState() {
        isTimelapseStartedState.value = RepeatingActivity.isActive(getApplication())
    }

}