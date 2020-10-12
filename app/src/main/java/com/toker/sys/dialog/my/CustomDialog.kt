package com.toker.sys.dialog.my

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.toker.sys.R
import com.toker.sys.view.home.activity.my.perinfor.event.PerInforEvent
import org.greenrobot.eventbus.EventBus

class CustomDialog(private val mContext: Context) : View.OnClickListener {


    var dialog: Dialog? = null

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_custom, null)
        dialog!!.setContentView(view)

        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        showDialog(dialog!!)
        dialog!!.findViewById<View>(R.id.tv_take_photo).setOnClickListener(this)

        dialog!!.findViewById<View>(R.id.tv_take_pic).setOnClickListener(this)

        dialog!!.findViewById<View>(R.id.tv_cancel).setOnClickListener(this)
    }

    private fun showDialog(dialog: Dialog) {
        dialog.show()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_take_photo -> {
                EventBus.getDefault().post(PerInforEvent(0))
            }
            R.id.tv_take_pic -> {
                EventBus.getDefault().post(PerInforEvent(1))
            }
            else -> {
            }
        }
        dialog!!.dismiss()
    }

}