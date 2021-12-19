package com.citrus.projecttemplate.view.main.adapter

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.citrus.projecttemplate.R
import com.citrus.projecttemplate.databinding.ItemViewDemoBinding
import com.citrus.projecttemplate.model.dto.Meme
import com.citrus.projecttemplate.util.base.BindingAdapter
import com.citrus.projecttemplate.util.ext.onSafeClick
import javax.inject.Inject

data class DemoPayLoad(val changes: List<DemoChange>)
sealed class DemoChange {
    data class StyleChange(val color: String) : DemoChange()
    data class TextChange(val text: String) : DemoChange()
}

class DemoItemAdapter: BindingAdapter<ItemViewDemoBinding, Meme>(ItemViewDemoBinding::inflate) {

    override fun payloadConvert(
        payload: Any,
        binding: ItemViewDemoBinding,
        data: Meme,
        position: Int
    ) {
        if (payload !is DemoPayLoad) {
            return
        }

        payload.changes.forEach { change ->
            when (change) {
                is DemoChange.StyleChange -> {
                    binding.name.setTextColor(context.getColor(R.color.red))
                }
            }
        }
    }



    override fun convert(binding: ItemViewDemoBinding, data: Meme, position: Int) {
        binding.apply {

            Glide.with(root)
                .load(data.url)
                .into(img)

            id.text = data.id
            name.text = data.name

            name.onSafeClick {
                onItemClickListener?.let { click ->
                    click(data)
                    notifyItemChanged(
                        position,
                        DemoPayLoad(listOf(DemoChange.StyleChange("red")))
                    )
                }
            }

            img.onSafeClick {
                onImgClickListener?.let { click ->
                    click(it)
                }
            }

            remove.onSafeClick {
                removeItem(data, position)
                onItemRemovedListener?.let { remove ->
                    remove(data)
                }
            }
        }
    }


    private var onItemClickListener: ((Meme) -> Unit)? = null
    fun setOnItemClickListener(listener: (Meme) -> Unit) {
        onItemClickListener = listener
    }

    private var onImgClickListener: ((View) -> Unit)? = null
    fun setOnImgClickListener(listener: (View) -> Unit) {
        onImgClickListener = listener
    }

    private var onItemRemovedListener: ((Meme) -> Unit)? = null
    fun setOnItemRemovedListener(listener: (Meme) -> Unit) {
        onItemRemovedListener = listener
    }
}