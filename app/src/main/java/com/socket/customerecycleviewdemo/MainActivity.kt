package com.socket.customerecycleviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.socket.customerecycleviewdemo.custome.RecycleViewCustom
import com.socket.customerecycleviewdemo.adapter.TestAdepter
import com.socket.customerecycleviewdemo.base.BaseBindingAdapter
import com.socket.customerecycleviewdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),  RecycleViewCustom.onSwipeToRefresh,
    RecycleViewCustom.onLoadMore, BaseBindingAdapter.ItemClickListener<Int?> {
    override fun onItemClick(view: View, data: Int?, position: Int) {
        Toast.makeText(this@MainActivity,"",Toast.LENGTH_LONG).show()
    }

    override fun onLoadMore() {
        Log.e("LOADMORE","--------------->")
        Handler().postDelayed(Runnable {
            (binding.customView.rvItems.adapter as TestAdepter).removeFooterProgressItem(binding.customView)
            (binding.customView.rvItems.adapter as TestAdepter).addItems(testingList)

        },3000)

    }
    override fun onSwipeToRefresh() {
        Log.e("SWIPE_TO_REFRESH","--------------->")

        setData()
    }


    /*binding variable for this activity*/
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    var testingList:ArrayList<Int?> = ArrayList()

    private fun init() {

        val adepter = TestAdepter()
        binding.customView.setAdepter(adepter)
        binding.customView.swipeToRefreshItemClick=this
        binding.customView.onLoadMoreItemClick=this
        adepter.itemClickListener=this
        setData()
    }


    private fun setData() {
        binding.customView.setTotalPages(3)
        (binding.customView.rvItems.adapter as TestAdepter).clear()
        testingList.add(R.drawable.a)
        testingList.add(R.drawable.b)
        testingList.add(R.drawable.c)
        testingList.add(R.drawable.d)
        testingList.add(R.drawable.e)
        testingList.add(R.drawable.f)
        testingList.add(R.drawable.g)
        testingList.add(R.drawable.h)


        (binding.customView.rvItems.adapter as TestAdepter).setItem(testingList)
        (binding.customView.rvItems.adapter as TestAdepter).notifyItemRangeInserted(0,testingList.size)

    }

}

