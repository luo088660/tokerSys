package com.toker.sys.dialog.my

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R

class CustMapDialog(val mContext: Context, title: String) : View.OnClickListener {
    var dialog: Dialog? = null

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_cust_map, null)

        dialog!!.setContentView(view)
        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.CENTER_VERTICAL)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        showDialog(dialog!!)

        val tvTitle = dialog!!.findViewById<TextView>(R.id.tv_dialog_cust_map_01)
        tvTitle.text = title
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
            //查看客户分布明细
            R.id.btn_layout_nc_exist_determine -> {
            }
            else -> {

            }
        }

        dialog!!.dismiss()
    }
}