package com.socket.customerecycleviewdemo.base


import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

//Created by Keshu odedara
/*Base class for Recyclerview ViewHolder*/
class BaseBindingViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
    /**
     * Called when mInputCharacter view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        clickListener.onViewClick(v!!, adapterPosition)
    }

    var binding: ViewDataBinding
    var clickListener: ClickListener

    constructor(binding: ViewDataBinding, clickListener: ClickListener) : super(binding.root) {
        binding.root.setOnClickListener(this)
        this.binding = binding
        this.clickListener = clickListener
        clickViews(this)
    }

    private fun clickViews(bindingViewHolder: BaseBindingViewHolder) {
        val view = bindingViewHolder.binding.root
        if (view is ViewGroup) {
            setViewGroupClick(view, view)
        }
    }

    private fun setViewGroupClick(viewGroup: ViewGroup, parentView: View) {
        for (i in 0 until viewGroup.childCount) {
            if (viewGroup.childCount > 0 && viewGroup.getChildAt(i) is ViewGroup) {
                setViewGroupClick(viewGroup.getChildAt(i) as ViewGroup, parentView)
            }
            viewGroup.getChildAt(i).setOnClickListener { view -> clickListener.onViewClick(view!!, adapterPosition) }
        }
    }

    interface ClickListener {
        fun onViewClick(view: View, position: Int)
    }
}