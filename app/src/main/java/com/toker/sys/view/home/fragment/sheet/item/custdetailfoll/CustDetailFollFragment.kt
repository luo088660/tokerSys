package com.toker.sys.view.home.fragment.sheet.item.custdetailfoll

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.R
import com.toker.sys.view.home.fragment.sheet.event.SheetEvent
import com.toker.sys.view.home.fragment.task.adapter.TaskAdapter
import kotlinx.android.synthetic.main.fragment_cust_detail_foll.*
import kotlinx.android.synthetic.main.fragment_task.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus

/**
 * 待跟进客户详情
 * @author yyx
 */

class CustDetailFollFragment : BaseFragment<CustDetailFollContract.View, CustDetailFollPresenter>(), CustDetailFollContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): CustDetailFollFragment {
            val args = Bundle()
            val fragment = CustDetailFollFragment()
            fragment.arguments = args
            return fragment
        }
    }
    private var taskAdapter:TaskAdapter? = null
    private var mBeans = arrayOf("")
    override var mPresenter: CustDetailFollPresenter = CustDetailFollPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (main_layout == null){
            main_layout = inflateView(R.layout.fragment_cust_detail_foll, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        tv_title.text = "待跟进客户详情"
    }

    override fun initData() {
        mBeans = activity!!.resources.getStringArray(R.array.cust_foll_detail)
        rv_cust_detail.layoutManager = GridLayoutManager(activity!!, 2)
        taskAdapter = TaskAdapter(activity!!, mBeans)


        rv_cust_detail.adapter = taskAdapter
        setOnClickListener(img_back)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            img_back -> EventBus.getDefault().post(SheetEvent(7))
            else -> {
            }
        }
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }


}