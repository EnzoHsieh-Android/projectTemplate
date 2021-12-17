package com.citrus.projecttemplate.view


import android.graphics.Point
import android.util.Log
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.citrus.projecttemplate.databinding.ActivityMainBinding
import com.citrus.projecttemplate.util.Constants
import com.citrus.projecttemplate.util.base.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = ActivityMainBinding::inflate

    override fun initView() {
        val size = Point()
        val display = windowManager.defaultDisplay
        display.getSize(size)
        /**儲存螢幕size*/
        Constants.screenW = size.x
        Constants.screenH = size.y
    }

    override fun initObserve() {
        Log.e("initObserve", "initObserve")
    }
}