package com.toker.sys.dialog.custom

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.toker.sys.R
import com.toker.sys.view.home.activity.custom.event.FUAllStatesEvent
import com.toker.sys.view.home.activity.sheet.event.STProjectRanEvent
import com.toker.sys.view.home.activity.task.event.AllTaskTypeEvent
import org.greenrobot.eventbus.EventBus

/**
 * 任务类型
 */
class FUAllStatesDialog(private val mContext: Context) : View.OnClickListener {

    var dialog: Dialog? = null

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_fu_all_state, null)
        dialog!!.setContentView(view)
        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        showDialog(dialog!!)
        dialog!!.findViewById<View>(R.id.tv_fu_all_state_01).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.tv_fu_all_state_02).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.tv_fu_all_state_03).setOnClickListener(this)

        dialog!!.findViewById<View>(R.id.tv_cancel).setOnClickListener(this)
    }

    private fun showDialog(dialog: Dialog) {
        dialog.show()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_fu_all_state_01 -> EventBus.getDefault().post(FUAllStatesEvent(0, "全部状态"))
            R.id.tv_fu_all_state_02 -> EventBus.getDefault().post(FUAllStatesEvent(2, "已推荐"))
            R.id.tv_fu_all_state_03 -> EventBus.getDefault().post(FUAllStatesEvent(1, "未推荐"))
            else -> {
            }
        }
        dialog!!.dismiss()
    }
}
