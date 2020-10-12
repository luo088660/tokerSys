package com.toker.sys.view.home.fragment.custom.item.publicpool

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.R
import com.toker.sys.utils.network.params.CustomerManageImp
import com.toker.sys.view.home.fragment.custom.adapter.PublicPoolAdapter
import com.toker.sys.view.home.fragment.custom.bean.PublicPoolBean
import com.toker.sys.view.home.fragment.event.CustomProjectEvent
import kotlinx.android.synthetic.main.fragment_public_pool.*
import kotlinx.android.synthetic.main.layout_public_pool_02.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 拓客中层
 * 公客池
 * @author yyx
 */

class PublicPoolFragment : BaseFragment<PublicPoolContract.View, PublicPoolPresenter>(),
    PublicPoolContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): PublicPoolFragment {
            val args = Bundle()
            val fragment = PublicPoolFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var tv01: TextView? = null
    private var btnKhzl: TextView? = null
    private var mBeans = mutableListOf<PublicPoolBean.PageData>()
    private var adapter: PublicPoolAdapter? = null
    override var mPresenter: PublicPoolPresenter = PublicPoolPresenter()
    var drawableS: Drawable? = null
    var drawableX: Drawable? = null
    var projectId = ""
    var order = "asc"
    var isOrder = false
    var valiFlag = ""
    var page = 1
    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        log("createView", "PublicPoolFragment")
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_public_pool, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        valiFlag = arguments?.getString("type")!!
        rv_t_public_pool.layoutManager = GridLayoutManager(context, 1)
        mPresenter.loadRepositories()
    }

    override fun initData() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_public_pool_01, null)
        tv01 = view.findViewById<TextView>(R.id.tv_follow_up_01)
        btnKhzl = view.findViewById<TextView>(R.id.tv_khzl)
        // 使用代码设置drawableleft
        drawableS = resources.getDrawable(R.mipmap.icon_xspx)
        drawableX = resources.getDrawable(R.mipmap.icon_xxpx)
        // / 这一步必须要做,否则不会显示.
        drawableS!!.setBounds(0, 0, drawableS!!.minimumWidth, drawableS!!.minimumHeight)
        drawableX!!.setBounds(0, 0, drawableX!!.minimumWidth, drawableX!!.minimumHeight)

        adapter = PublicPoolAdapter(context!!, mBeans)
        rv_t_public_pool.adapter = adapter
        adapter!!.setHeaderView(view)
        setOnClickListener(btnKhzl!!)
        setOnClickListener(tv01!!)
        setOnClickListener(btn_public_pool)
        initSpringView(sp_t_public_pool)
    }


    override fun onFRefresh() {
        super.onFRefresh()
        this.page = 1
        mBeans.clear()
        mPresenter.loadRepositories()
    }

    override fun onFLoadmore() {
        super.onFLoadmore()
        this.page++
        mPresenter.loadRepositories()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
//            btn_public_pool -> sl_public_pool.fullScroll(View.FOCUS_UP)
            //全部项目
            tv01 -> {
                EventBus.getDefault().post(CustomProjectEvent(1))
            }
            btnKhzl -> {
                btnKhzl?.setCompoundDrawables(
                    null,
                    null,
                    if (isOrder) drawableS else drawableX,
                    null
                )
                order = if (isOrder) "asc" else "desc"
                isOrder = !isOrder
                onFRefresh()
            }
            else -> {
            }
        }
    }

    //全部项目
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ProjectList(event: com.toker.sys.view.home.bean.Data) {
        tv01!!.text = event.projectName
        projectId = event.projectId
        onFRefresh()
    }

    //数据展示
    override fun onShowData(data: PublicPoolBean.Data) {
        tv_pool_03.text = data.total
        mBeans.addAll(data.dto.pageData)
        adapter?.refreshData(mBeans)
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        if (!TextUtils.isEmpty(projectId)) {
            map["projectId"] = projectId    //项目id	否	String
        }
        map["validFlag"] = valiFlag    //1,公客池 2，无效客户
//        if (!TextUtils.isEmpty(order)) {
        map["sortWay"] = order    //1,降序 2，升序
//        }
        map["pageSize"] = "10"	//当前页大小	是	String
        map["page"] = "$page"    //当前页面	是	String
        Log.d(TAG, "map:$map ");
        return map
    }

    override fun getUrl(url_type: Int): String {
        return CustomerManageImp.API_CUST_CUSTOMER_INVALID_LIST
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        log("onDestroy", "PublicPoolFragment")
    }

}// Required empty public constructor