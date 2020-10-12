package com.toker.sys.utils.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import kotlin.math.max
import kotlin.math.min

/**
 * 功能描述：一个简洁而高效的圆形ImageView
 */
class CircleImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatImageView(context, attrs, defStyleAttr) {
    private var width: Float = 0.toFloat()
    private var height: Float = 0.toFloat()
    private var radius: Float = 0.toFloat()
    private val paint: Paint = Paint()
    private val matrixl: Matrix

    init {
        paint.isAntiAlias = true   //设置抗锯齿
        matrixl = Matrix()
    }

    /**
     * 测量控件的宽高，并获取其内切圆的半径
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = measuredWidth.toFloat()
        height = measuredHeight.toFloat()
        radius = min(width, height) / 2
    }

    override fun onDraw(canvas: Canvas) {
        val drawable = drawable
        if (drawable == null) {
            super.onDraw(canvas)
            return
        }
        if (drawable is BitmapDrawable) {
            try {
                paint.shader = initBitmapShader(drawable)//将着色器设置给画笔
                canvas.drawCircle(width / 2, height / 2, radius, paint)//使
                return
            } catch (e: Exception) {
                return
            }

        }
        super.onDraw(canvas)
    }

    /**
     * 获取ImageView中资源图片的Bitmap，利用Bitmap初始化图片着色器,通过缩放矩阵将原资源图片缩放到铺满整个绘制区域，避免边界填充
     */
    private fun initBitmapShader(drawable: BitmapDrawable): BitmapShader {
        val bitmap = drawable.bitmap
        val bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val scale = max(width / bitmap.width, height / bitmap.height)
        matrixl.setScale(scale, scale)//将图片宽高等比例缩放，避免拉伸
        bitmapShader.setLocalMatrix(matrixl)
        return bitmapShader
    }
}
