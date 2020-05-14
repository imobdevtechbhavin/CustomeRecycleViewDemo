package com.socket.customerecycleviewdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.chart.recycleview.base.BaseBindingAdapter
import com.chart.recycleview.base.BaseBindingViewHolder
import com.socket.customerecycleviewdemo.databinding.ItemListTestBinding


class TestAdepter : BaseBindingAdapter<Int?>() {

    override fun bind(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding {
        return ItemListTestBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_ITEM -> {

                val binding: ItemListTestBinding = holder.binding as ItemListTestBinding
                val item = items[position]
                item?.let {
                    binding.tv.text=""+item
                    Glide
                        .with(binding.root.context)
                        .load(item)
                        .into(binding.iv)

                }
            }

        }

    }
}