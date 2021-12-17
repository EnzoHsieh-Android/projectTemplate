package com.citrus.projecttemplate.util


import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.citrus.projecttemplate.R
import com.citrus.projecttemplate.di.MyApplication.Companion.prefs

import com.skydoves.balloon.Balloon
import com.skydoves.elasticviews.ElasticAnimation

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object Constants {
    const val BASE_URL = "https://api.imgflip.com/"
    const val SHARED_PREFERENCES_NAME = "sharedPref"

    @SuppressLint("SimpleDateFormat")
    var dateTimeFormatSql = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    var screenW = 0
    var screenH = 0

    fun String.trimSpace(): String {
        return this.replace("\\s".toRegex(), "")
    }

    fun Balloon.setDuration(sec: Long) {
        MainScope().launch {
            val duration = TimeUnit.SECONDS.toMillis(sec)
            delay(duration)
        }
        this.dismiss()
    }

    /**取得當前時間*/
    @SuppressLint("SimpleDateFormat")
    fun getCurrentTime(): String {
        val currentDate = Calendar.getInstance().time
        val sdf = dateTimeFormatSql
        return sdf.format(currentDate)
    }


    /**View點擊動畫*/
    inline fun View.clickAnimation(crossinline block: suspend () -> Unit) {
        ElasticAnimation(this)
            .setScaleX(0.85f)
            .setScaleY(0.85f)
            .setDuration(100)
            .setOnFinishListener {
                this.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                    block()
                }
            }.doAction()
    }

}