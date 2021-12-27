package com.citrus.projecttemplate.view.main.adapter

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.DragEvent
import android.view.HapticFeedbackConstants
import android.view.View
import com.citrus.projecttemplate.databinding.ItemViewPuzzleBinding
import com.citrus.projecttemplate.model.dto.PuzzleBitmap
import com.citrus.projecttemplate.util.base.BindingAdapter
import com.citrus.projecttemplate.util.ext.onSafeClick


class PuzzleAdapter: BindingAdapter<ItemViewPuzzleBinding, PuzzleBitmap>(ItemViewPuzzleBinding::inflate) {

    var dragPosition = -1
    override fun convert(binding: ItemViewPuzzleBinding, item: PuzzleBitmap, position: Int) {
        binding.apply {
            puzzle.setImageBitmap(item.bitmap)


            /**Long Click保存拖曳位置供drop時比對*/
            root.setOnLongClickListener {
                val vibrator: Vibrator =
                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            500,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else vibrator.vibrate(500)

                dragPosition = position

                val shadowBuilder = View.DragShadowBuilder(root)
                root.startDragAndDrop(null, shadowBuilder, null, 0)
                root.performHapticFeedback(
                    HapticFeedbackConstants.LONG_PRESS,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                )
            }

            root.setOnDragListener { _, dragEvent ->
                if (dragEvent.action == DragEvent.ACTION_DROP && dragPosition != position) {
                    var result = switchItem(dragPosition, position)

                    if (result) {
                        onCompleteListener?.let { alert ->
                            alert()
                        }
                    }

                }
                return@setOnDragListener true
            }
        }
    }

    /**交換拼圖位置＆位置確認*/
    private fun switchItem(first: Int, second: Int): Boolean {
        var temp = data[first]
        data[first] = data[second]
        data[second] = temp
        notifyItemChanged(first)
        notifyItemChanged(second)
        var check = false

        var checkNum = -1

        data.forEachIndexed { index, data ->
            val num = data.index
            if (num < checkNum) {
                return false
            } else {
                checkNum = num
                if (index == 8) {
                    check = true
                }
            }
        }
        return check
    }

    private var onCompleteListener: (() -> Unit)? = null
    fun setOnCompleteListener(listener: () -> Unit) {
        onCompleteListener = listener
    }

    override fun payloadConvert(
        payload: Any,
        binding: ItemViewPuzzleBinding,
        item: PuzzleBitmap,
        position: Int
    ) {

    }

}