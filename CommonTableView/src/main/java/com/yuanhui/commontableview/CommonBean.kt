package com.yuanhui.commontableview

object MultipleItem {
    const val STYLE_TITLE = 0
    const val STYLE_1 = 1
    const val STYLE_2 = 2
    const val STYLE_3 = 3
    const val STYLE_4 = 4
}

class TableHeaderBean{
    var title = ""
    var level = 0
    var child: ArrayList<TableHeaderBean> = ArrayList()
    var multiItemType = MultipleItem.STYLE_2 //MultipleItem.STYLE_1: 第一列
    constructor()
    constructor(title: String) {
        this.title = title
    }
}

enum class RowType{
    Singular, // 单行
    Double //双行
}

interface FirstColumnEntity {
    fun getRowType(): RowType
    fun getFirstColumnText(): String
    fun getOtherColumnData(): ArrayList<String>
}

abstract class FirstColumnBean<T> : FirstColumnEntity {
    var t: T?

    constructor(t: T?) {
        this.t = t
    }
}

class ChlTableBean {
    var chlName = ""
    var chlId = ""
    var chlPId = ""
    var flag = ""  // 0：不能上下钻 1：可以上下钻
    var data: ArrayList<String> = ArrayList()
    var mRowType: RowType = RowType.Singular
}

class CommonTableItem(t: ChlTableBean) : FirstColumnBean<ChlTableBean>(t) {
    override fun getRowType(): RowType {
        return t?.mRowType ?: RowType.Singular
    }

    override fun getFirstColumnText(): String {
        return t?.chlName ?: ""
    }

    override fun getOtherColumnData(): ArrayList<String> {
        return t?.data ?: ArrayList()
    }
}


class CustomerTableItem : FirstColumnEntity {
    var city = ""
    var chlName = ""
    var chlId = ""
    var chlPId = ""
    var flag = ""  // 0：不能上下钻 1：可以上下钻
    var data: ArrayList<String> = ArrayList()
    var date = ""

    fun copyFirstData(bean: CustomerTableItem): CustomerTableItem {
        this.chlId = bean.chlId
        this.chlPId = bean.chlPId
        this.chlName = bean.chlName
        this.date = bean.date
        this.flag = bean.flag
        this.city = bean.city
        this.data.clear()
        return this
    }

    var mRowType: RowType = RowType.Singular
    override fun getRowType(): RowType {
        return mRowType
    }

    override fun getFirstColumnText(): String {
        return when {
            this.chlName.isNotEmpty() -> this.chlName
            this.date.isNotEmpty() -> this.date
            else -> ""
        }
    }

    override fun getOtherColumnData(): ArrayList<String> {
        return this.data
    }
}