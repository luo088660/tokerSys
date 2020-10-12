package com.toker.sys.dialog.custom

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.toker.sys.R
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.fragment.custom.bean.PublicPoCust
import com.toker.sys.view.home.fragment.custom.bean.PublicPoCustBean
import com.toker.sys.view.home.fragment.custom.bean.SelectUserList
import com.toker.sys.view.home.fragment.custom.event.SelectUserEvent
import org.greenrobot.eventbus.EventBus

class PublicPoCustDialog(val mContext: Context, var bean: PublicPoCust) : View.OnClickListener {


    var dialog: Dialog? = null
    var tv02: TextView? = null
    var tv03: TextView? = null
    var tv04: TextView? = null
    var record: SelectUserList.PageData? = null

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_public_po_cust, null)

        dialog!!.setContentView(view)
        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.CENTER_VERTICAL)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        showDialog(dialog!!)

        tv02 = dialog!!.findViewById<TextView>(R.id.tv_item_po_02)
        tv03 = dialog!!.findViewById<TextView>(R.id.tv_item_po_03)
        tv04 = dialog!!.findViewById<TextView>(R.id.tv_item_po_04)

        tv02!!.text = bean.name
        tv03!!.text = bean.phone
        tv04!!.setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.img_layout_nc_exist_close).setOnClickListener(this)

        dialog!!.findViewById<View>(R.id.btn_call_phone_cancel).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.btn_call_phone_determine).setOnClickListener(this)
    }

    private fun showDialog(dialog: Dialog) {
        dialog.show()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
    }

    fun setData(event: SelectUserList.PageData) {
        this.record = event
        tv04?.text = event.userName
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            //确定
            R.id.btn_call_phone_determine -> {
                if (record != null) {
                    bean.tkId = record?.userId
                    EventBus.getDefault().post(bean)
                    dialog?.dismiss()
                } else {
                    Toast.makeText(mContext, "请选择拓客员", Toast.LENGTH_SHORT).show()
                }

                dialog!!.dismiss()
            }
            R.id.tv_item_po_04 -> {
                LogUtils.d("PublicPoCustDialog", "tv_item_po_04: ");
                EventBus.getDefault().post(SelectUserEvent(tv04?.text.toString()))
            }
            else -> {
                dialog!!.dismiss()
            }
        }


    }
}