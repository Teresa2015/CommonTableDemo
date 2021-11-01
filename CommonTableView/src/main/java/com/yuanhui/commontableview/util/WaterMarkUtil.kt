package com.yuanhui.commontableview.util

import android.content.Context
import android.graphics.*
import java.lang.Math.tan

object WaterMarkUtil {

    /**
     * dip转pix
     *
     * @param context
     * @param dp
     * @return
     */
    private fun dp2px(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun getWaterMuchMarkBitmap(context: Context, bitmap: Bitmap, waterPrintText: String): Bitmap {
        val paintText = Paint()
        paintText.textSize = dp2px(context, 14f).toFloat()
        val rect = Rect()
        paintText.getTextBounds(waterPrintText, 0, waterPrintText.length, rect)
        val paintTextW = rect.width()

        val resourceBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        val canvas = Canvas(resourceBitmap)
        canvas.rotate(-15f)
        //创建透明画布
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.GRAY
        paint.alpha = 125
        paint.textSize = dp2px(context, 14f).toFloat()
        val perH = DensityUtils.dp2px(context, 120f)
        val perW = paintTextW + dp2px(context, 30f).toFloat()
        var startY =  perH / 2f
        val time = (bitmap.height / perH)
        for (i in 0..time) {
            var startX = 10f
            startX -= startY * tan((Math.PI * 15 / 180)).toFloat()
            while(startX < bitmap.width) {
                canvas.drawText(waterPrintText, startX, startY, paint)
                startX += perW
            }
            startY += perH
        }
        return resourceBitmap
    }
}