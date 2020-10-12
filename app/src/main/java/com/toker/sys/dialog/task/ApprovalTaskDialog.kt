package com.toker.sys.dialog.task

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.toker.sys.R
import com.toker.sys.view.home.activity.task.event.ApprovalTaskEvent
import com.toker.sys.view.home.fragment.task.bean.PageData
import org.greenrobot.eventbus.EventBus

/**
 * 审批
 */
class ApprovalTaskDialog(
    val mContext: Context,
    val bean: PageData?
) : View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    var result = "1"
    var dialog: Dialog? = null
    var et: EditText? = null

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_approval_task, null)
        dialog!!.setContentView(view)
        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.CENTER_VERTICAL)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        showDialog(dialog!!)
        dialog!!.findViewById<View>(R.id.img_app_task_close).setOnClickListener(this)
        val tv01 = dialog!!.findViewById<TextView>(R.id.tv_sp_01)
        val tv02 = dialog!!.findViewById<TextView>(R.id.tv_sp_02)
        val tv03 = dialog!!.findViewById<TextView>(R.id.tv_sp_03)
        tv01.text = bean?.taskName
        tv02.text = bean?.projectName
        tv03.text = bean?.creator

        et = dialog!!.findViewById<EditText>(R.id.et_dialog)
        dialog!!.findViewById<View>(R.id.btn_app_task_cancel).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.btn_app_task_determine).setOnClickListener(this)
        val rbApp = dialog!!.findViewById<RadioGroup>(R.id.rg_app_task)
        rbApp.check(R.id.rb_app_task_01)
        rbApp.setOnCheckedChangeListener(this)

    }

    private fun showDialog(dialog: Dialog) {
        dialog.show()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_app_task_cancel -> {

            }
            R.id.btn_app_task_determine -> {
                val remark = et!!.text.toString()
                if (TextUtils.isEmpty(remark)){
                    Toast.makeText(mContext,"审批意见不能为空",Toast.LENGTH_SHORT).show()
                    return
                }
                EventBus.getDefault().post(ApprovalTaskEvent( 0,bean!!,result,remark))
            }
            else -> {
            }
        }
        dialog!!.dismiss()
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.rb_app_task_01 -> {
                result = "1"
            }
            else -> {
                result = "0"
            }
        }
    }
}