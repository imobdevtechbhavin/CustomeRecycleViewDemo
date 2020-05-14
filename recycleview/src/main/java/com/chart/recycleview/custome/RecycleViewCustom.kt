package com.chart.recycleview.custome

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chart.recycleview.R
import com.chart.recycleview.base.BaseBindingAdapter
import com.chart.recycleview.databinding.RecycleviewLayoutBinding


/**
 * Created by keshu odedara on 28,March,2020
 */
class RecycleViewCustom : LinearLayout {
    lateinit var binding: RecycleviewLayoutBinding
    lateinit var typedArray: TypedArray
    lateinit var rvItems: RecyclerView
    var TOTAL_PAGE = 1
    var CURENT_PAGE = 1
    var swipeToRefreshItemClick: onSwipeToRefresh? = null
    var onLoadMoreItemClick: onLoadMore? = null


    constructor(context: Context) : super(context) {

    }

    @SuppressLint("Recycle")
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.RecycleViewCustom, 0, 0
        )
        val mSpanCount =
            typedArray.getInt(R.styleable.RecycleViewCustom_rv_span_count, 2)

        val isSwipeToRefresh =
            typedArray.getBoolean(R.styleable.RecycleViewCustom_is_swipe_refresh_layout, false)

        val isLoadMore =
            typedArray.getBoolean(R.styleable.RecycleViewCustom_is_load_more, false)

        val layoutManager =
            typedArray.getInt(R.styleable.RecycleViewCustom_set_layout_manager, 1)

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
        binding = RecycleviewLayoutBinding.inflate(inflater as LayoutInflater, this, true)
        rvItems = binding.rvItems

        /*[start]set layout manager*/

        layoutManager.let {
            when (it) {
                1 -> binding.rvItems.layoutManager = LinearLayoutManager(context)
                2 -> binding.rvItems.layoutManager =
                    GridLayoutManager(context, mSpanCount)
                3 ->{
                    binding.rvItems.layoutManager = StaggeredGridLayoutManager(mSpanCount, StaggeredGridLayoutManager.VERTICAL)
                }
            }
        }

        /*[end]set layout manager*/

        /*[start] set pull to refresh */
        isSwipeToRefresh.let {
            if (isSwipeToRefresh) {
                binding.swipeContainer.setOnRefreshListener {
                    binding.swipeContainer.isRefreshing = false
                    swipeToRefreshItemClick?.onSwipeToRefresh()
                    CURENT_PAGE = 1
                }
            } else {
                binding.swipeContainer.isRefreshing = isSwipeToRefresh
                binding.swipeContainer.isEnabled = isSwipeToRefresh
            }

        }
        /*[end] set pull to refresh */

        /*[start] set is load more*/
        if (isLoadMore) {

            loadMore(layoutManager,mSpanCount)
        }
        /*[end] set is load more*/

    }

    private val emptyObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            checkData()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            checkData()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            checkData()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            checkData()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            checkData()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            super.onItemRangeChanged(positionStart, itemCount, payload)
            checkData()
        }
    }

    private fun checkData() {
        Log.e("CHANGES_DATA", "----------->")
            val count = (binding.rvItems.adapter as BaseBindingAdapter<*>).itemCount
            if (count == 0) {
                setEmptyView(count)
            } else {
                setEmptyView(count)
            }
    }

    private fun setEmptyView(size: Int?) {
        if (size == 0) {
            binding.dataNotFound.visibility = View.VISIBLE
            binding.rvItems.visibility = View.GONE
        } else {
            binding.dataNotFound.visibility = View.GONE
            binding.rvItems.visibility = View.VISIBLE
        }
    }

    /*loading flag*/
    var isLoading: Boolean = true
    lateinit var layoutManager: RecyclerView.LayoutManager
    fun loadMore(mLayoutManager: Int, gridLayoutManagerSpanCount: Int) {

        when (mLayoutManager) {
            2 -> {
                layoutManager = binding.rvItems.layoutManager as GridLayoutManager
                (layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when ((binding.rvItems.adapter as BaseBindingAdapter<*>).getItemViewType(
                            position
                        )) {
                            (binding.rvItems.adapter as BaseBindingAdapter<*>).VIEW_TYPE_ITEM -> 1
                            else -> gridLayoutManagerSpanCount
                        }
                    }
                }
            }
            1 -> layoutManager = binding.rvItems.layoutManager as LinearLayoutManager
            3 -> layoutManager = binding.rvItems.layoutManager as StaggeredGridLayoutManager
        }

        binding.run {
            when (mLayoutManager) {
                2 -> {
                    layoutManager = binding.rvItems.layoutManager as GridLayoutManager
                    (layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return when ((binding.rvItems.adapter as BaseBindingAdapter<*>).getItemViewType(
                                position
                            )) {
                                (binding.rvItems.adapter as BaseBindingAdapter<*>).VIEW_TYPE_ITEM -> 1
                                else -> gridLayoutManagerSpanCount
                            }
                        }
                    }
                }
                1 -> layoutManager = binding.rvItems.layoutManager as LinearLayoutManager
                3 ->{
                    layoutManager = binding.rvItems.layoutManager as StaggeredGridLayoutManager
                    (layoutManager as StaggeredGridLayoutManager).gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS}
            }
            if(mLayoutManager==2 || mLayoutManager==1){
                rvItems.addOnScrollListener(object :
                    PaginationScrollListenerLinear(
                        layoutManager as LinearLayoutManager) {
                    override fun loadMoreItems() {
                        if (TOTAL_PAGE != CURENT_PAGE) {
                            if (isLoading) {
                                CURENT_PAGE += 1
                                isLoading = false
                                (binding.rvItems.adapter as BaseBindingAdapter<*>).addFooterProgressItem()
                                onLoadMoreItemClick?.onLoadMore()
                            }
                        }
                    }

                    override fun getTotalPageCount(): Int {
                        return TOTAL_PAGE
                    }

                    override fun isLastPage(): Boolean {
                        return false
                    }

                    override fun isLoading(): Boolean {
                        return false
                    }

                })

            }else{
                rvItems.addOnScrollListener(object :
                    PaginationScrollListenerStaggerLayout(
                        layoutManager as StaggeredGridLayoutManager) {
                    override fun loadMoreItems() {
                        if (TOTAL_PAGE != CURENT_PAGE) {
                            if (isLoading) {
                                CURENT_PAGE += 1
                                isLoading = false
                                (binding.rvItems.adapter as BaseBindingAdapter<*>).addFooterProgressItem()
                                onLoadMoreItemClick?.onLoadMore()
                            }
                        }
                    }

                    override fun getTotalPageCount(): Int {
                        return TOTAL_PAGE
                    }

                    override fun isLastPage(): Boolean {
                        return false
                    }

                    override fun isLoading(): Boolean {
                        return false
                    }

                })
            }


        }
    }

    constructor(context: Context, attrs: AttributeSet, styleId: Int) : super(
        context,
        attrs,
        styleId
    )

    public fun setAdepter(adepter: RecyclerView.Adapter<*>) {
        binding.rvItems.adapter = adepter

    }
    public fun setTotalPages(TotalPage:Int=1){
        TOTAL_PAGE=TotalPage

    }

    public fun isLoading(){
        isLoading=true
      //  emptyObserver.onChanged()
    }
    interface onSwipeToRefresh {
        fun onSwipeToRefresh()
    }

    interface onLoadMore {
        fun onLoadMore()
    }
}