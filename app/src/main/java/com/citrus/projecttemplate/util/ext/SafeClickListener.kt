package com.citrus.projecttemplate.util.ext

import android.os.SystemClock
import android.view.View

/**Prevent twice click in a short time*/
class SafeClickListener(
    private var defaultInterval: Int = 500,
    private val onSafeClick: (View) -> Unit
) : View.OnClickListener {

    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()

        onSafeClick(v)
    }
}

fun View.onSafeClick(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}