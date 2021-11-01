package com.yuanhui.commontableview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yuanhui.commontableview.FirstColumnEntity
import com.yuanhui.commontableview.R
import com.yuanhui.commontableview.RowType
import com.yuanhui.commontableview.util.DensityUtils
import com.yuanhui.commontableview.util.TableParam

/**
 * @Description: 表格主体内容适配器。layoutResId：默认布局为R.layout.item_table_row
 * @Author:         yu anhui
 * @CreateDate:     2021/10/21 10:03
 * @UpdateUser:     yu anhui
 * @UpdateDate:     2021/10/21 10:03
 * @UpdateRemark:
 * @Version:        1.0
 */
abstract class BaseCommonTableAdapter<T : FirstColumnEntity>(context: Context, @LayoutRes private val layoutResId: Int = R.layout.item_table_row, data: ArrayList<T>)
    : BaseQuickAdapter<T, BaseViewHolder>(layoutResId, data) {

    private var columnPerWidth: Int = DensityUtils.dp2px(context, TableParam.columnPerWidthDP)
    private var columnPerHeight: Int = DensityUtils.dp2px(context, TableParam.columnPerHeightDP)

    override fun convert(holder: BaseViewHolder, item: T) {
        when(this.layoutResId) {
            R.layout.item_table_row -> {
                convertDefaultItem(holder, item)
            }
        }
    }

    @SuppressLint("ResourceType", "InflateParams")
    private fun convertDefaultItem(holder: BaseViewHolder, item: T) {
        val attrsArray = intArrayOf(R.attr.customTableOddBgColor, R.attr.customTableEvenBgColor,
            R.attr.customTableEvenTextColor, R.attr.customTableSubItemNegativeTextColor)
        val typedArray= holder.itemView.context.theme.obtainStyledAttributes(attrsArray)
        val negativeColor = typedArray.getColor(3, Color.parseColor("#37c200"))
        val textColor = typedArray.getColor(2, Color.parseColor("#37c200"))

        val context = holder.itemView.context
        val llContainer = holder.getView<LinearLayout>(R.id.llContainer)
        llContainer.removeAllViews()

        if (item.getRowType() == RowType.Singular) {
            llContainer.setBackgroundColor(typedArray.getColor(0, Color.parseColor("#202020")))
        } else {
            llContainer.setBackgroundColor(typedArray.getColor(1, Color.parseColor("#282828")))
        }
        typedArray.recycle()

        val view = LayoutInflater.from(context).inflate(R.layout.layout_kpi_table_cell, null)
        val layoutPara = RelativeLayout.LayoutParams(columnPerWidth, columnPerHeight)
        view.layoutParams = layoutPara
        val title = view.findViewById<AppCompatTextView>(R.id.tvCell)
        title.text = item.getFirstColumnText()

        title.setTextColor(textColor)
        title.setOnClickListener {
            onFirstColumnClickListener(item)
        }
        llContainer.addView(view)

        val tableDate = item.getOtherColumnData()
        for (index in tableDate.indices) {
            val itemView = LayoutInflater.from(context).inflate(R.layout.layout_kpi_table_cell, null)
            val layoutParam = RelativeLayout.LayoutParams(columnPerWidth, columnPerHeight)
            itemView.layoutParams = layoutParam
            val title1 = itemView.findViewById<AppCompatTextView>(R.id.tvCell)
            try{
                val itemData = tableDate[index]
                title1.text = itemData
                if (itemData.startsWith("-") && itemData != "-") {
                    title1.setTextColor(negativeColor)
                } else {
                    title1.setTextColor(textColor)
                }
            } catch (e: Exception) {
                Log.e("BaseTableAdapter","BaseTableAdapter: ${e.message}")
            }

            llContainer.addView(itemView)
        }
    }

    fun setColumnPerWidth(columnPerWidth: Int) {
        this.columnPerWidth = columnPerWidth
    }

    protected abstract fun onFirstColumnClickListener(item: T)
}
