package com.toker.sys.dialog.my

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.activity.custom.event.CustomerInvalid
import org.greenrobot.eventbus.EventBus

/**
 * Tost
 */
class CallToastDialog(val mContext: Context,val content: String) : View.OnClickListener {

    var dialog: Dialog? = null
    var tvPhone:TextView?=null

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_call_phone, null)
        dialog!!.setContentView(view)

        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.CENTER)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        showDialog(dialog!!)
         tvPhone = dialog!!.findViewById<TextView>(R.id.tv_call_phone)
        tvPhone?.text = content
        dialog!!.findViewById<View>(R.id.img_call_phone_close).setOnClickListener(this)

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
            //拨打电话
            R.id.btn_call_phone_determine -> {
                    EventBus.getDefault().post(CustomerInvalid(100,""))

            }
            R.id.btn_call_phone_cancel -> {

            }
            else -> {
            }
        }
        dialog!!.dismiss()
    }
}