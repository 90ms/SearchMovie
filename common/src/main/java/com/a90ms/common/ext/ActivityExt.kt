package com.a90ms.common.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager

inline fun <reified T : Activity> Activity.startActivity(
    bundle: Bundle? = null,
    transitionBundle: Bundle? = null
) {
    val intent = Intent(this, T::class.java)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(intent, transitionBundle)
}

inline fun <reified T : Activity> Activity.startActivity(
    configureIntent: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java).apply(configureIntent)
    startActivity(intent)
}

fun Activity.showKeyboard(view: View, delay: Long = 100) {
    view.postDelayed(
        {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
                view,
                0
            )
        },
        delay
    )
}

fun Activity.hideKeyboard(view: View, delay: Long = 100) {
    view.postDelayed(
        {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(
                    view.windowToken,
                    0
                )
        },
        delay
    )
}
