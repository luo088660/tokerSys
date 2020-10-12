package com.toker.sys.utils.tools

import android.graphics.Color
import android.graphics.Canvas
import android.graphics.Bitmap
import android.view.View


object BitmapUtil {
    fun createBitmap3(v: View, width: Int, height: Int): Bitmap {
        //测量使得view指定大小
        val measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        v.measure(measuredWidth, measuredHeight)
        //调用layout方法布局后，可以得到view的尺寸大小
        v.layout(0, 0, v.measuredWidth, v.measuredHeight)
        val bmp = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        c.drawColor(Color.WHITE)
        v.draw(c)
        return bmp
    }

}