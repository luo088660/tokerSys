package com.toker.sys.dialog.my

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import com.toker.sys.R
import com.toker.sys.dialog.adapter.NationProvinAdapter
import com.toker.sys.view.home.activity.custom.bean.CityBean
import org.greenrobot.eventbus.EventBus

class NationProvinDialog (
    private val mContext: Context ,val mBeans:MutableList<CityBean.Data>
) : View.OnClickListener, NationProvinAdapter.NationProListener {
    private val Tag = "ProjectListDialog";
    var dialog: Dialog? = null

    override fun itemListener(bean: CityBean.Data) {
        EventBus.getDefault().post(bean)
        dialog!!.dismiss()
    }

    init {

        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_nation_provin, null)
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

        val listAdapter = NationProvinAdapter(mContext, mBeans)
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
//                if (bean != null) {
//                    EventBus.getDefault().post(NPBean(bean!!))
//                } else {
//                    Toast.makeText(mContext, "请选择项目", Toast.LENGTH_SHORT).show()
//                    return
//                }
            }
            else -> {
            }
        }
        dialog!!.dismiss()
    }

    class NPBean(val name:String)
}
