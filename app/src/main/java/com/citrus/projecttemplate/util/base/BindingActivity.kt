package com.citrus.projecttemplate.util.base

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.citrus.projecttemplate.util.i18n.LocaleHelper


abstract class BindingActivity<out VB : ViewBinding> : AppCompatActivity() {
    private var binding: ViewBinding? = null
    protected abstract val bindingInflater: (LayoutInflater) -> ViewBinding

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    protected abstract fun initView()
    protected abstract fun initObserve()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingInflater.invoke(layoutInflater)
        setContentView(requireNotNull(binding).root)
        initView()
        initObserve()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                var isTouchOnView: Boolean
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)

                isTouchOnView = outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED)

                if (!isTouchOnView) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }

                v.isCursorVisible = isTouchOnView
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}