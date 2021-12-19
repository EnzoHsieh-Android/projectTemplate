package com.citrus.projecttemplate.util.base


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BindingAdapter<VB : ViewBinding, DATA>(
    private val inflate: Inflate<VB>
) : RecyclerView.Adapter<BindingAdapter.BaseBindHolder>() {

    lateinit var context:Context

    open var data: MutableList<DATA> = mutableListOf()

    suspend fun updateDataset(newDataset: MutableList<DATA>) = withContext(Dispatchers.Default) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return data.size
            }

            override fun getNewListSize(): Int {
                return newDataset.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return true
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == newDataset[newItemPosition]
            }

        })
        withContext(Dispatchers.Main) {
            data = newDataset
            diff.dispatchUpdatesTo(this@BindingAdapter)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindHolder {
        context = parent.context
        return BaseBindHolder(
            inflate.invoke(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: BaseBindHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val adapterPosition = holder.adapterPosition
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            for (payload in payloads) {
                payloadConvert(
                    payload,
                    holder.binding as VB,
                    data[adapterPosition],
                    data.indexOf(data[adapterPosition])
                )
            }
        }
    }

    override fun onBindViewHolder(holder: BaseBindHolder, position: Int) {
        val adapterPosition = holder.adapterPosition
        convert(holder.binding as VB, data[adapterPosition], data.indexOf(data[adapterPosition]))

    }

    override fun getItemCount() = data.size

    fun removeItem(item: DATA, position: Int) {
        data.remove(item)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, data.size - 1)
    }


    abstract fun convert(binding: VB, item: DATA, position: Int)
    abstract fun payloadConvert(payload: Any, binding: VB, item: DATA, position: Int)

    class BaseBindHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)
}
