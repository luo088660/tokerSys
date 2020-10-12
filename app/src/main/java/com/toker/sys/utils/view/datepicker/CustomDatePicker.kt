package com.toker.sys.utils.view.datepicker

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.utils.tools.LogUtils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar

/**
 * 说明：自定义时间选择器
 * 作者：liuwan1992
 * 添加时间：2016/9/28
 * 修改人：liuwan1992
 * 修改时间：2018/12/21
 */
class CustomDatePicker
/**
 * 通过时间戳初始换时间选择器，毫秒级别
 *
 * @param context        Activity Context
 * @param callback       选择结果回调
 * @param beginTimestamp 毫秒级时间戳
 * @param endTimestamp   毫秒级时间戳
 */
    (private var mContext: Context?) : View.OnClickListener,
    PickerView.OnSelectListener, RadioGroup.OnCheckedChangeListener {


    var beginTimestamp: Long =
        DateFormatUtils.str2Long(mContext!!.getString(R.string.star_time), false)
    var endTimestamp: Long =
        DateFormatUtils.str2Long(mContext!!.getString(R.string.end_time), false)

    // 切换时间格式
    private var isVisiTab = true
    //切换开始 结束时间
    private var isSEndTime = true

    private var mCallback: Callback? = null
    private var mCallbackListen: CallbackListen? = null
    private var mCallSTListen: CallbackSEListen? = null
    private val mBeginTime: Calendar
    private val mEndTime: Calendar
    private val mSelectedTime: Calendar
    private var mCanDialogShow: Boolean = false

    private var mPickerDialog: Dialog? = null
    private var mDpvYear: PickerView? = null
    private var mDpvMonth: PickerView? = null
    private var mDpvDay: PickerView? = null
    private var mDpvHour: PickerView? = null
    private var mDpvMinute: PickerView? = null
    private var mTvHourUnit: TextView? = null
    private var mTvMinuteUnit: TextView? = null
    private var mTvDayUnit: TextView? = null
    private var mTvMonthUnit: TextView? = null

    private var mBeginYear: Int = 0
    private var mBeginMonth: Int = 0
    private var mBeginDay: Int = 0
    private var mBeginHour: Int = 0
    private var mBeginMinute: Int = 0
    private var mEndYear: Int = 0
    private var mEndMonth: Int = 0
    private var mEndDay: Int = 0
    private var mEndHour: Int = 0
    private var mEndMinute: Int = 0
    private val mYearUnits = ArrayList<String>()
    private val mMonthUnits = ArrayList<String>()
    private val mDayUnits = ArrayList<String>()
    private val mHourUnits = ArrayList<String>()
    private val mMinuteUnits = ArrayList<String>()
    private val mDecimalFormat = DecimalFormat("00")

    private var mCanShowPreciseTime: Boolean = false
    private var mScrollUnits = SCROLL_UNIT_HOUR + SCROLL_UNIT_MINUTE
    //开始时间
    private var startTime: Long = 0
    //结束时间
    private var endTime: Long = 0
    private var rg01: RadioGroup? = null
    private var rg02: RadioGroup? = null
    private var timeType: Int = 0
    private var rbDate01: RadioButton? = null
    private var rbDate02: RadioButton? = null
    private var rbPick01: RadioButton? = null
    private var rbPick02: RadioButton? = null
    private var rbPick03: RadioButton? = null
    private var rbPick04: RadioButton? = null
    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("yyyy/MM/dd")

    private var isStarEnd = true

    /**
     * 时间选择结果回调接口
     */
    interface Callback {
        fun onTimeSelected(timestamp: Long)
    }

    interface CallbackListen {
        fun onTimeSelected(timestamp: Long, timeType: Int)
    }

    interface CallbackSEListen {
        fun onTimeSelected(startTime: Long, endTime: Long, timeType: Int)
    }

    constructor(mContext: Context?, isYMD: Int, isVisiTab: Boolean, isSEndTime: Boolean) : this(
        mContext
    ) {
        LogUtils.d("CustomDatePicker", "isYMD:$isYMD ");
        LogUtils.d("CustomDatePicker", "isVisiTab:$isVisiTab ");
        LogUtils.d("CustomDatePicker", "isSEndTime:$isSEndTime ");
        this.isVisiTab = isVisiTab
        this.isSEndTime = isSEndTime
        rg01?.visibility = if (isVisiTab) View.VISIBLE else View.GONE
        rg02?.visibility = if (isSEndTime) View.VISIBLE else View.GONE
        rbPick04?.visibility = if (isSEndTime) View.VISIBLE else View.GONE
        if (!isSEndTime) {
            rbPick03?.setBackgroundResource(R.drawable.right_selector)
            rg01?.check(R.id.tv_date_pick_03)
        }
        when (isYMD) {
            2 -> {
                rbPick03?.visibility = View.GONE
                mDpvDay?.visibility = View.GONE
                rg01?.check(R.id.tv_date_pick_02)
                timeType = 2

            }
            3 -> {
                //隐藏年
                rbPick01?.visibility = View.GONE
                rbPick02?.setBackgroundResource(R.drawable.left_selector)
                rg01?.check(R.id.tv_date_pick_02)
                timeType = 2
            }
            4 -> {
                //全部显示 默认选择月
                rg02?.visibility = View.GONE
                rg01?.check(R.id.tv_date_pick_02)
                timeType = 2
            }
            5 -> {
                //全部显示 默认选择月
                rg02?.visibility = View.GONE
                rg01?.check(R.id.tv_date_pick_03)
                timeType = 3
            }
            6 -> {
                rg02?.visibility = View.VISIBLE
                //全部显示 默认选择月
                rg01?.check(R.id.tv_date_pick_03)
                timeType = 3
            }
        }

    }

    /**
     * isYMD  1 隐藏月 日,2 隐藏 日， 3，隐藏年
     *isVisiTab 影藏年月日
     * isSEndTime 隐藏 开始结束时间
     *
     */
    constructor(
        mContext: Context?,
        mCallback: Callback?,
        isYMD: Int,
        isVisiTab: Boolean,
        isSEndTime: Boolean
    ) : this(mContext, isYMD, isVisiTab, isSEndTime) {
        this.mCallback = mCallback

    }

    constructor(
        mContext: Context?,
        mCallback: CallbackListen?,
        isYMD: Int,
        isVisiTab: Boolean,
        isSEndTime: Boolean
    ) : this(mContext, isYMD, isVisiTab, isSEndTime) {
        this.mCallbackListen = mCallback
    }

    /**
     * 获取开始时间和结束时间
     */

    constructor(
        mContext: Context?,
        mCallback: CallbackSEListen?,
        isYMD: Int,
        isVisiTab: Boolean,
        isSEndTime: Boolean
    ) : this(mContext, isYMD, isVisiTab, isSEndTime) {
        this.mCallSTListen = mCallback
    }


    constructor (
        mContext: Context?,
        mCallback: Callback?,
        beginTimestamp: Long,
        endTimestamp: Long
    ) : this(
        mContext
    ) {
        LogUtils.d("CustomDatePicker", "constructor: ");
        this.beginTimestamp = beginTimestamp
        this.endTimestamp = endTimestamp
        this.mCallback = mCallback
    }

    /**
     * 通过日期字符串初始换时间选择器
     *
     * @param context      Activity Context
     * @param callback     选择结果回调
     * @param beginDateStr 日期字符串，格式为 yyyy-MM-dd HH:mm
     * @param endDateStr   日期字符串，格式为 yyyy-MM-dd HH:mm
     */
    constructor(
        context: Context,
        callback: Callback,
        beginDateStr: String,
        endDateStr: String
    ) : this(
        context, callback, DateFormatUtils.str2Long(beginDateStr, true),
        DateFormatUtils.str2Long(endDateStr, true)
    )


    init {
        LogUtils.d("CustomDatePicker", "init: ");
        if (mContext == null || mCallback == null || beginTimestamp!! <= 0 || beginTimestamp!! >= endTimestamp!!) {
            mCanDialogShow = false
        }
        mBeginTime = Calendar.getInstance()
        mBeginTime.timeInMillis = beginTimestamp!!
        mEndTime = Calendar.getInstance()
        mEndTime.timeInMillis = endTimestamp!!
        mSelectedTime = Calendar.getInstance()

        initView()
        initData()
        mCanDialogShow = true
    }

    private fun initView() {
        mPickerDialog = Dialog(mContext, R.style.date_picker_dialog)
        mPickerDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mPickerDialog!!.setContentView(R.layout.dialog_date_picker)

        val window = mPickerDialog!!.window
        if (window != null) {
            val lp = window.attributes
            lp.gravity = Gravity.BOTTOM
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = lp
        }
        rg01 = mPickerDialog!!.findViewById<RadioGroup>(R.id.rg_time_01)
        rg02 = mPickerDialog!!.findViewById<RadioGroup>(R.id.rg_time_02)
        rbDate01 = mPickerDialog!!.findViewById<RadioButton>(R.id.rb_date_se_01)
        rbDate02 = mPickerDialog!!.findViewById<RadioButton>(R.id.rb_date_se_02)
        rbPick01 = mPickerDialog!!.findViewById<RadioButton>(R.id.tv_date_pick_01)
        rbPick02 = mPickerDialog!!.findViewById<RadioButton>(R.id.tv_date_pick_02)
        rbPick03 = mPickerDialog!!.findViewById<RadioButton>(R.id.tv_date_pick_03)
        rbPick04 = mPickerDialog!!.findViewById<RadioButton>(R.id.tv_date_pick_04)
        rg01?.check(R.id.tv_date_pick_04)
        rg02?.check(R.id.rb_date_se_01)
        rg01?.setOnCheckedChangeListener(this)
        rg02?.setOnCheckedChangeListener(this)

        mPickerDialog!!.findViewById<View>(R.id.tv_cancel).setOnClickListener(this)
        mPickerDialog!!.findViewById<View>(R.id.tv_confirm).setOnClickListener(this)
        mPickerDialog!!.findViewById<View>(R.id.tv_date_pick_01).setOnClickListener(this)
        mPickerDialog!!.findViewById<View>(R.id.tv_date_pick_02).setOnClickListener(this)
        mPickerDialog!!.findViewById<View>(R.id.tv_date_pick_03).setOnClickListener(this)
        mPickerDialog!!.findViewById<View>(R.id.tv_date_pick_04).setOnClickListener(this)

        mTvHourUnit = mPickerDialog!!.findViewById(R.id.tv_hour_unit)
        mTvMinuteUnit = mPickerDialog!!.findViewById(R.id.tv_minute_unit)
        mTvDayUnit = mPickerDialog!!.findViewById(R.id.tv_day_unit)
        mTvMonthUnit = mPickerDialog!!.findViewById(R.id.tv_month_unit)


        mDpvYear = mPickerDialog!!.findViewById(R.id.dpv_year)
        mDpvYear!!.setOnSelectListener(this)
        mDpvMonth = mPickerDialog!!.findViewById(R.id.dpv_month)
        mDpvMonth!!.setOnSelectListener(this)
        mDpvDay = mPickerDialog!!.findViewById(R.id.dpv_day)
        mDpvDay!!.setOnSelectListener(this)
        mDpvHour = mPickerDialog!!.findViewById(R.id.dpv_hour)
        mDpvHour!!.setOnSelectListener(this)
        mDpvMinute = mPickerDialog!!.findViewById(R.id.dpv_minute)
        mDpvMinute!!.setOnSelectListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_cancel -> {
                if (mPickerDialog != null && mPickerDialog!!.isShowing) {
                    mPickerDialog!!.dismiss()
                }
            }

            R.id.tv_confirm -> {
                mCallback?.onTimeSelected(mSelectedTime.timeInMillis)
                mCallbackListen?.onTimeSelected(mSelectedTime.timeInMillis, timeType)

                //选择开始和结束时间
                if (mCallSTListen != null) {
                    //开始时间不能大于结束时间
//                    if (startTime > endTime && timeType == 0) return
                    if (timeType==0){
                        mCallSTListen!!.onTimeSelected(startTime, endTime, timeType)
                    }else{
                        mCallSTListen!!.onTimeSelected(mSelectedTime.timeInMillis, endTime, timeType)
                    }


                }


                mPickerDialog!!.dismiss()
            }
        }


    }

    override fun onSelect(view: View, selected: String) {
        if (view == null || TextUtils.isEmpty(selected)) return

        val timeUnit: Int
        try {
            val selected = selected.substring(0, selected.length - 1)
            timeUnit = Integer.parseInt(selected)
        } catch (ignored: Throwable) {
            return
        }

        when (view.id) {
            R.id.dpv_year -> {
                mSelectedTime.set(Calendar.YEAR, timeUnit)
                linkageMonthUnit(true, LINKAGE_DELAY_DEFAULT)
            }

            R.id.dpv_month -> {
                // 防止类似 2018/12/31 滚动到11月时因溢出变成 2018/12/01
                val lastSelectedMonth = mSelectedTime.get(Calendar.MONTH) + 1
                mSelectedTime.add(Calendar.MONTH, timeUnit - lastSelectedMonth)
                linkageDayUnit(true, LINKAGE_DELAY_DEFAULT)
            }

            R.id.dpv_day -> {
                mSelectedTime.set(Calendar.DAY_OF_MONTH, timeUnit)
                linkageHourUnit(true, LINKAGE_DELAY_DEFAULT)
            }

            R.id.dpv_hour -> {
                mSelectedTime.set(Calendar.HOUR_OF_DAY, timeUnit)
                linkageMinuteUnit(true)
            }

            R.id.dpv_minute -> mSelectedTime.set(Calendar.MINUTE, timeUnit)
        }

        (if (isStarEnd) rbDate01 else rbDate02)?.text = sdf.format(mSelectedTime.timeInMillis)

        if (isStarEnd) startTime = mSelectedTime.timeInMillis else endTime =
            mSelectedTime.timeInMillis

        LogUtils.d("CustomDatePicker", "timeType:$timeType ");
    }

    private fun initData() {
        mSelectedTime.timeInMillis = mBeginTime.timeInMillis

        mBeginYear = mBeginTime.get(Calendar.YEAR)
        // CalendarUtils.MONTH 值为 0-11
        mBeginMonth = mBeginTime.get(Calendar.MONTH) + 1
        mBeginDay = mBeginTime.get(Calendar.DAY_OF_MONTH)
        mBeginHour = mBeginTime.get(Calendar.HOUR_OF_DAY)
        mBeginMinute = mBeginTime.get(Calendar.MINUTE)

        mEndYear = mEndTime.get(Calendar.YEAR)
        mEndMonth = mEndTime.get(Calendar.MONTH) + 1
        mEndDay = mEndTime.get(Calendar.DAY_OF_MONTH)
        mEndHour = mEndTime.get(Calendar.HOUR_OF_DAY)
        mEndMinute = mEndTime.get(Calendar.MINUTE)

        val canSpanYear = mBeginYear != mEndYear
        val canSpanMon = !canSpanYear && mBeginMonth != mEndMonth
        val canSpanDay = !canSpanMon && mBeginDay != mEndDay
        val canSpanHour = !canSpanDay && mBeginHour != mEndHour
        val canSpanMinute = !canSpanHour && mBeginMinute != mEndMinute
        if (canSpanYear) {
            initDateUnits(
                MAX_MONTH_UNIT,
                mBeginTime.getActualMaximum(Calendar.DAY_OF_MONTH),
                MAX_HOUR_UNIT,
                MAX_MINUTE_UNIT
            )
        } else if (canSpanMon) {
            initDateUnits(
                mEndMonth,
                mBeginTime.getActualMaximum(Calendar.DAY_OF_MONTH),
                MAX_HOUR_UNIT,
                MAX_MINUTE_UNIT
            )
        } else if (canSpanDay) {
            initDateUnits(mEndMonth, mEndDay, MAX_HOUR_UNIT, MAX_MINUTE_UNIT)
        } else if (canSpanHour) {
            initDateUnits(mEndMonth, mEndDay, mEndHour, MAX_MINUTE_UNIT)
        } else if (canSpanMinute) {
            initDateUnits(mEndMonth, mEndDay, mEndHour, mEndMinute)
        }
    }

    private fun initDateUnits(endMonth: Int, endDay: Int, endHour: Int, endMinute: Int) {
        for (i in mBeginYear..mEndYear) {
            mYearUnits.add("${i.toString()}年")
//            mYearUnits.add(i.toString())
        }

        for (i in mBeginMonth..endMonth) {
            mMonthUnits.add("${mDecimalFormat.format(i.toLong())}月")
//            mMonthUnits.add(mDecimalFormat.format(i.toLong()))
        }

        for (i in mBeginDay..endDay) {

            mDayUnits.add("${mDecimalFormat.format(i.toLong())}日")
//            mDayUnits.add(mDecimalFormat.format(i.toLong()))
        }

        if (mScrollUnits and SCROLL_UNIT_HOUR != SCROLL_UNIT_HOUR) {
            mHourUnits.add(mDecimalFormat.format(mBeginHour.toLong()))
        } else {
            for (i in mBeginHour..endHour) {
                mHourUnits.add(mDecimalFormat.format(i.toLong()))
            }
        }

        if (mScrollUnits and SCROLL_UNIT_MINUTE != SCROLL_UNIT_MINUTE) {
            mMinuteUnits.add(mDecimalFormat.format(mBeginMinute.toLong()))
        } else {
            for (i in mBeginMinute..endMinute) {
                mMinuteUnits.add(mDecimalFormat.format(i.toLong()))
            }
        }

        mDpvYear!!.setDataList(mYearUnits)
        mDpvYear!!.setSelected(0)
        mDpvMonth!!.setDataList(mMonthUnits)
        mDpvMonth!!.setSelected(0)
        mDpvDay!!.setDataList(mDayUnits)
        mDpvDay!!.setSelected(0)
        mDpvHour!!.setDataList(mHourUnits)
        mDpvHour!!.setSelected(0)
        mDpvMinute!!.setDataList(mMinuteUnits)
        mDpvMinute!!.setSelected(0)

        setCanScroll()
    }

    private fun setCanScroll() {
        mDpvYear!!.setCanScroll(mYearUnits.size > 1)
        mDpvMonth!!.setCanScroll(mMonthUnits.size > 1)
        mDpvDay!!.setCanScroll(mDayUnits.size > 1)
        mDpvHour!!.setCanScroll(mHourUnits.size > 1 && mScrollUnits and SCROLL_UNIT_HOUR == SCROLL_UNIT_HOUR)
        mDpvMinute!!.setCanScroll(mMinuteUnits.size > 1 && mScrollUnits and SCROLL_UNIT_MINUTE == SCROLL_UNIT_MINUTE)
    }

    /**
     * 联动“月”变化
     *
     * @param showAnim 是否展示滚动动画
     * @param delay    联动下一级延迟时间
     */
    private fun linkageMonthUnit(showAnim: Boolean, delay: Long) {
        val minMonth: Int
        val maxMonth: Int
        val selectedYear = mSelectedTime.get(Calendar.YEAR)
        if (mBeginYear == mEndYear) {
            minMonth = mBeginMonth
            maxMonth = mEndMonth
        } else if (selectedYear == mBeginYear) {
            minMonth = mBeginMonth
            maxMonth = MAX_MONTH_UNIT
        } else if (selectedYear == mEndYear) {
            minMonth = 1
            maxMonth = mEndMonth
        } else {
            minMonth = 1
            maxMonth = MAX_MONTH_UNIT
        }

        // 重新初始化时间单元容器
        mMonthUnits.clear()
        for (i in minMonth..maxMonth) {
            mMonthUnits.add("${mDecimalFormat.format(i.toLong())}月")
//            mMonthUnits.add(mDecimalFormat.format(i.toLong()))
        }
        mDpvMonth!!.setDataList(mMonthUnits)

        // 确保联动时不会溢出或改变关联选中值
        val selectedMonth =
            getValueInRange(mSelectedTime.get(Calendar.MONTH) + 1, minMonth, maxMonth)
        mSelectedTime.set(Calendar.MONTH, selectedMonth - 1)
        mDpvMonth!!.setSelected(selectedMonth - minMonth)
        if (showAnim) {
            mDpvMonth!!.startAnim()
        }

        // 联动“日”变化
        mDpvMonth!!.postDelayed({ linkageDayUnit(showAnim, delay) }, delay)
    }

    /**
     * 联动“日”变化
     *
     * @param showAnim 是否展示滚动动画
     * @param delay    联动下一级延迟时间
     */
    private fun linkageDayUnit(showAnim: Boolean, delay: Long) {
        val minDay: Int
        val maxDay: Int
        val selectedYear = mSelectedTime.get(Calendar.YEAR)
        val selectedMonth = mSelectedTime.get(Calendar.MONTH) + 1
        if (mBeginYear == mEndYear && mBeginMonth == mEndMonth) {
            minDay = mBeginDay
            maxDay = mEndDay
        } else if (selectedYear == mBeginYear && selectedMonth == mBeginMonth) {
            minDay = mBeginDay
            maxDay = mSelectedTime.getActualMaximum(Calendar.DAY_OF_MONTH)
        } else if (selectedYear == mEndYear && selectedMonth == mEndMonth) {
            minDay = 1
            maxDay = mEndDay
        } else {
            minDay = 1
            maxDay = mSelectedTime.getActualMaximum(Calendar.DAY_OF_MONTH)
        }

        mDayUnits.clear()
        for (i in minDay..maxDay) {

            mDayUnits.add("${mDecimalFormat.format(i.toLong())}日")
//            mDayUnits.add(mDecimalFormat.format(i.toLong()))
        }
        mDpvDay!!.setDataList(mDayUnits)

        val selectedDay = getValueInRange(mSelectedTime.get(Calendar.DAY_OF_MONTH), minDay, maxDay)
        mSelectedTime.set(Calendar.DAY_OF_MONTH, selectedDay)
        mDpvDay!!.setSelected(selectedDay - minDay)
        if (showAnim) {
            mDpvDay!!.startAnim()
        }

        mDpvDay!!.postDelayed({ linkageHourUnit(showAnim, delay) }, delay)
    }

    /**
     * 联动“时”变化
     *
     * @param showAnim 是否展示滚动动画
     * @param delay    联动下一级延迟时间
     */
    private fun linkageHourUnit(showAnim: Boolean, delay: Long) {
        if (mScrollUnits and SCROLL_UNIT_HOUR == SCROLL_UNIT_HOUR) {
            val minHour: Int
            val maxHour: Int
            val selectedYear = mSelectedTime.get(Calendar.YEAR)
            val selectedMonth = mSelectedTime.get(Calendar.MONTH) + 1
            val selectedDay = mSelectedTime.get(Calendar.DAY_OF_MONTH)
            if (mBeginYear == mEndYear && mBeginMonth == mEndMonth && mBeginDay == mEndDay) {
                minHour = mBeginHour
                maxHour = mEndHour
            } else if (selectedYear == mBeginYear && selectedMonth == mBeginMonth && selectedDay == mBeginDay) {
                minHour = mBeginHour
                maxHour = MAX_HOUR_UNIT
            } else if (selectedYear == mEndYear && selectedMonth == mEndMonth && selectedDay == mEndDay) {
                minHour = 0
                maxHour = mEndHour
            } else {
                minHour = 0
                maxHour = MAX_HOUR_UNIT
            }

            mHourUnits.clear()
            for (i in minHour..maxHour) {
                mHourUnits.add(mDecimalFormat.format(i.toLong()))
            }
            mDpvHour!!.setDataList(mHourUnits)

            val selectedHour =
                getValueInRange(mSelectedTime.get(Calendar.HOUR_OF_DAY), minHour, maxHour)
            mSelectedTime.set(Calendar.HOUR_OF_DAY, selectedHour)
            mDpvHour!!.setSelected(selectedHour - minHour)
            if (showAnim) {
                mDpvHour!!.startAnim()
            }
        }

        mDpvHour!!.postDelayed({ linkageMinuteUnit(showAnim) }, delay)
    }

    /**
     * 联动“分”变化
     *
     * @param showAnim 是否展示滚动动画
     */
    private fun linkageMinuteUnit(showAnim: Boolean) {
        if (mScrollUnits and SCROLL_UNIT_MINUTE == SCROLL_UNIT_MINUTE) {
            val minMinute: Int
            val maxMinute: Int
            val selectedYear = mSelectedTime.get(Calendar.YEAR)
            val selectedMonth = mSelectedTime.get(Calendar.MONTH) + 1
            val selectedDay = mSelectedTime.get(Calendar.DAY_OF_MONTH)
            val selectedHour = mSelectedTime.get(Calendar.HOUR_OF_DAY)
            if (mBeginYear == mEndYear && mBeginMonth == mEndMonth && mBeginDay == mEndDay && mBeginHour == mEndHour) {
                minMinute = mBeginMinute
                maxMinute = mEndMinute
            } else if (selectedYear == mBeginYear && selectedMonth == mBeginMonth && selectedDay == mBeginDay && selectedHour == mBeginHour) {
                minMinute = mBeginMinute
                maxMinute = MAX_MINUTE_UNIT
            } else if (selectedYear == mEndYear && selectedMonth == mEndMonth && selectedDay == mEndDay && selectedHour == mEndHour) {
                minMinute = 0
                maxMinute = mEndMinute
            } else {
                minMinute = 0
                maxMinute = MAX_MINUTE_UNIT
            }

            mMinuteUnits.clear()
            for (i in minMinute..maxMinute) {
                mMinuteUnits.add(mDecimalFormat.format(i.toLong()))
            }
            mDpvMinute!!.setDataList(mMinuteUnits)

            val selectedMinute =
                getValueInRange(mSelectedTime.get(Calendar.MINUTE), minMinute, maxMinute)
            mSelectedTime.set(Calendar.MINUTE, selectedMinute)
            mDpvMinute!!.setSelected(selectedMinute - minMinute)
            if (showAnim) {
                mDpvMinute!!.startAnim()
            }
        }

        setCanScroll()
    }

    private fun getValueInRange(value: Int, minValue: Int, maxValue: Int): Int {
        return if (value < minValue) {
            minValue
        } else if (value > maxValue) {
            maxValue
        } else {
            value
        }
    }

    /**
     * 展示时间选择器
     *
     * @param dateStr 日期字符串，格式为 yyyy-MM-dd 或 yyyy-MM-dd HH:mm
     */
    fun show(dateStr: String) {
        if (!canShow() || TextUtils.isEmpty(dateStr)) return
        startTime = 0
        endTime = 0
        rbDate01?.text = ""
        rbDate02?.text = ""

        // 弹窗时，考虑用户体验，不展示滚动动画
        if (setSelectedTime(dateStr, false)) {
            mPickerDialog!!.show()
        }
    }

    private fun canShow(): Boolean {
        return mCanDialogShow && mPickerDialog != null
    }

    /**
     * 设置日期选择器的选中时间
     *
     * @param dateStr  日期字符串
     * @param showAnim 是否展示动画
     * @return 是否设置成功
     */
    fun setSelectedTime(dateStr: String, showAnim: Boolean): Boolean {
        return (canShow() && !TextUtils.isEmpty(dateStr)
                && setSelectedTime(
            DateFormatUtils.str2Long(dateStr, mCanShowPreciseTime),
            showAnim
        ))
    }

    /**
     * 展示时间选择器
     *
     * @param timestamp 时间戳，毫秒级别
     */
    fun show(timestamp: Long) {
        if (!canShow()) return

        if (setSelectedTime(timestamp, false)) {
            mPickerDialog!!.show()
        }
    }

    /**
     * 设置日期选择器的选中时间
     *
     * @param timestamp 毫秒级时间戳
     * @param showAnim  是否展示动画
     * @return 是否设置成功
     */
    fun setSelectedTime(timestamp: Long, showAnim: Boolean): Boolean {
        var timestamp = timestamp
        if (!canShow()) return false

        if (timestamp < mBeginTime.timeInMillis) {
            timestamp = mBeginTime.timeInMillis
        } else if (timestamp > mEndTime.timeInMillis) {
            timestamp = mEndTime.timeInMillis
        }
        mSelectedTime.timeInMillis = timestamp

        mYearUnits.clear()
        for (i in mBeginYear..mEndYear) {
            mYearUnits.add("${i.toString()}年")
//            mYearUnits.add(i.toString())
        }
        mDpvYear!!.setDataList(mYearUnits)
        mDpvYear!!.setSelected(mSelectedTime.get(Calendar.YEAR) - mBeginYear)
        linkageMonthUnit(showAnim, if (showAnim) LINKAGE_DELAY_DEFAULT else 0)
        return true
    }

    /**
     * 设置是否允许点击屏幕或物理返回键关闭
     */
    fun setCancelable(cancelable: Boolean) {
        if (!canShow()) return

        mPickerDialog!!.setCancelable(cancelable)
    }

    /**
     * 设置日期控件是否显示时和分
     */
    fun setCanShowPreciseTime(canShowPreciseTime: Boolean) {
        if (!canShow()) return

        if (canShowPreciseTime) {
            initScrollUnit()
            mDpvHour!!.visibility = View.VISIBLE
            mTvHourUnit!!.visibility = View.VISIBLE
            mDpvMinute!!.visibility = View.VISIBLE
            mTvMinuteUnit!!.visibility = View.VISIBLE
        } else {
            initScrollUnit(SCROLL_UNIT_HOUR, SCROLL_UNIT_MINUTE)
            mDpvHour!!.visibility = View.GONE
            mTvHourUnit!!.visibility = View.GONE
            mDpvMinute!!.visibility = View.GONE
            mTvMinuteUnit!!.visibility = View.GONE
        }
        mCanShowPreciseTime = canShowPreciseTime
    }

    /**
     * 设置日期控件是否显示时和分  日 和月
     * 1.显示年
     * 2.显示年月
     * 3.显示年月日
     */
    fun setCanShowPrecise(type: Int) {
        if (!canShow()) return
        when (type) {
            //影藏月 日
            1 -> {
                mDpvMonth!!.visibility = View.GONE
                mDpvDay!!.visibility = View.GONE
            }
            //影藏日
            2 -> {
                mTvDayUnit!!.visibility = View.GONE
                mDpvMonth!!.visibility = View.VISIBLE
                mDpvDay!!.visibility = View.GONE
            }
            //显示 年 月 日
            else -> {
                mDpvMonth!!.visibility = View.VISIBLE
                mDpvDay!!.visibility = View.VISIBLE
            }
        }
    }

    private fun initScrollUnit(vararg units: Int) {
        if (units == null || units.isEmpty()) {
            mScrollUnits = SCROLL_UNIT_HOUR + SCROLL_UNIT_MINUTE
        } else {
            for (unit in units) {
                mScrollUnits = mScrollUnits xor unit
            }
        }
    }

    /**
     * 设置日期控件是否可以循环滚动
     */
    fun setScrollLoop(canLoop: Boolean) {
        if (!canShow()) return

        mDpvYear!!.setCanScrollLoop(canLoop)
        mDpvMonth!!.setCanScrollLoop(canLoop)
        mDpvDay!!.setCanScrollLoop(canLoop)
        mDpvHour!!.setCanScrollLoop(canLoop)
        mDpvMinute!!.setCanScrollLoop(canLoop)
    }

    /**
     * 设置日期控件是否展示滚动动画
     */
    fun setCanShowAnim(canShowAnim: Boolean) {
        if (!canShow()) return

        mDpvYear!!.setCanShowAnim(canShowAnim)
        mDpvMonth!!.setCanShowAnim(canShowAnim)
        mDpvDay!!.setCanShowAnim(canShowAnim)
        mDpvHour!!.setCanShowAnim(canShowAnim)
        mDpvMinute!!.setCanShowAnim(canShowAnim)
    }

    /**
     * 销毁弹窗
     */
    fun onDestroy() {
        if (mPickerDialog != null) {
            mPickerDialog!!.dismiss()
            mPickerDialog = null

            mDpvYear!!.onDestroy()
            mDpvMonth!!.onDestroy()
            mDpvDay!!.onDestroy()
            mDpvHour!!.onDestroy()
            mDpvMinute!!.onDestroy()
        }
    }

    companion object {

        /**
         * 时间单位：时、分
         */
        private val SCROLL_UNIT_HOUR = 1
        private val SCROLL_UNIT_MINUTE = 2

        /**
         * 时间单位的最大显示值
         */
        private val MAX_MINUTE_UNIT = 59
        private val MAX_HOUR_UNIT = 23
        private val MAX_MONTH_UNIT = 12

        /**
         * 级联滚动延迟时间
         */
        private val LINKAGE_DELAY_DEFAULT = 100L
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            //年
            R.id.tv_date_pick_01 -> {
                rg02?.visibility = View.GONE
                mDpvDay?.visibility = View.GONE
                mDpvMonth?.visibility = View.GONE
                timeType = 1
            }
            //月
            R.id.tv_date_pick_02 -> {
                rg02?.visibility = View.GONE
                mDpvDay?.visibility = View.GONE
                mDpvMonth?.visibility = View.VISIBLE
                timeType = 2
            }
            //日
            R.id.tv_date_pick_03 -> {
                rg02?.visibility = View.GONE
                mDpvDay?.visibility = View.VISIBLE
                mDpvMonth?.visibility = View.VISIBLE
                timeType = 3
            }
            //自定义
            R.id.tv_date_pick_04 -> {
                rg02?.visibility = View.VISIBLE
                mDpvDay?.visibility = View.VISIBLE
                mDpvMonth?.visibility = View.VISIBLE
                timeType = 0
            }
            //开始时间
            R.id.rb_date_se_01 -> {
                isStarEnd = true
                LogUtils.d("TReceivedFragment", "startTime:$startTime ");
                LogUtils.d("TReceivedFragment", "endTime:$endTime ");
            }
            //结束时间
            else -> {

                LogUtils.d("TReceivedFragment", "startTime:$startTime ");
                LogUtils.d("TReceivedFragment", "endTime:$endTime ");
                isStarEnd = false
            }
        }

    }
}
