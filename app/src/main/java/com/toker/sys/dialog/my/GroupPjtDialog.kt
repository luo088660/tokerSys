package com.toker.sys.dialog.my

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import com.toker.sys.R
import com.toker.sys.dialog.adapter.GroupPjtAdapter
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.my.bean.GroupProjectBean
import org.greenrobot.eventbus.EventBus

/**
 * 全部项目
 */
class GroupPjtDialog(
    private val mContext: Context,
    data: MutableList<GroupProjectBean.Data>
) : View.OnClickListener, GroupPjtAdapter.ProjecListListener {
    private val Tag = "ProjectListDialog";
    var bean: GroupProjectBean.Data? = null
    var dialog: Dialog? = null

    override fun itemListener(bean: GroupProjectBean.Data) {
//        Toast.makeText(mContext, "${bean.groupName}", Toast.LENGTH_SHORT).show()
        this.bean = bean
        EventBus.getDefault().post(bean)
        dialog!!.dismiss()
    }

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_group_pjt, null)
        dialog!!.setContentView(view)
        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM)

        val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val height = wm.defaultDisplay.height
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height / 2)

        dialog!!.findViewById<View>(R.id.tv_cancel).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.tv_confirm).setOnClickListener(this)
        val rvList = dialog!!.findViewById<RecyclerView>(R.id.rv_project_list)


        LogUtils.d(Tag, "data.toString():${data.toString()} ")
        val listAdapter = GroupPjtAdapter(mContext, data)
        rvList.layoutManager = GridLayoutManager(mContext, 1)
        rvList.adapter = listAdapter
        listAdapter.setListener(this)
        showDialog(dialog!!)
    }

    private fun showDialog(dialog: Dialog) {
        dialog.show()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_confirm -> {
                if (bean != null) {
                    EventBus.getDefault().post(bean)
                } else {
                    Toast.makeText(mContext, "请选择项目", Toast.LENGTH_SHORT).show()
                    return
                }
            }
            else -> {
            }
        }
        dialog!!.dismiss()
    }
}
