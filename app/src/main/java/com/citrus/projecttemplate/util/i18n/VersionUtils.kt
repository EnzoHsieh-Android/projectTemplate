package com.citrus.projecttemplate.util.i18n

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi

@SuppressLint("ObsoleteSdkInt")
object VersionUtils {

    val isAfter25: Boolean
        @RequiresApi(Build.VERSION_CODES.N_MR1)
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1

    val isAfter24: Boolean
        @RequiresApi(Build.VERSION_CODES.N)
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

    val isAfter23: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    val isAfter22: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1

    val isAfter21: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    val isAfter20: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH

    val isAfter19: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    val isAfter18: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2

    val isAfter17: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1

    val isAfter16: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN

    val isAfter14: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH

    val isAfter13: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2

    val isAfter11: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB

    val isAfter9: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD

    val isAfter8: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO

    val isAfter5: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR
}