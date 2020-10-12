package com.toker.sys.dialog.custom

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.toker.sys.R
import com.toker.sys.dialog.custom.adapter.FUAllRecUserAdapter
import com.toker.sys.view.home.activity.custom.event.FUAllRecUserEvent
import org.greenrobot.eventbus.EventBus

/**
 * 推荐用户
 */
class FUAllRecUserDialog (private val mContext: Context) : View.OnClickListener, FUAllRecUserAdapter.FuAllUserClick {


    var dialog: Dialog? = null

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_fu_all_rec_user, null)
        dialog!!.setContentView(view)
        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM)
        val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = wm.defaultDisplay.width
        val height = wm.defaultDisplay.height


        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height/2)
        showDialog(dialog!!)
        val rvView = dialog!!.findViewById<RecyclerView>(R.id.rv_fu_user)
        val mBeans:Array<String> = arrayOf("全部","到访","认筹","认购","签约","过期")
        rvView.layoutManager = GridLayoutManager(mContext,1)
        val userAdapter = FUAllRecUserAdapter(mContext, mBeans)
        rvView.adapter = userAdapter
        userAdapter.setOnClick(this)

        dialog!!.findViewById<View>(R.id.tv_cancel).setOnClickListener(this)
    }

    private fun showDialog(dialog: Dialog) {
        dialog.show()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
    }

    override fun onClick(v: View?) {
        dialog!!.dismiss()
    }
    //Item 点击事件
    override fun userOnClick(itemName: String) {
        EventBus.getDefault().post(FUAllRecUserEvent(itemName))
        dialog!!.dismiss()
    }
}
