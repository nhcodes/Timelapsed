package codes.nh.timelapsed.repeatingactivity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import codes.nh.timelapsed.utils.log

/*
https://developer.android.com/training/scheduling/alarms
https://developer.android.com/guide/components/activities/background-starts
*/

private const val ACTIVITY_CLASS_KEY = "activityClass"
private const val INTERVAL_KEY = "intervalSeconds"

private const val REQUEST_CODE = 12345

class RepeatingActivity(
    private val activityClass: Class<*>,
    private val activityExtras: Bundle = Bundle(),
    private val intervalSeconds: Long
) {

    constructor(bundle: Bundle) : this(
        Class.forName(bundle.get(ACTIVITY_CLASS_KEY) as String),
        bundle,
        bundle.get(INTERVAL_KEY) as Long
    )

    private val activityClassName = activityClass.canonicalName!!

    fun start(context: Context) {
        log("start $intervalSeconds")

        val creationIntent = getCreationIntent(context)

        val timestamp = System.currentTimeMillis() + intervalSeconds * 1000

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(timestamp, null),
            creationIntent
        )

        /*
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timestamp,
            pendingIntent
        )
        */
    }

    fun launchActivity(context: Context) {
        log("launch $activityClassName")
        val intent = Intent(context, activityClass)
        intent.putExtras(activityExtras)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun getCreationIntent(context: Context): PendingIntent? {
        val intent = Intent(context, RepeatingActivityReceiver::class.java)
        intent.putExtras(activityExtras)
        intent.putExtra(INTERVAL_KEY, intervalSeconds)
        intent.putExtra(ACTIVITY_CLASS_KEY, activityClassName)
        val flag = PendingIntent.FLAG_CANCEL_CURRENT
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flag or PendingIntent.FLAG_IMMUTABLE
        } else {
            flag
        }
        return PendingIntent.getBroadcast(context, REQUEST_CODE, intent, flags)
    }

    companion object {

        fun isActive(context: Context): Boolean {
            return getStatusIntent(context) != null
        }

        fun stop(context: Context) {
            val statusIntent = getStatusIntent(context) ?: return
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(statusIntent)
            statusIntent.cancel()
        }

        private fun getStatusIntent(context: Context): PendingIntent? {
            val intent = Intent(context, RepeatingActivityReceiver::class.java)
            val flag = PendingIntent.FLAG_NO_CREATE
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                flag or PendingIntent.FLAG_IMMUTABLE
            } else {
                flag
            }
            return PendingIntent.getBroadcast(context, REQUEST_CODE, intent, flags)
        }

    }

}