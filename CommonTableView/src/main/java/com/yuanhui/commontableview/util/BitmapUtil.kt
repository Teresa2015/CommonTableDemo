package com.yuanhui.commontableview.util

import android.graphics.Bitmap
import android.graphics.Canvas


object BitmapUtil {
    /**
     * 纵向拼接
     */
    fun addBitmap(first: Bitmap, second: Bitmap): Bitmap {
        val width = Math.max(first.width, second.width)
        val height = first.height + second.height
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        canvas.drawBitmap(first, 0f, 0f, null)
        canvas.drawBitmap(second, 0f, first.height.toFloat(), null)
        return result
    }

    /**
     * 纵向拼接
     */
    fun addBitmap(first: Bitmap, second: Bitmap, third: Bitmap): Bitmap {
        var width = Math.max(first.width, second.width)
        width = Math.max(width, third.width)
        val height = first.height + second.height + third.height
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        canvas.drawBitmap(first, 0f, 0f, null)
        canvas.drawBitmap(second, 0f, first.height.toFloat(), null)
        canvas.drawBitmap(third, 0f, first.height.toFloat() + second.height, null)
        return result
    }

    fun addBitmapBg(bg: Bitmap, src: Bitmap): Bitmap {
        val canvas = Canvas(bg)
        canvas.drawBitmap(src, 0f, 0f, null)
        return bg
    }

    /**
     * 横向拼接
     */
    fun addHBitmap(first: Bitmap, second: Bitmap): Bitmap {
        val width = first.width + second.width
        val height = Math.max(first.height, second.height)
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        canvas.drawBitmap(first, 0f, 0f, null)
        canvas.drawBitmap(second, first.width.toFloat(), 0f, null)
        return result
    }
}