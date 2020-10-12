package com.toker.sys.dialog.sheet

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.toker.sys.R
import com.toker.sys.view.home.activity.sheet.event.STProjectRanEvent
import org.greenrobot.eventbus.EventBus

/**
 * 任务进行状态
 */
class TurnoverDialog(private val mContext: Context) : View.OnClickListener {

    var dialog: Dialog? = null

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_turnove, null)
        dialog!!.setContentView(view)
        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        showDialog(dialog!!)
        dialog!!.findViewById<View>(R.id.tv_turnove_01).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.tv_turnove_02).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.tv_turnove_03).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.tv_turnove_04).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.tv_cancel).setOnClickListener(this)
    }

    private fun showDialog(dialog: Dialog) {
        dialog.show()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_turnove_01 -> EventBus.getDefault().post(STProjectRanEvent(1, "留电数"))
            R.id.tv_turnove_02 -> EventBus.getDefault().post(STProjectRanEvent(2, "到访数"))
            R.id.tv_turnove_03 -> EventBus.getDefault().post(STProjectRanEvent(3, "成交套数"))
            R.id.tv_turnove_04 -> EventBus.getDefault().post(STProjectRanEvent(4, "成交额(万元)"))

            else -> {
            }
        }
        dialog!!.dismiss()
    }
}
