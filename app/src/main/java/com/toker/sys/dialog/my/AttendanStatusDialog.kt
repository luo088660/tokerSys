package com.toker.sys.dialog.my

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.toker.sys.R
import com.toker.sys.utils.network.bean.AstatuBean
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.task.event.ApprovalTaskEvent
import com.toker.sys.view.home.fragment.task.bean.PageData
import org.greenrobot.eventbus.EventBus

/**
 * 审批
 */
class AttendanStatusDialog(
    val mContext: Context,
    val stutas: Int,
    val attType: String
) : View.OnClickListener {
    val TAG  = "AttendanStatusDialog"
    var result = "1"
    var dialog: Dialog? = null
    var et: EditText? = null
    var cb01: CheckBox? = null
    var cb02: CheckBox? = null
    var cb03: CheckBox? = null

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_attendan_status, null)
        dialog!!.setContentView(view)
        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.CENTER_VERTICAL)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        showDialog(dialog!!)
        dialog!!.findViewById<View>(R.id.img_app_task_close).setOnClickListener(this)
        et = dialog!!.findViewById<EditText>(R.id.et_dialog)
        val tv = dialog!!.findViewById<TextView>(R.id.tv_dialog)
        tv.text = String.format(
            mContext.resources.getString(R.string.tip_task_number_09),
            if (stutas == 1) "正常" else "异常"
        )
        dialog!!.findViewById<View>(R.id.btn_app_task_cancel).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.btn_app_task_determine).setOnClickListener(this)
        cb01 = dialog!!.findViewById<CheckBox>(R.id.cb_01)
        cb02 = dialog!!.findViewById<CheckBox>(R.id.cb_02)
        cb03 = dialog!!.findViewById<CheckBox>(R.id.cb_03)
        cb01!!.setOnClickListener(this)
        cb02!!.setOnClickListener(this)
        cb03!!.setOnClickListener(this)
    }

    private fun showDialog(dialog: Dialog) {
        dialog.show()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
    }

    var isRequest = 0
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_app_task_cancel -> {
                dialog!!.dismiss()
            }
            R.id.btn_app_task_determine -> {
                val remark = et!!.text.toString()
                LogUtils.d(TAG, "attType:$attType ");
                LogUtils.d(TAG, "isRequest:$isRequest ");
                LogUtils.d(TAG, "stutas:$stutas ");
                EventBus.getDefault().post(AstatuBean.Bean(attType, "$stutas","$isRequest",remark))
                dialog!!.dismiss()
            }
            R.id.cb_01 -> {
                if (cb01!!.isChecked) {
                    cb02!!.isChecked = false
                    cb03!!.isChecked = false
                    isRequest = 1
                }else isRequest = 0
            }
            R.id.cb_02 -> {
                if (cb02!!.isChecked) {
                    cb01!!.isChecked = false
                    cb03!!.isChecked = false
                    isRequest = 2
                }else isRequest = 0
            }
            R.id.cb_03 -> {
                if (cb03!!.isChecked) {
                    cb01!!.isChecked = false
                    cb02!!.isChecked = false
                    isRequest = 0
                }else isRequest = 0
            }
            else -> {
                dialog!!.dismiss()
            }
        }

    }


}