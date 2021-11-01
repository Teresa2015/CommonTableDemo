package com.yuanhui.commontableview

interface OnFirstColumnClickListener {
    fun onFirstColumnClick(item: CustomerTableItem)
}

interface OnTableClickListener {
    fun onShareClick(item: TableHeaderBean, position: Int)
    fun onCaliberClick(item: TableHeaderBean, position: Int)
    fun onFirstColumnClick(item: CustomerTableItem)
}