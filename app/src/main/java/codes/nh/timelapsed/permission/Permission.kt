package codes.nh.timelapsed.permission

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat

open class Permission(
    val name: String,
    val description: String
) {

    open fun isGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, name) == PackageManager.PERMISSION_GRANTED
    }

}

class SpecialPermission(
    name: String,
    description: String,
    private val checkGranted: () -> Boolean,
    private val requestAction: String
) : Permission(name, description) {

    override fun isGranted(context: Context): Boolean {
        return checkGranted()
    }

    fun getRequestIntent(context: Context): Intent {
        return Intent(requestAction, Uri.parse("package:${context.packageName}"))
    }

}