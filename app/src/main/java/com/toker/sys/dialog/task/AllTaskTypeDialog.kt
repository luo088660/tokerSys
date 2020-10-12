package com.toker.sys.dialog.task

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.toker.sys.R
import com.toker.sys.view.home.activity.sheet.event.STProjectRanEvent
import com.toker.sys.view.home.activity.task.event.AllTaskTypeEvent
import org.greenrobot.eventbus.EventBus

/**
 * 任务类型
 */
class AllTaskTypeDialog(private val mContext: Context) : View.OnClickListener {

    var dialog: Dialog? = null

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_all_task_type, null)
        dialog!!.setContentView(view)
        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        showDialog(dialog!!)
        dialog!!.findViewById<View>(R.id.tv_task_all_type_01).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.tv_task_all_type_02).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.tv_task_all_type_03).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.tv_task_all_type_04).setOnClickListener(this)

        dialog!!.findViewById<View>(R.id.tv_cancel).setOnClickListener(this)
    }

    private fun showDialog(dialog: Dialog) {
        dialog.show()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_task_all_type_01 -> EventBus.getDefault().post(AllTaskTypeEvent(3, "全部任务类型"))
            R.id.tv_task_all_type_02 -> EventBus.getDefault().post(AllTaskTypeEvent(2, "日"))
            R.id.tv_task_all_type_03 -> EventBus.getDefault().post(AllTaskTypeEvent(1, "周"))
            R.id.tv_task_all_type_04 -> EventBus.getDefault().post(AllTaskTypeEvent(0, "月"))

            else -> {
            }
        }
        dialog!!.dismiss()
    }
}
