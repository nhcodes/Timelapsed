package codes.nh.timelapsed.repeatingactivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import codes.nh.timelapsed.utils.getTimeString
import codes.nh.timelapsed.utils.log

class RepeatingActivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        log("${getTimeString()} onReceive")

        val repeatingTask = RepeatingActivity(intent.extras!!)
        repeatingTask.start(context)
        repeatingTask.launchActivity(context)

    }

}