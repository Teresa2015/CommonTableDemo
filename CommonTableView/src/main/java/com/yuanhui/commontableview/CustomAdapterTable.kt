package com.yuanhui.commontableview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.yuanhui.commontableview.util.BitmapUtil
import com.yuanhui.commontableview.util.ShotViewUtil
import com.yuanhui.commontableview.util.WaterMarkUtil
import java.io.ByteArrayOutputStream
import kotlin.collections.ArrayList

/**
 * 自定义 固定首页的tableView
 * 自定义adapter的tableView
 * Created by yuanhui on 2021/10/12.
 */
class CustomAdapterTable : RelativeLayout {

    protected var rvHeaderList: RecyclerView? = null
    protected var rvList: RecyclerView? = null
    protected var rvHeaderFirstColumn: RecyclerView? = null
    protected var rvFirstColumn: RecyclerView? = null
    protected var llFirstColumn: LinearLayout? = null

    init { //init 先于 constructor构造函数调用， addView在init中调用。否则，在xml真正使用自定义view时，onCreate方法中调用类似 findViewById会出现view为null
        //MyLogger.i(TAG, "init")
        val view = View.inflate(context, R.layout.layout_cmmon_table_view, null)
        addView(view)
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    @SuppressLint("NewApi")
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        rvHeaderFirstColumn = findViewById(R.id.rvHeaderFirstColumn)
        rvFirstColumn = findViewById(R.id.rvFirstColumn)
        rvList = findViewById(R.id.rvList)
        rvHeaderList = findViewById(R.id.rvHeaderList)
        llFirstColumn = findViewById(R.id.llFirstColumn)

        addScrollListener()
    }

    private fun addScrollListener() {
        val scrollListeners: ArrayList<RecyclerView.OnScrollListener> = ArrayList()
        scrollListeners.add(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                rvFirstColumn?.removeOnScrollListener(scrollListeners[1])
                rvFirstColumn?.scrollBy(dx, dy)
                rvFirstColumn?.addOnScrollListener(scrollListeners[1])
            }
        })
        scrollListeners.add(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                rvList?.removeOnScrollListener(scrollListeners[0])
                rvList?.scrollBy(dx, dy)
                rvList?.addOnScrollListener(scrollListeners[0])
            }
        })
        rvList?.addOnScrollListener(scrollListeners[0])
        rvFirstColumn?.addOnScrollListener(scrollListeners[1])
    }

    fun getShotBitmap(activity: Activity, waterPrintText: String): Bitmap {
        val topBitmap = ShotViewUtil.shotHRecyclerView(rvHeaderList!!)
        val listBitmap = ShotViewUtil.shotRecyclerView(rvList!!)
        val result = BitmapUtil.addBitmap(topBitmap!!, listBitmap!!)
        val bos = ByteArrayOutputStream()
        result.compress(Bitmap.CompressFormat.JPEG, 70, bos)
        val bytes = bos.toByteArray()
        val shareBitmap =
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size, ShotViewUtil.getBitmapOption(3))
        return WaterMarkUtil.getWaterMuchMarkBitmap(activity, shareBitmap, waterPrintText)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

}