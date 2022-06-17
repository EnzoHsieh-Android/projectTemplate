package com.citrus.projecttemplate.view.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.citrus.projecttemplate.databinding.ItemViewUserBinding
import com.citrus.projecttemplate.model.dto.User


class UserAdapter :
    PagingDataAdapter<User, UserAdapter.PhotoViewHolder>(USER_COMPARATOR) {

    var selectId = listOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding =
            ItemViewUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val parentHeight: Int = parent.height
        val layoutParams: ViewGroup.LayoutParams = binding.root.layoutParams
        layoutParams.height = parentHeight / 3

        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class PhotoViewHolder(private val binding: ItemViewUserBinding) :
        RecyclerView.ViewHolder(binding.root) {


        init {
            binding.root.setOnClickListener {
                Log.e("selectId","!!!")
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    selectId = selectId + item!!.id
                    Log.e("selectId",selectId.toString())
                    a(position)
                }
            }
        }

        fun bind(user: User) {
            binding.apply {
                name.text = user.name
                age.text = user.age

                if(selectId.contains(user.id)){
                    name.text = user.name + " Selected"
                }
            }
        }
    }

    fun a(position:Int) {
        Log.e("!!","refresh")
        this.notifyItemChanged(position)
    }

    // DiffUtil是幫助我們在刷新RecyclerView時，計算新舊DATA的差異，
    // 並自動調用RecyclerView.Adapter的刷新方法，以完成高效刷新並伴有項目動畫的效果，
    // 避免並自動調用RecyclerView無腦調用mAdapter.notifyDataSetChanged()
    companion object {
        private val USER_COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: User, newItem: User) =
                oldItem == newItem
        }
    }
}