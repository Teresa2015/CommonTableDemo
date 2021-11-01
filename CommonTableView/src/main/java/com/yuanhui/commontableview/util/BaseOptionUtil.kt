package com.yuanhui.commontableview.util

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

object TableParam {
    const val columnPerWidthDP = 70f
    const val columnPerHeightDP = 35f

    fun getColumnPerWidth(context: Context, columnSize: Int, margin: Float = 10f): Int {
        var perWith = DensityUtils.dp2px(context, columnPerWidthDP)
        val tableWidth = context.resources.displayMetrics.widthPixels -
                DensityUtils.dp2px(context, margin)
        if (perWith * columnSize < tableWidth) {
            if (columnSize > 1) {
                perWith = (tableWidth - perWith) / (columnSize - 1)
            }
        }
        return perWith
    }

    fun getTopicColumnPerWidth(context: Context, columnSize: Int, margin: Float = 10f): Int {
        var perWith = DensityUtils.dp2px(context, columnPerWidthDP)
        val tableWidth = context.resources.displayMetrics.widthPixels -
                DensityUtils.dp2px(context, margin)
        if (perWith * columnSize < tableWidth) {
            if (columnSize > 0) {
                perWith = tableWidth / columnSize
            }
        }
        return perWith
    }
}

object CommonTableUtil {
    fun addScrollerListener(rvList: RecyclerView, rvFirstColumn: RecyclerView) {
        val scrollListeners: ArrayList<RecyclerView.OnScrollListener> = ArrayList()
        scrollListeners.add(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                rvFirstColumn.removeOnScrollListener(scrollListeners[1])
                rvFirstColumn.scrollBy(dx, dy)
                rvFirstColumn.addOnScrollListener(scrollListeners[1])
            }
        })
        scrollListeners.add(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                rvList.removeOnScrollListener(scrollListeners[0])
                rvList.scrollBy(dx, dy)
                rvList.addOnScrollListener(scrollListeners[0])
            }
        })
        rvList.addOnScrollListener(scrollListeners[0])
        rvFirstColumn.addOnScrollListener(scrollListeners[1])
    }
}