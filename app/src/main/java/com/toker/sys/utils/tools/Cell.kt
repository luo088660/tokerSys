package com.toker.sys.utils.tools

class Cell{
    var day: String? = null
    var month: String? = null
    private var mCurrDay: Int = 0
    var isSelect: Boolean = false

    fun getmCurrDay(): Int {
        return mCurrDay
    }

    fun setmCurrDay(mCurrDay: Int) {
        this.mCurrDay = mCurrDay
    }
}