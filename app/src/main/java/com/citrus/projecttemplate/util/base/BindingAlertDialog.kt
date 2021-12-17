package com.citrus.projecttemplate.util.base

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.lifecycle.LifecycleObserver
import androidx.viewbinding.ViewBinding
import com.citrus.projecttemplate.R



typealias InflateActivity<T> = (LayoutInflater) -> T

abstract class BindingAlertDialog<out VB : ViewBinding>(
    private val mContext: Context,
    private val inflate: InflateActivity<VB>
) : AlertDialog(mContext, R.style.CustomDialogTheme), LifecycleObserver {

    private var _binding: VB? = null
    open val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflate.invoke(layoutInflater)
        setContentView(binding.root)

        setOwnerActivity(mContext as Activity) //important!!! OwnerActivity is for dispatchTouchEvent


        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        setCancelable(true)
        initView()
    }

    override fun dismiss() {
        _binding = null
        super.dismiss()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText || v is AutoCompleteTextView) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    ownerActivity?.let {
                        val imm =
                            it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                }
            }
        }

        return super.dispatchTouchEvent(event)
    }

    abstract fun initView()

}