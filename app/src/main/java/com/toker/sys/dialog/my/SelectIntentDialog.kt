package com.toker.sys.dialog.my

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.toker.sys.R
import org.greenrobot.eventbus.EventBus

class SelectIntentDialog(private val mContext: Context) : View.OnClickListener {


    var dialog: Dialog? = null

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_select_intent, null)
        dialog!!.setContentView(view)

        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        showDialog(dialog!!)
        dialog!!.findViewById<View>(R.id.tv_take_photo_a).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.tv_take_photo_b).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.tv_take_photo_c).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.tv_take_photo_d).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.tv_cancel).setOnClickListener(this)
    }

    private fun showDialog(dialog: Dialog) {
        dialog.show()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_take_photo_a -> EventBus.getDefault().post(SelectIntentBean("A"))
            R.id.tv_take_photo_b -> EventBus.getDefault().post(SelectIntentBean("B"))
            R.id.tv_take_photo_c -> EventBus.getDefault().post(SelectIntentBean("C"))
            R.id.tv_take_photo_d -> EventBus.getDefault().post(SelectIntentBean("D"))
            else -> {
            }
        }
        dialog!!.dismiss()
    }
    class SelectIntentBean(val type:String)
}