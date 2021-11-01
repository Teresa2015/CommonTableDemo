package com.yuanhui.commontableview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuanhui.commontableview.adapter.BaseReportHeaderAdapter
import com.yuanhui.commontableview.adapter.BaseTableAdapter
import com.yuanhui.commontableview.util.BitmapUtil
import com.yuanhui.commontableview.util.ShotViewUtil
import com.yuanhui.commontableview.util.TableParam
import com.yuanhui.commontableview.util.WaterMarkUtil
import java.io.ByteArrayOutputStream
import kotlin.collections.ArrayList

/**
 * 自定义 固定首页的tableView
 * Created by yuanhui on 2021/10/12.
 */
class CustomCommonTable : RelativeLayout {

    private var rvHeaderList: RecyclerView? = null
    private var rvList: RecyclerView? = null
    private var rvHeaderFirstColumn: RecyclerView? = null
    private var rvFirstColumn: RecyclerView? = null
    private var llFirstColumn: LinearLayout? = null

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
        addScrollerListener()
    }

    private fun addScrollerListener() {
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

    private var mHeaderAdapter: BaseReportHeaderAdapter? = null
    private var mTableAdapter: BaseTableAdapter? = null
    fun initView(reportHeadLvl: Int, hasCaliber: Boolean = false, onTableClickListener: OnTableClickListener){
        rvHeaderList?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mHeaderAdapter = BaseReportHeaderAdapter(context, ArrayList(), reportHeadLvl)
        rvHeaderList?.adapter = mHeaderAdapter
        mHeaderAdapter?.setHasCaliber(hasCaliber)
        mHeaderAdapter?.addChildClickViewIds(R.id.rlShare, R.id.rlCaliber)
        mHeaderAdapter?.setOnItemChildClickListener { adapter, view, position -> // reportHeadLvl = 2
            when(view.id) {
                R.id.rlShare -> {
                    onTableClickListener.onShareClick(adapter.data[position] as TableHeaderBean, position)
                }
                R.id.rlCaliber -> {
                    onTableClickListener.onCaliberClick(adapter.data[position] as TableHeaderBean, position - 1)
                }
            }
        }
        // reportHeadLvl = 3
        mHeaderAdapter?.setOnCaliberClickListener(object : BaseReportHeaderAdapter.OnCaliberClickListener{
            override fun onCaliberClick(item: TableHeaderBean, subIndex: Int, parentIndex: Int) {
                val adapterData = mHeaderAdapter!!.data
                var itemIndex = 0
                for (index in 1..parentIndex){
                    val bean = adapterData[index]
                    if (index == parentIndex){
                        itemIndex += subIndex
                        break
                    } else {
                        itemIndex += bean.child.size
                    }
                }
                onTableClickListener.onCaliberClick(item, itemIndex)
            }

        })

        rvList?.layoutManager = LinearLayoutManager(context)
        mTableAdapter = BaseTableAdapter(context, ArrayList())
        rvList?.adapter = mTableAdapter
        mTableAdapter?.setOnChlClickListener(object : BaseTableAdapter.OnChlClickListener {

            override fun onChlClick(item: CustomerTableItem) {
                onTableClickListener.onFirstColumnClick(item)
            }

        })

        initFirstColumnView(reportHeadLvl, onTableClickListener)
    }

    private var mHeaderFirstColumnAdapter: BaseReportHeaderAdapter? = null
    private var mFirstColumnAdapter: BaseTableAdapter? = null
    private fun initFirstColumnView(reportHeadLvl: Int, onTableClickListener: OnTableClickListener) {
        llFirstColumn?.visibility = View.VISIBLE
        rvHeaderFirstColumn?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mHeaderFirstColumnAdapter = BaseReportHeaderAdapter(context, ArrayList(), reportHeadLvl)
        rvHeaderFirstColumn?.adapter = mHeaderFirstColumnAdapter
        mHeaderFirstColumnAdapter?.setHasCaliber(false)

        rvFirstColumn?.layoutManager = LinearLayoutManager(context)
        mFirstColumnAdapter = BaseTableAdapter(context, ArrayList())
        rvFirstColumn?.adapter = mFirstColumnAdapter
        mFirstColumnAdapter?.setOnChlClickListener(object : BaseTableAdapter.OnChlClickListener {

            override fun onChlClick(item: CustomerTableItem) {
                onTableClickListener.onFirstColumnClick(item)
            }

        })
    }

    fun clearTableData() {
        mHeaderAdapter?.setList(ArrayList())
        mHeaderFirstColumnAdapter?.setList(ArrayList())
        mTableAdapter?.setList(ArrayList())
        mFirstColumnAdapter?.setList(ArrayList())
    }


    fun setTableData(headerData: ArrayList<TableHeaderBean>, bodyData: ArrayList<CustomerTableItem>){
        headerSize = 0
        Log.i("CustomCommonTable", "headerSize=$headerSize")
        for (item in headerData) {
            getTableHeaderSize(item)
        }
        Log.i("CustomCommonTable", "headerSize=$headerSize")
        if(headerData.size > 0) {
            headerData[0].multiItemType = MultipleItem.STYLE_1 // 第一列
        }
        val perWith = TableParam.getTopicColumnPerWidth(context, headerSize, 0f)
        mHeaderAdapter?.refreshData(headerData, perWith)

        for (index in bodyData.indices) {
            bodyData[index].mRowType = if (index % 2 != 0) {
                RowType.Singular
            } else {
                RowType.Double
            }
        }
        mTableAdapter?.refreshData(bodyData, perWith)

        val mHeader = ArrayList<TableHeaderBean>()
        mHeader.add(headerData[0])
        mHeaderFirstColumnAdapter?.refreshData(mHeader, perWith)
        val mFirstColumn = ArrayList<CustomerTableItem>()
        for (item in bodyData) {
            val column = CustomerTableItem()
            column.chlId = item.chlId
            column.chlName = item.chlName
            column.chlPId = item.chlPId
            column.date = item.date
            column.mRowType = item.mRowType
            mFirstColumn.add(column)
        }
        mFirstColumnAdapter?.refreshData(mFirstColumn, perWith)
    }

    private var headerSize = 0
    private fun getTableHeaderSize(item: TableHeaderBean) {
        if (item.child.size == 0) {
            headerSize++
            Log.i("CustomCommonTable", "getTableHeaderSize=$headerSize")
        } else {
            for (subItem in item.child) {
                getTableHeaderSize(subItem)
                Log.i("CustomCommonTable", "for-subItem")
            }
        }
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