@file:Suppress("DEPRECATION")

package com.yuanhui.commontableview.util

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.util.LruCache
import android.view.View
import androidx.recyclerview.widget.RecyclerView


object ShotViewUtil {

    /**
     * 屏幕可见区域的截图
     * @param view
     * @return
     */
    fun getNormalViewScreenshot(view: View): Bitmap? {
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        /**这一步很关键，释放绘图缓存，避免出现重复的镜像 */
        view.destroyDrawingCache()
        return bitmap
    }

    /**
     * 将一个view转换为Bitmap
     *
     * @param view
     * @return
     */
    fun convertViewToBitmap(view: View): Bitmap? {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        /**这一步很关键，释放绘图缓存，避免出现重复的镜像 */
        view.destroyDrawingCache()
        return bitmap
    }

    /**
     * 整个屏幕截图
     *
     * @return
     */
    fun shotScreen(activity: Activity): Bitmap? {
        val view = activity.window.decorView
        return view.drawingCache
    }

    fun interceptedScreen(activity: Activity): Bitmap? {
        val view = activity.window.decorView.rootView
        return view.drawingCache
    }

    fun shotHRecyclerView(view: RecyclerView): Bitmap? {
        val adapter = view.adapter
        var bigBitmap: Bitmap? = null
        if (adapter != null) {
            val size = adapter.itemCount
            var width = 0
            val paint = Paint()
            var iWidth = 0
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
            // Use 1/8th of the available memory for this memory cache.
            val cacheSize = maxMemory / 8
            val bitmapCache = LruCache<String, Bitmap>(cacheSize)
            for (i in 0 until size) {
                val holder = adapter.createViewHolder(view, adapter.getItemViewType(i))
                adapter.onBindViewHolder(holder, i)
                holder.itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(view.height, View.MeasureSpec.EXACTLY))
                holder.itemView.layout(0, 0, holder.itemView.measuredWidth,
                    holder.itemView.measuredHeight)
                holder.itemView.isDrawingCacheEnabled = true
                holder.itemView.buildDrawingCache()
                val drawingCache = holder.itemView.drawingCache
                if (drawingCache != null) {
                    bitmapCache.put(i.toString(), drawingCache)
                }
                width += holder.itemView.measuredWidth
            }
            bigBitmap = Bitmap.createBitmap(width, view.height, Bitmap.Config.ARGB_8888)
            val bigCanvas = Canvas(bigBitmap!!)
            val lBackground = view.background
            if (lBackground is ColorDrawable) {
                val lColor = lBackground.color
                bigCanvas.drawColor(lColor)
            }
            for (i in 0 until size) {
                val bitmap = bitmapCache[i.toString()]!!
                bigCanvas.drawBitmap(bitmap, iWidth.toFloat(),0f,  paint)
                iWidth += bitmap.width
                bitmap.recycle()
            }
        }
        return bigBitmap
    }

    fun shotRecyclerView(view: RecyclerView): Bitmap? {
        val adapter = view.adapter
        var bigBitmap: Bitmap? = null
        if (adapter != null) {
            val size = adapter.itemCount
            var height = 0
            val paint = Paint()
            var iHeight = 0
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

            // Use 1/8th of the available memory for this memory cache.
            val cacheSize = maxMemory / 8
            val bitmaCache = LruCache<String, Bitmap>(cacheSize)
            for (i in 0 until size) {
                val holder = adapter.createViewHolder(view, adapter.getItemViewType(i))
                adapter.onBindViewHolder(holder, i)
                holder.itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                holder.itemView.layout(
                    0, 0, holder.itemView.measuredWidth,
                    holder.itemView.measuredHeight
                )
                holder.itemView.isDrawingCacheEnabled = true
                holder.itemView.buildDrawingCache()
                val drawingCache = holder.itemView.drawingCache
                if (drawingCache != null) {
                    bitmaCache.put(i.toString(), drawingCache)
                }
                height += holder.itemView.measuredHeight
            }
            bigBitmap = Bitmap.createBitmap(view.measuredWidth, height, Bitmap.Config.ARGB_8888)
            val bigCanvas = Canvas(bigBitmap)
            val lBackground = view.background
            if (lBackground is ColorDrawable) {
                val lColor = lBackground.color
                bigCanvas.drawColor(lColor)
            }
            for (i in 0 until size) {
                val bitmap = bitmaCache[i.toString()]
                bigCanvas.drawBitmap(bitmap, 0f, iHeight.toFloat(), paint)
                iHeight += bitmap.height
                bitmap.recycle()
            }
        }
        return bigBitmap
    }

    fun getBitmapOption(inSampleSize: Int = 2): BitmapFactory.Options {
        System.gc()
        val options = BitmapFactory.Options()
        options.inPurgeable = true
        options.inSampleSize = inSampleSize
        return options
    }

}