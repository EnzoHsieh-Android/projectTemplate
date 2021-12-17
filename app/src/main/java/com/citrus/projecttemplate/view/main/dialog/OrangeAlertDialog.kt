package com.citrus.projecttemplate.view.main.dialog

import android.content.Context
import com.citrus.projecttemplate.databinding.DialogViewBinding
import com.citrus.projecttemplate.util.base.BindingAlertDialog
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.skydoves.balloon.*
import com.skydoves.balloon.overlay.BalloonOverlayAnimation

class OrangeAlertDialog(private val mContext: Context) :
    BindingAlertDialog<DialogViewBinding>(mContext, DialogViewBinding::inflate) {

    private var balloon: Balloon? = null

    override fun initView() {
        initBalloon()
        binding.apply {
            YoYo.with(Techniques.Tada)
                .duration(700)
                .onEnd {
                    balloon?.showAlignTop(orange)
                }
                .playOn(orange)
        }
    }

    private fun initBalloon() {
        balloon = createBalloon(mContext) {
            setArrowSize(10)
            setText("Good!")
            setTextSize(20f)
            setArrowOrientationRules(ArrowOrientationRules.ALIGN_FIXED)
            setArrowOrientation(ArrowOrientation.BOTTOM)
            setWidth(100)
            setHeight(80)
            setCornerRadius(10f)
            setAlpha(0.9f)
            setBalloonAnimation(BalloonAnimation.OVERSHOOT)
            setBalloonOverlayAnimation(BalloonOverlayAnimation.FADE)
            setLifecycleOwner(lifecycleOwner)
        }
    }
}