package com.citrus.projecttemplate.util.i18n

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.citrus.projecttemplate.di.MyApplication.Companion.prefs
import com.yariksoffice.lingver.Lingver


import java.util.*

object LocaleHelper {


    fun onAttach(context: Context?): Context? {

        var pos: Int = prefs?.languagePos ?: 0

        
        var newLocale = when (pos) {
            0 -> Locale.SIMPLIFIED_CHINESE
            1 -> Locale.CHINESE
            2 -> Locale.US
            else -> {
                Locale.getDefault()
            }
        }

        Lingver.getInstance().setLocale(context!!, newLocale)



        return initLocale(context, newLocale)
    }

    /**進行本地化*/
    private fun initLocale(context: Context?, locale: Locale): Context? {

        if (VersionUtils.isAfter24) {
            return updateResources(context, locale)
        }
        return updateResourcesLegacy(context, locale)
    }

    /**region 因應 API Level 取得相對應context更新方法*/
    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context?, locale: Locale): Context? {
        Locale.setDefault(locale)

        val configuration: Configuration? = context?.resources?.configuration
        if (configuration != null) {
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
        }

        return context?.createConfigurationContext(configuration!!)
    }

    private fun updateResourcesLegacy(context: Context?, locale: Locale): Context? {
        Locale.setDefault(locale)

        val resource = context?.resources

        val configuration: Configuration? = resource?.configuration
        if (configuration != null) {
            configuration.locale = locale
            if (VersionUtils.isAfter11)
                configuration.setLayoutDirection(locale)
            resource.updateConfiguration(configuration, resource.displayMetrics)
        }

        return context
    }

}