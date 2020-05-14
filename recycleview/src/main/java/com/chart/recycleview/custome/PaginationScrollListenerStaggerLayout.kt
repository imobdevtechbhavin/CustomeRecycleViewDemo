package com.chart.recycleview.custome

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


/**
 * Created by keshu odedara
 */
abstract class PaginationScrollListenerStaggerLayout(

    private var staggeredGridLayoutManager: StaggeredGridLayoutManager
) : RecyclerView.OnScrollListener() {
    private var pastVisibleItems: Int = 0
    val totalItemCount: Int = 0
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = staggeredGridLayoutManager.childCount
        var firstVisibleItems: IntArray? = null

        firstVisibleItems =
            staggeredGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems)
        if (firstVisibleItems != null && firstVisibleItems.isNotEmpty()) {
            pastVisibleItems = firstVisibleItems[0]
        }
        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
            loadMoreItems()
        }

    }

    protected abstract fun loadMoreItems()

    abstract fun getTotalPageCount(): Int

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean
}