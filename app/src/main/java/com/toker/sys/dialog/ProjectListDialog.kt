package com.toker.sys.dialog

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.toker.sys.R
import com.toker.sys.dialog.adapter.ProjectListAdapter
import com.toker.sys.view.home.bean.Data
import org.greenrobot.eventbus.EventBus

/**
 * 全部项目
 */
class ProjectListDialog(val mContext: Context, data: MutableList<Data>) : View.OnClickListener,
    ProjectListAdapter.ProjecListListener {
    private val Tag = "ProjectListDialog";
    var bean: Data? = null
    var dialog: Dialog? = null
    var stuta: Int = 0
    var isType: Boolean = false
    var tvTitle: TextView? = null
    var listAdapter: ProjectListAdapter? = null

    var height: Int = 0

    constructor(mContext: Context, data: MutableList<Data>, stuta: Int) : this(mContext, data) {
        this.stuta = stuta
        val mBeans = mutableListOf<Data>()
        mBeans.add(Data("", "", "全部项目", ""))
        mBeans.addAll(data)
        listAdapter?.refreshData(mBeans)
        getHeight(mBeans)
    }

    constructor(mContext: Context, data: MutableList<Data>, isType: Boolean) : this(
        mContext,
        data
    ) {
        this.isType = isType
        tvTitle!!.text = if (isType)"选择团队" else "选择项目"
        val mBeans = mutableListOf<Data>()
        mBeans.add(Data("", "", if (isType)"全部团队" else "全部项目", ""))
        mBeans.addAll(data)
        listAdapter?.refreshData(mBeans)
        getHeight(mBeans)
    }

    override fun itemListener(bean: Data) {
        this.bean = bean
        bean?.stuta = stuta
        EventBus.getDefault().post(bean)
        dialog!!.dismiss()

    }

    init {
        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_project_list, null)
        dialog!!.setContentView(view)
        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM)
        val mBeans = mutableListOf<Data>()
        mBeans.add(Data("", "", "全部项目", ""))
        mBeans.addAll(data)

        var wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        height = wm.defaultDisplay.height

        //设置对话框大小
        getHeight(mBeans)

        dialog!!.findViewById<View>(R.id.tv_cancel).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.tv_confirm).setOnClickListener(this)
        tvTitle = dialog!!.findViewById<TextView>(R.id.tv_title)
        val rvList = dialog!!.findViewById<RecyclerView>(R.id.rv_project_list)

        listAdapter = ProjectListAdapter(mContext, mBeans)
        rvList.layoutManager = GridLayoutManager(mContext, 1)
        rvList.adapter = listAdapter
        listAdapter?.setListener(this)
        showDialog(dialog!!)
    }

    private fun getHeight(
        mBeans: MutableList<Data>
    ) {
        dialog!!.window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            if (mBeans.size > 7) height / 2 else ViewGroup.LayoutParams.WRAP_CONTENT
        )
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
                    bean?.stuta = stuta
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
