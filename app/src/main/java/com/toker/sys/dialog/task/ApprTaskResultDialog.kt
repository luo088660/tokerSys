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
import org.greenrobot.eventbus.EventBus


/**
 *
 * 审批
 */
class ApprTaskResultDialog(val mContext: Context, val title: String) : View.OnClickListener {
    var dialog: Dialog? = null

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_appr_task_result, null)

        dialog!!.setContentView(view)
        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.CENTER_VERTICAL)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        showDialog(dialog!!)

        val tvTitle = dialog!!.findViewById<TextView>(R.id.tv_layout_3s)
        tvTitle.text = title
        dialog!!.findViewById<View>(R.id.img_app_task_close).setOnClickListener(this)
        val tv = dialog!!.findViewById<TextView>(R.id.btn_app_task_determine)
        tv.setOnClickListener(this)
//        onCountDown(tv)
    }

    private fun showDialog(dialog: Dialog) {
        dialog.show()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_app_task_determine -> {
                EventBus.getDefault().post(ApprovalTaskEvent(2))
                dialog!!.dismiss()
            }
            else -> {
                dialog!!.dismiss()
            }
        }



    }
    private var mCountTime: Int = 0
    //修改btn状态
    private fun onCountDown(btn_login_vcode: TextView) {
        mCountTime = 4
        ThreadPoolUtil.handler.postDelayed(object : Runnable {
            override fun run() {
                mCountTime--
                if (mCountTime < 0) {
                    ThreadPoolUtil.handler.removeMessages(0)
                    EventBus.getDefault().post(ApprovalTaskEvent(2))
                    dialog!!.dismiss()
                    return
                }
                btn_login_vcode?.text = "${mCountTime}秒后返回页面"
                ThreadPoolUtil.handler.postDelayed(this, 1000)
            }
        }, 1000)
    }
}