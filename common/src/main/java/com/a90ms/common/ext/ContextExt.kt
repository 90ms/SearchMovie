package com.a90ms.common.ext

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlin.system.exitProcess

fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun Context?.isValidContext() = when (this) {
    null -> false
    is Activity -> (isDestroyed || isFinishing).not()
    else -> true
}

fun Context.quit() {
    if (this is Activity) {
        ActivityCompat.finishAffinity(this)
        System.runFinalization()
        exitProcess(0)
    }
}
