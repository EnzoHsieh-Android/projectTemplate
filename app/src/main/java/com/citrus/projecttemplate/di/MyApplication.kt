package com.citrus.projecttemplate.di

import android.app.Application
import com.citrus.projecttemplate.util.Prefs
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp


val prefs: Prefs by lazy {
    MyApplication.prefs!!
}

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        /** If Lingver not work checkout settings gradle if "maven { url 'https://jitpack.io' }" is exist */
        /** This library solve the problem which has changed language use i18N,
         * but when get context through fragment to get string in xml still not changed,
         * this problem due to the application context not changed while changed i18N ,
         * it only changed activity context. */
        Lingver.init(this)
        prefs = Prefs(applicationContext)

    }

    companion object {
        var prefs: Prefs? = null
    }
}