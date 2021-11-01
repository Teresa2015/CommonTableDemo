package com.yuanhui.commontableview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yuanhui.commontableview.MultipleItem
import com.yuanhui.commontableview.R
import com.yuanhui.commontableview.TableHeaderBean
import com.yuanhui.commontableview.util.DensityUtils
import com.yuanhui.commontableview.util.TableParam

/**
 * @Description:
 * @Author:         yu'anhui
 * @CreateDate:     2021/10/21 10:03
 * @UpdateUser:     yu'anhui
 * @UpdateDate:     2021/10/21 10:03
 * @UpdateRemark:
 * @Version:        1.0
 */
class BaseReportHeaderAdapter(context: Context, data: ArrayList<TableHeaderBean>, private var reportHeadLvl: Int = 2) :
    BaseQuickAdapter<TableHeaderBean, BaseViewHolder>(R.layout.item_kpi_topic_header_level_2, data) {

    private var columnPerWidth: Int = DensityUtils.dp2px(context, TableParam.columnPerWidthDP)
    private var columnPerHeight: Int = DensityUtils.dp2px(context, TableParam.columnPerHeightDP)

    private var hasCaliber: Boolean = true

    fun setHasCaliber(hasCaliber: Boolean){
        this.hasCaliber = hasCaliber
    }

    @SuppressLint("ResourceType")
    override fun convert(holder: BaseViewHolder, item: TableHeaderBean) {
        holder.setText(R.id.tvTitle, item.title)

        when(reportHeadLvl) {
            2 -> {
                dealLevel2View(holder, item)
            }
            3 -> {
                dealLevel3View(holder, item)
            }
        }
    }

    private fun dealLevel2View(helper: BaseViewHolder, item: TableHeaderBean) {
        if (item.multiItemType == MultipleItem.STYLE_1) { // 第一列
            helper.getView<RelativeLayout>(R.id.rlShare).visibility = View.GONE
        } else {
            helper.getView<RelativeLayout>(R.id.rlShare).visibility = View.VISIBLE
            helper.getView<RelativeLayout>(R.id.rlCaliber).visibility = if (hasCaliber) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        val llContainer = helper.getView<LinearLayout>(R.id.llContainer)
        llContainer.removeAllViews()
        val itemData = item.child

        for (index in itemData.indices) {
            val bean = itemData[index]
            val viewRL = View.inflate(helper.itemView.context, R.layout.layout_kpi_table_header_cell, null)
            val textView = viewRL.findViewById<TextView>(R.id.tvCell)
            val param2 = ViewGroup.LayoutParams(columnPerWidth, columnPerHeight)
            viewRL.layoutParams = param2
            textView.gravity = Gravity.CENTER
            textView.text = bean.title

            llContainer.addView(viewRL)
        }
    }

    private fun dealLevel3View(helper: BaseViewHolder, item: TableHeaderBean) {
        helper.getView<RelativeLayout>(R.id.rlCaliber).visibility = View.GONE
        helper.getView<RelativeLayout>(R.id.rlShare).visibility = View.VISIBLE

        val llContainer = helper.getView<LinearLayout>(R.id.llContainer)
        llContainer.removeAllViews()
        val itemData = item.child

        if (item.multiItemType == MultipleItem.STYLE_1) {  // 第一列
            helper.getView<RelativeLayout>(R.id.rlShare).visibility = View.GONE
            if (itemData.size <= 0) return
            val bean = itemData[0]
            val viewRL = View.inflate(helper.itemView.context, R.layout.layout_kpi_table_header_cell, null)
            val textView = viewRL.findViewById<TextView>(R.id.tvCell)
            val param2 = LinearLayout.LayoutParams(columnPerWidth, columnPerHeight * 2)
            viewRL.layoutParams = param2
            textView.gravity = Gravity.CENTER
            textView.text = bean.title

            llContainer.addView(viewRL)
        } else {
            for (index in itemData.indices) {
                val subBean = itemData[index]
                val llSubContainer = View.inflate(helper.itemView.context, R.layout.item_kpi_topic_header_level_3, null)
                val tvTitle = llSubContainer.findViewById<TextView>(R.id.tvTitle)
                tvTitle.text = subBean.title
                val rlTitle = llSubContainer.findViewById<RelativeLayout>(R.id.rlTitle)
                val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, columnPerHeight)
                rlTitle.layoutParams = param
                val rlCaliber = llSubContainer.findViewById<RelativeLayout>(R.id.rlCaliber)
                if (hasCaliber) {
                    rlCaliber.visibility = View.VISIBLE
//                    rlCaliber.setOnClickListener {
//                        this.onCaliberClickListener?.onCaliberClick(subBean, index, helper.layoutPosition)
//                    }
                } else {
                    rlCaliber.visibility = View.GONE
                }

                if (subBean.child.size > 0) {
                    val subContainer = llSubContainer.findViewById<LinearLayout>(R.id.llContainer)
                    subContainer.removeAllViews()
                    for (bean in subBean.child) {
                        val viewRL = View.inflate(helper.itemView.context, R.layout.layout_kpi_table_header_cell, null)
                        val textView = viewRL.findViewById<TextView>(R.id.tvCell)
                        val param2 = LinearLayout.LayoutParams(columnPerWidth, columnPerHeight)
                        viewRL.layoutParams = param2
                        textView.gravity = Gravity.CENTER
                        textView.text = bean.title
                        subContainer.addView(viewRL)
                    }
                }
                llContainer.addView(llSubContainer)
            }
        }
    }

    private var onCaliberClickListener: OnCaliberClickListener? = null
    fun setOnCaliberClickListener(OnCaliberClickListener: OnCaliberClickListener){
        this.onCaliberClickListener = OnCaliberClickListener
    }
    interface OnCaliberClickListener {
        fun onCaliberClick(item: TableHeaderBean, subIndex: Int, parentIndex: Int)
    }

    fun refreshData(data: ArrayList<TableHeaderBean>, columnPerWidth: Int) {
        this.columnPerWidth = columnPerWidth
        setList(data)
    }

    fun refreshData(data: ArrayList<TableHeaderBean>, columnPerWidth: Int, reportHeadLvl: Int) {
        this.reportHeadLvl = reportHeadLvl
        this.columnPerWidth = columnPerWidth
        setList(data)
    }

}