package com.toker.sys.dialog.task

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import biz.kux.emergency.utils.thread.ThreadPoolUtil
import com.toker.sys.R
import com.toker.sys.view.home.activity.task.event.ApprovalTaskEvent
import com.toker.sys.view.home.activity.task.event.SplashEvent
import org.greenrobot.eventbus.EventBus


/**
 *
 * 检测更新提醒
 */
class SplashDialog(val mContext: Context) : View.OnClickListener {
    var dialog: Dialog? = null

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_splash, null)

        dialog!!.setContentView(view)
        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.CENTER_VERTICAL)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        showDialog(dialog!!)

        val tvTitle = dialog!!.findViewById<TextView>(R.id.tv_layout_nc_exist_content)
        tvTitle.text = "检测到有新版本,是否前往更新"
        dialog!!.findViewById<View>(R.id.img_layout_nc_exist_close).visibility = View.GONE
        dialog!!.findViewById<View>(R.id.btn_layout_nc_exist_determine).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.btn_call_phone_cancel).setOnClickListener(this)
    }

    private fun showDialog(dialog: Dialog) {
        dialog.show()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_layout_nc_exist_determine -> {
                EventBus.getDefault().post(SplashEvent(2))
            }
            else -> {
                EventBus.getDefault().post(SplashEvent(1))
            }
        }
        dialog?.dismiss()

    }

}