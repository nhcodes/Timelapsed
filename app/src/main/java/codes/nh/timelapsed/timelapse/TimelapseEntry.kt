package codes.nh.timelapsed.timelapse

import java.io.File

class TimelapseEntry(val file: File) {

    val name: String = file.name

    val lastModified = file.lastModified().takeIf { it > 0L }

}