package com.toker.sys.dialog.custom

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import com.liaoinstan.springview.container.DefaultFooter
import com.liaoinstan.springview.container.DefaultHeader
import com.liaoinstan.springview.widget.SpringView
import com.toker.sys.R
import com.toker.sys.dialog.custom.adapter.SelectUserAdapter
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.fragment.custom.bean.PublicPoCustBean
import com.toker.sys.view.home.fragment.custom.bean.SelectUserList
import com.toker.sys.view.home.fragment.custom.event.SelectUserEvent
import org.greenrobot.eventbus.EventBus

class SelectUserDialog(val mContext: Context, val mBeans: MutableList<SelectUserList.PageData>) :
    View.OnClickListener,
    SelectUserAdapter.OnItemLitener {
    override fun onItemClick(record: SelectUserList.PageData, position: Int) {
        this.record = record
        userAdapter?.setSelected(position)
        userAdapter?.notifyDataSetChanged()
    }

    var page = 1
    var record: SelectUserList.PageData? = null
    var dialog: Dialog? = null
    var et04: EditText? = null
    var rvItem: RecyclerView? = null
    var userAdapter: SelectUserAdapter? = null

    init {

        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_select_user, null)
        dialog!!.setContentView(view)

        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM)
        val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val height = wm.defaultDisplay.height

        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height / 2)
        showDialog(dialog!!)
        dialog!!.findViewById<View>(R.id.tv_item_select_user_01).setOnClickListener(this)
        et04 = dialog!!.findViewById<EditText>(R.id.tv_item_select_user_04)

        rvItem = dialog!!.findViewById<RecyclerView>(R.id.rv_item_select_user)
        val spView = dialog!!.findViewById<SpringView>(R.id.sv_select_dialog)
        initSpringView(spView)
        dialog!!.findViewById<View>(R.id.tv_item_select_user_03).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.tv_item_select_user_05).setOnClickListener(this)

        rvItem?.layoutManager = GridLayoutManager(mContext, 1)
        userAdapter = SelectUserAdapter(mContext, mBeans)
        rvItem?.adapter = userAdapter
        userAdapter?.setOnItemkLitener(this)
        LogUtils.d("PublicPoCustDialog", "SelectUserDialog: ");
    }

    private fun showDialog(dialog: Dialog) {
        if (!dialog.isShowing) {
            dialog.show()
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
        }
    }

    //刷新数据
    fun setData(records: MutableList<SelectUserList.PageData>) {
        LogUtils.d("PublicPoCustDialog", "setData: $records");
        mBeans.addAll(records)
        userAdapter?.refreshData(mBeans)
        showDialog(dialog!!)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            //确定
            R.id.tv_item_select_user_03 -> {
                if (record != null) {
                    EventBus.getDefault().post(record)
                    dialog!!.dismiss()
                } else {
                    Toast.makeText(mContext, "请选择拓客员", Toast.LENGTH_SHORT).show()
                }

            }
            //搜索
            R.id.tv_item_select_user_05 -> {
                page = 1
                mBeans.clear()
                EventBus.getDefault().post(SelectUserEvent("${et04?.text}", page))

            }
            else -> {
                dialog!!.dismiss()
            }
        }

    }

    //刷新列表
    protected fun initSpringView(spView: SpringView) {
        spView.isEnable = true
        spView.header = DefaultHeader(mContext)
        spView.footer = DefaultFooter(mContext)
        spView.setListener(object : SpringView.OnFreshListener {
            override fun onLoadmore() {
                //加载更多
                if (spView != null) {
                    spView.onFinishFreshAndLoad()
                }

                page++
                EventBus.getDefault().post(SelectUserEvent("${et04?.text}", page))

            }

            override fun onRefresh() {
                //刷新
                if (spView != null) {
                    spView.onFinishFreshAndLoad()
                }
                page = 1
                mBeans.clear()
                EventBus.getDefault().post(SelectUserEvent("${et04?.text}", page))
            }

        })
    }

}