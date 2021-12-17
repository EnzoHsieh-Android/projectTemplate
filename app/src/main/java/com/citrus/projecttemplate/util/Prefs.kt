package com.citrus.projecttemplate.util

import android.content.Context
import android.content.SharedPreferences


class Prefs(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    var languagePos: Int
        get() = prefs.getInt("lan", -1)
        set(value) = prefs.edit().putInt("lan", value).apply()
}
