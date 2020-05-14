package com.chart.recycleview.custome

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by keshu odedara
 */
abstract class PaginationScrollListenerLinear(
    private var layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

            val totalItemCount = layoutManager.itemCount
            val lastVisible = layoutManager.findLastVisibleItemPosition()

            val endHasBeenReached = lastVisible + 1 >= totalItemCount
            if (totalItemCount > 0 && endHasBeenReached) {
                if (!isLoading() && !isLastPage()) {
                    loadMoreItems()
                }
            }
    }

    protected abstract fun loadMoreItems()

    abstract fun getTotalPageCount(): Int

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean
}