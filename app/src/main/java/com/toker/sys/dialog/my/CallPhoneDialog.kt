package com.toker.sys.dialog.my

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.custom.event.CustomerInvalid
import org.greenrobot.eventbus.EventBus

/**
 * 拨打电话
 */
class CallPhoneDialog(val mContext: Context) : View.OnClickListener {

    var dialog: Dialog? = null
    var phone:String = ""
    var isType:Int = 0
    var tvPhone:TextView?=null
    constructor(mContext: Context,phone: String):this(mContext){
        this.phone = phone
        tvPhone?.text = "确认拨打电话$phone?"
    }
    constructor(mContext: Context,isType: Int,text:String):this(mContext){
        this.isType = isType
        tvPhone?.text =if (isType == 1){
            "当前时间未到考勤签退时间。确认签退后，考勤将视为早退，且不允许再次签退，确认签退吗？"
        }else{
            Html.fromHtml(String.format(mContext.resources.getString(R.string.tip_task_number_10),text))
        }
    }

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_call_phone, null)
        dialog!!.setContentView(view)

        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.CENTER)
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        showDialog(dialog!!)
         tvPhone = dialog!!.findViewById<TextView>(R.id.tv_call_phone)
        LogUtils.d("CallPhoneDialog", "isType:$isType ");
//        tvPhone?.text = if (isType)"当前时间未到考勤签退时间。确认签退后，考勤将视为早退，且不允许再次签退，确认签退吗？" else "确认拨打电话$phone?"
        dialog!!.findViewById<View>(R.id.img_call_phone_close).setOnClickListener(this)

        dialog!!.findViewById<View>(R.id.btn_call_phone_cancel).setOnClickListener(this)

        dialog!!.findViewById<View>(R.id.btn_call_phone_determine).setOnClickListener(this)
    }

    private fun showDialog(dialog: Dialog) {
        dialog.show()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            //拨打电话
            R.id.btn_call_phone_determine -> {
                if (isType != 0){
                    EventBus.getDefault().post(CustomerInvalid(100,"",isType))
                }else{
                    callPhone(phone)
                }

            }
            R.id.btn_call_phone_cancel -> {

            }
            else -> {
            }
        }
        dialog!!.dismiss()
    }
    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    private fun callPhone(phoneNum: String) {

        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse("tel:$phoneNum")
        intent.data = data
        mContext.startActivity(intent)
    }
}