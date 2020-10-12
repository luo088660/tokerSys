package com.toker.sys.dialog.my

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

class MeAttenRemarksDialog(
    val mContext: Context,
    val type: Int,
    val add: String,
    val tContent: String,
    val time: String,
    val status: String
) : View.OnClickListener {
    var dialog: Dialog? = null
    var et: EditText? = null
    var tvName: TextView? = null
    var tv01: TextView? = null
    var tv02: TextView? = null
    var tv03: TextView? = null


    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_meatten_remarks, null)

        dialog!!.setContentView(view)
        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.CENTER_VERTICAL)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        showDialog(dialog!!)
        //确定将客户【王女士】标记无效吗？
        dialog!!.findViewById<View>(R.id.img_layout_nc_exist_close).setOnClickListener(this)
        tvName = dialog!!.findViewById<TextView>(R.id.tv_layout_nc_exist_content)
        tv01 = dialog!!.findViewById<TextView>(R.id.tv_dialog_remark_01)
        tv02 = dialog!!.findViewById<TextView>(R.id.tv_dialog_remark_02)
        tv03 = dialog!!.findViewById<TextView>(R.id.btn_me_atten_01)
        val tv04 = dialog!!.findViewById<TextView>(R.id.tv_meatten_remar_01)
        et = dialog!!.findViewById<EditText>(R.id.et_fill_report)
        tv01?.text = tContent
        tv02?.text = status
        tv03?.text = time
        tv04?.text = add

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
                    EventBus.getDefault().post(CustomerInvalid(type,context))
                    dialog!!.dismiss()
                }

            }
            else -> dialog!!.dismiss()
        }

    }
}