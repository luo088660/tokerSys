package com.toker.sys.utils.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView
import kotlin.math.abs

class FScrollView(context: Context, attrs: AttributeSet) : ScrollView(context, attrs) {
    private var mLastXIntercept = 0f
    private var mLastYIntercept = 0f

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercepted = false
        val x = ev.x
        val y = ev.y
        val action = ev.action and MotionEvent.ACTION_MASK
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                intercepted = false
                //初始化mActivePointerId
                super.onInterceptTouchEvent(ev)
            }
            MotionEvent.ACTION_MOVE -> {
                //横坐标位移增量
                val deltaX = x - mLastXIntercept
                //纵坐标位移增量
                val deltaY = y - mLastYIntercept
                intercepted = abs(deltaX) < abs(deltaY)
            }
            MotionEvent.ACTION_UP -> {
                intercepted = false
            }
        }
        mLastXIntercept = x
        mLastYIntercept = y
        return intercepted
    }
}