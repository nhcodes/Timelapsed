package codes.nh.timelapsed.timelapse

import java.io.File

class Timelapse(val directory: File, val entries: List<TimelapseEntry>) {

    val name: String = directory.name

}

