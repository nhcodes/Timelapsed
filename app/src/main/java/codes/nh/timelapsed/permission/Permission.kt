package codes.nh.timelapsed.permission

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat

open class Permission(
    val name: String,
    val description: String,
    val required: (Int) -> Boolean = { true }
) {

    open fun isGranted(context: Context): Boolean {
        return !required(Build.VERSION.SDK_INT) ||
                ContextCompat.checkSelfPermission(context, name) == PackageManager.PERMISSION_GRANTED
    }

}

class SpecialPermission(
    name: String,
    description: String,
    required: (Int) -> Boolean = { true },
    private val checkGranted: () -> Boolean,
    private val requestAction: String
) : Permission(name, description, required) {

    override fun isGranted(context: Context): Boolean {
        return !required(Build.VERSION.SDK_INT) || checkGranted()
    }

    fun getRequestIntent(context: Context): Intent {
        return Intent(requestAction, Uri.parse("package:${context.packageName}"))
    }

}