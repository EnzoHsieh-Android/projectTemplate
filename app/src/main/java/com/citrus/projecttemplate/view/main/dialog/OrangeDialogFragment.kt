package com.citrus.projecttemplate.view.main.dialog


import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.citrus.projecttemplate.databinding.FragmentOrangeDialogBinding
import com.citrus.projecttemplate.util.base.BindingDialogFragment
import com.citrus.projecttemplate.util.base.lifecycleFlow
import com.citrus.projecttemplate.util.ext.onSafeClick
import com.citrus.projecttemplate.view.main.MainViewModel
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.skydoves.balloon.*
import com.skydoves.balloon.overlay.BalloonOverlayAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrangeDialogFragment : BindingDialogFragment<FragmentOrangeDialogBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentOrangeDialogBinding::inflate

    private val viewModel: DialogViewModel by viewModels()

    private var balloon: Balloon? = null

    override fun initView() {

        viewModel.initLaunch()
        /**設定視窗大小*/
        setWindowWidthPercent(hPct = 0.6)
        binding.apply {
            orange.onSafeClick { view ->
                YoYo.with(Techniques.Tada)
                    .duration(700)
                    .onEnd {
                        balloon?.showAlignTop(view)
                    }
                    .playOn(view)
            }
        }
    }

    override fun initAction() {
        lifecycleFlow(viewModel.sharedPackString){
            initBalloon(it)
        }
    }

    override fun clearMemory() {
        Log.e("","")
    }

    private fun initBalloon(msg:String) {
        balloon = createBalloon(requireContext()) {
            setArrowSize(10)
            setText(msg)
            setTextSize(20f)
            setArrowOrientation(ArrowOrientation.BOTTOM)
            setArrowOrientationRules(ArrowOrientationRules.ALIGN_FIXED)
            setWidth(100)
            setHeight(100)
            setCornerRadius(10f)
            setAlpha(0.9f)
            setBalloonAnimation(BalloonAnimation.OVERSHOOT)
            setBalloonOverlayAnimation(BalloonOverlayAnimation.FADE)
            setLifecycleOwner(lifecycleOwner)
        }
    }
}