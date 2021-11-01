package com.yuanhui.commontable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.yuanhui.commontableview.CustomCommonTable
import com.yuanhui.commontableview.CustomerTableItem
import com.yuanhui.commontableview.OnTableClickListener
import com.yuanhui.commontableview.TableHeaderBean

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private var ctList: CustomCommonTable? = null
    private fun initView() {
        ctList = findViewById(R.id.ctList)
        ctList?.initView(2, true, object : OnTableClickListener {
            override fun onShareClick(item: TableHeaderBean, position: Int) {
                Toast.makeText(this@MainActivity, "onShareClick: ${item.title}", Toast.LENGTH_SHORT).show()
            }

            override fun onCaliberClick(item: TableHeaderBean, position: Int) {
                Toast.makeText(this@MainActivity, "onCaliberClick: ${item.title}", Toast.LENGTH_SHORT).show()
            }

            override fun onFirstColumnClick(item: CustomerTableItem) {
                Toast.makeText(this@MainActivity, item.chlName, Toast.LENGTH_SHORT).show()
            }

        })
        ctList?.clearTableData()
        setData()
        Log.i("main", "initView")
    }

    private fun setData() {
        Log.i("main", "setData")
        val headerData = ArrayList<TableHeaderBean>()
        val header = TableHeaderBean("项目")
        header.child.add(TableHeaderBean("地市"))
        headerData.add(header)
        for (index in 0..3) {
            val item  = TableHeaderBean()
            item.title = "item-$index"
            for (subIndex in 0..2) {
                val subItem  = TableHeaderBean()
                subItem.title = "subItem-$index"
                item.child.add(subItem)
            }
            headerData.add(item)
        }

        val contentData = ArrayList<CustomerTableItem>()
        for (index in 0..15) {
            val item  = CustomerTableItem()
            item.chlName = "chlName-$index"
            for (subIndex in 0..11) {
                item.data.add("content-$index-$subIndex")
            }
            contentData.add(item)
        }
        ctList?.setTableData(headerData, contentData)
    }
}