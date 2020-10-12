package com.toker.sys.dialog.custom.adapter

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.activity.custom.event.CustomerInvalid
import org.greenrobot.eventbus.EventBus

/**
 * 请填写客户跟进记录
 */
class CustFollowRecordDialog(val mContext: Context, val name:String, val phone:String) : View.OnClickListener {
    var dialog: Dialog? = null
    var et: EditText? = null
    var tvName: TextView? = null


    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_cust_follow_record, null)

        dialog!!.setContentView(view)
        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.CENTER_VERTICAL)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        showDialog(dialog!!)
//        确定将客户【王女士】标记无效吗？
        dialog!!.findViewById<View>(R.id.img_layout_nc_exist_close).setOnClickListener(this)
        tvName = dialog!!.findViewById<TextView>(R.id.tv_layout_nc_exist_content)
        et = dialog!!.findViewById<EditText>(R.id.et_fill_report)
        tvName!!.text = "$name\t$phone"

        dialog!!.findViewById<View>(R.id.btn_call_phone_cancel).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.btn_call_phone_determine).setOnClickListener(this)
    }

    private fun showDialog(dialog: Dialog) {
        dialog.show()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_call_phone_determine -> {
                val context = et?.text.toString()
                if (context.isNotEmpty()) {
                    EventBus.getDefault().post(CustomerInvalid(2,context))
                    dialog!!.dismiss()
                }

            }
            else -> dialog!!.dismiss()
        }

    }
}