package com.citrus.projecttemplate.util.ext

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

object DensityUtil {
    fun Context.dp2px(dpValue: Float):Float {
       var scale = this.resources.displayMetrics.density
        return dpValue * scale + 0.5f
    }

    fun Context.px2dp(pxValue: Float) : Int {
        var scale = this.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun Context.px2sp(pxValue: Float): Float {
        val fontScale = this.resources.displayMetrics.scaledDensity
        return pxValue / fontScale + 0.5f
    }

    fun Context.sp2px(spValue: Float): Int {
        val fontScale = this.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }


    inline fun <reified T> Resources.dpToPx(value: Int): T {
        val result = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(), displayMetrics)

        return when (T::class) {
            Float::class -> result as T
            Int::class -> result.toInt() as T
            else -> throw IllegalStateException("Type not supported")
        }
    }

}