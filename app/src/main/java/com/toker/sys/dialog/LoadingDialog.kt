package com.toker.sys.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import com.toker.sys.R
import com.toker.sys.http.subsciber.IProgressDialog

class LoadingDialog(context: Context) : Dialog(context), IProgressDialog {
    override fun getDialog(): Dialog {
        val  dialog = Dialog(context, R.style.LoadingDialog)
        dialog.setContentView(R.layout.dialog_loading)

        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.CENTER)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        // 设置窗口大小
        val attributes = window.attributes;
        // 设置窗口背景透明度
        attributes.alpha = 0.7f

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }
}