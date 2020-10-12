package com.toker.sys.dialog.custom

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.activity.custom.event.NCExistedEvent
import org.greenrobot.eventbus.EventBus

class NCExistedDialog (val mContext: Context, val content: String) : View.OnClickListener {
    var dialog: Dialog? = null

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_nc_existed, null)

        dialog!!.setContentView(view)
        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.CENTER_VERTICAL)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        showDialog(dialog!!)

        val tvTitle = dialog!!.findViewById<TextView>(R.id.tv_layout_nc_exist_content)
        tvTitle.text = content
        dialog!!.findViewById<View>(R.id.img_layout_nc_exist_close).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.btn_layout_nc_exist_determine).setOnClickListener(this)
    }

    private fun showDialog(dialog: Dialog) {
        dialog.show()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_layout_nc_exist_determine -> {
//                EventBus.getDefault().post(NCExistedEvent(true))
            }
            else -> {

            }
        }

        dialog!!.dismiss()
    }
}