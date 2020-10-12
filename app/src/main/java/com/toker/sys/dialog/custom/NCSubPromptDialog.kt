package com.toker.sys.dialog.custom

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.activity.custom.event.NCSubPrompEvent
import org.greenrobot.eventbus.EventBus

/**
 * 提示 确认提交
 */
class NCSubPromptDialog (val mContext: Context,var content:String) : View.OnClickListener {

    var tvContent:TextView? = null
    var dialog: Dialog? = null
    var btnDate:TextView? = null
    var isTStatus = 0
    constructor(mContext: Context,content: String,isTStatus:Int):this(mContext,content){
        this.content = content
        this.isTStatus = isTStatus
        tvContent?.text = content
        btnDate?.text = "确定"
    }

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_nc_sub_prompt, null)
        dialog!!.setContentView(view)

        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.CENTER)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        showDialog(dialog!!)

        tvContent = dialog!!.findViewById<TextView>(R.id.tv_dialog_ns_context)
        tvContent?.text = content
        dialog!!.findViewById<View>(R.id.img_call_phone_close).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.btn_new_nc_cancel).setOnClickListener(this)

        btnDate = dialog!!.findViewById<TextView>(R.id.btn_new_nc_determine)
        btnDate?.setOnClickListener(this)
    }

    private fun showDialog(dialog: Dialog) {
        dialog.show()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_new_nc_determine -> {
                EventBus.getDefault().post(NCSubPrompEvent(true,isTStatus))
            }
            else -> {
            }
        }
        dialog!!.dismiss()
    }
}