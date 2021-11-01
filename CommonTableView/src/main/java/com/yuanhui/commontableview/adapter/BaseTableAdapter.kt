package com.yuanhui.commontableview.adapter

import android.annotation.SuppressLint
import android.content.Context
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yuanhui.commontableview.CustomerTableItem
import com.yuanhui.commontableview.util.DensityUtils
import com.yuanhui.commontableview.util.TableParam

/**
 * @Description:
 * @Author:         yu'anhui
 * @CreateDate:     2021/6/17 10:03
 * @UpdateUser:     yu'anhui
 * @UpdateDate:     2021/6/17 10:03
 * @UpdateRemark:
 * @Version:        1.0
 */
class BaseTableAdapter(context: Context, data: ArrayList<CustomerTableItem>) :
    BaseCommonTableAdapter<CustomerTableItem>(context, data = data) {

    private var selectChlId = ""
    private var columnPerWidthDP: Int = DensityUtils.dp2px(context, TableParam.columnPerWidthDP)

    @SuppressLint("ResourceType")
    override fun convert(holder: BaseViewHolder, item: CustomerTableItem) {
        // 如果BaseTableAdapter构造函数中没有layoutResId这个参数，便是采用默认的layout, 此处便调用super.convert(holder, item)；反之则不调用此方法
        super.convert(holder, item)
    }

    private var onChlClickListener: OnChlClickListener? = null
    fun setOnChlClickListener(onChlClickListener: OnChlClickListener) {
        this.onChlClickListener = onChlClickListener
    }
    interface OnChlClickListener {
        fun onChlClick(item: CustomerTableItem)
    }

    fun refreshData(data: ArrayList<CustomerTableItem>, columnPerWidth: Int) {
        this.columnPerWidthDP = columnPerWidth
        setColumnPerWidth(this.columnPerWidthDP)
        setList(data)
    }

    fun refreshData(data: ArrayList<CustomerTableItem>, columnPerWidth: Int, selectChlId: String) {
        this.columnPerWidthDP = columnPerWidth
        setColumnPerWidth(this.columnPerWidthDP)
        this.selectChlId = selectChlId
        setList(data)
    }


    override fun onFirstColumnClickListener(item: CustomerTableItem) {
        onChlClickListener?.onChlClick(item)
    }

}

