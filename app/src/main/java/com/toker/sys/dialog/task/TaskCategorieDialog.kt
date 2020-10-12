package com.toker.sys.dialog.task

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.toker.sys.R
import com.toker.sys.view.home.activity.task.event.AllTaskTypeEvent
import com.toker.sys.view.home.activity.task.event.TaskCategorieEvent
import org.greenrobot.eventbus.EventBus

class TaskCategorieDialog (private val mContext: Context) : View.OnClickListener {

    var dialog: Dialog? = null

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_task_categorie, null)
        dialog!!.setContentView(view)
        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        showDialog(dialog!!)
        dialog!!.findViewById<View>(R.id.tv_task_task_cate_01).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.tv_task_task_cate_02).setOnClickListener(this)

        dialog!!.findViewById<View>(R.id.tv_cancel).setOnClickListener(this)
    }

    private fun showDialog(dialog: Dialog) {
        dialog.show()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_task_task_cate_01 -> EventBus.getDefault().post(TaskCategorieEvent(1, "指标型任务"))
            R.id.tv_task_task_cate_02 -> EventBus.getDefault().post(TaskCategorieEvent(2, "事务型任务"))

            else -> {
            }
        }
        dialog!!.dismiss()
    }
}
