package com.chart.recycleview.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.chart.recycleview.custome.RecycleViewCustom
import com.chart.recycleview.databinding.LoadMoreProgressBinding


//Created by keshu odedara

/*A Base class for Recyclerview adapter*/
abstract class BaseBindingAdapter<T> : RecyclerView.Adapter<BaseBindingViewHolder>(),
    BaseBindingViewHolder.ClickListener {

    val VIEW_TYPE_ITEM = 0
    val VIEW_TYPE_LOADING = 1

    override fun onViewClick(view: View, position: Int) {
        itemClickListener?.onItemClick(view, items[position], position)
    }

    /**
     * Enable filter or not !
     */
    var filterable: Boolean = false
    /*Enable load more or not*/
    var enableLoadMore: Boolean = true

    var items: ArrayList<T?> = ArrayList<T?>()

    /**
     * used for backup purpose in case of filterable = true
     */
    protected var allItems: ArrayList<T?> = ArrayList<T?>()

    var itemClickListener: ItemClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder {
        if(viewType == VIEW_TYPE_ITEM) {
            val inflater = LayoutInflater.from(parent.context)
            val binding = bind(inflater, parent, viewType)
            return BaseBindingViewHolder(binding, this)
        } else {
            // viewType == VIEW_TYPE_LOADING
            val inflater = LayoutInflater.from(parent.context)
            val binding = LoadMoreProgressBinding.inflate(inflater, parent, false)
            return BaseBindingViewHolder(binding, this)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(item: ArrayList<T?>) {
        items = item
        if (filterable) allItems = item
        notifyDataSetChanged()
    }

    fun addItems(item: ArrayList<T?>) {
        items.addAll(item)
        if (filterable) allItems.addAll(item)
        //notifyDataSetChanged()
    }

    fun addItem(item: T?) {
        items.add(item)
        if (filterable) allItems.add(item)
        notifyDataSetChanged()
    }

    /*Adding footer progress bar while performing load more*/
    fun addFooterProgressItem() {
        items.add(null)
        if (filterable) allItems.add(null)
        notifyItemInserted((items.size - 1))
    }

    /*Remove footer progress bar while load more of data completes*/
    fun removeFooterProgressItem(customView: RecycleViewCustom) {
        items.removeAt(items.size - 1)
        if (filterable) allItems.removeAt(items.size - 1)
        val scrollPosition = items.size
        notifyItemRemoved(scrollPosition)
        customView.isLoading()
    }

    fun addItemNotify(item: T) {
        items.add(item)
        if (filterable) allItems.add(item)
        notifyItemInserted(itemCount - 1)
    }

    fun removeItem(position: Int) {
        if(position >= 0) {
            items.removeAt(position)
            if (filterable) allItems.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        val size = items.size
        if (size > 0) {
            items.clear()
            if (filterable) allItems.clear()
            notifyItemRangeRemoved(0, size)
        }
    }

    protected abstract fun bind(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding

    interface ItemClickListener<T> {
        fun onItemClick(view: View, data: T?, position: Int)
    }

    override fun getItemViewType(position: Int): Int {
        //return position
        if(enableLoadMore) {
            return if (items[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
        } else {
            return VIEW_TYPE_ITEM
        }
    }
}