package com.citrus.projecttemplate.util.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.citrus.projecttemplate.R
import com.citrus.projecttemplate.util.Constants
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


abstract class BindingDialogFragment<out T : ViewBinding> : DialogFragment() {
    protected abstract val bindingInflater: (LayoutInflater) -> ViewBinding
    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: T
        get() = _binding as T

    private var isActive = false
    var isFullScreen = true


    override fun onDestroyView() {
        clearMemory()
        _binding = null
        super.onDestroyView()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isActive = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater(inflater)
        return _binding!!.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        isActive = false
        super.onDismiss(dialog)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        view.setBackgroundResource(R.drawable.custom_dialog_bg)
        setWindowWidthPercent()
        initView()
        initAction()
        if (isFullScreen) {
            setFullScreen()
        }
    }

    abstract fun initView()
    abstract fun initAction()
    abstract fun clearMemory()

    //if add this, click editText, window will not pop to top
    open fun setFullScreen() {
        val decorView = dialog?.window?.decorView
        decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }


    open fun setWindowWidthPercent(wPct: Double = 0.95, hPct: Double = 0.95) {
        dialog?.window?.let {
            val width =
                if (wPct == 0.0) WindowManager.LayoutParams.WRAP_CONTENT else Constants.screenW * wPct
            val height =
                if (hPct == 0.0) WindowManager.LayoutParams.WRAP_CONTENT else Constants.screenH * hPct

            it.setLayout((width).toInt(), (height).toInt())
            it.setGravity(Gravity.CENTER)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireContext(), theme) {
            override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
                if (ev.action == MotionEvent.ACTION_DOWN) {
                    val v = currentFocus
                    if (isShouldHideInput(v, ev)) {
                        val imm =
                            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                        imm?.hideSoftInputFromWindow(v!!.windowToken, 0)
                    }
                    return super.dispatchTouchEvent(ev)
                }
                return if (window!!.superDispatchTouchEvent(ev)) {
                    true
                } else onTouchEvent(ev)
            }
        }
    }

    fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && (v is EditText || v is TextInputEditText || v is TextInputLayout)) {
            val leftTop = intArrayOf(0, 0)
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        return false
    }
}

