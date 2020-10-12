package com.toker.sys.view.home.activity.custom.choocustneed.visitingplace

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.CustomerManageImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.adapter.VisitingPlaceAdapter
import com.toker.sys.view.home.activity.custom.bean.ArriveAreaBean
import com.toker.sys.view.home.activity.custom.bean.VisiPlaceBean
import kotlinx.android.synthetic.main.activity_visiting_area.*
import kotlinx.android.synthetic.main.activity_visiting_place.*
import kotlinx.android.synthetic.main.activity_visiting_place.btn_new_custom_cancel
import kotlinx.android.synthetic.main.activity_visiting_place.btn_new_custom_determine
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 到访地点
 * @author yyx
 */

class VisitingPlaceActivity : BaseActivity<VisitingPlaceContract.View, VisitingPlacePresenter>(),
    VisitingPlaceContract.View {

    override var mPresenter: VisitingPlacePresenter = VisitingPlacePresenter()
    var mBeans: MutableList<VisiPlaceBean.Data> = mutableListOf()
    var adapter: VisitingPlaceAdapter? = null
    override fun layoutResID(): Int = R.layout.activity_visiting_place
    var arriveAreaId = ""
    var projectId = ""
    var bean: VisiPlaceBean.Data? = null
    override fun initView() {
        EventBus.getDefault().register(this)
        arriveAreaId = intent.getStringExtra("arriveAreaId")
        projectId = intent.getStringExtra("projectId")
        tv_title.text = "请选择到访地点"
        mPresenter.loadRepositories()
    }

    override fun initData() {
        setOnClickListener(img_back)
        setOnClickListener(btn_new_custom_cancel)
        setOnClickListener(btn_new_custom_determine)
        rv_visiting.layoutManager = GridLayoutManager(this, 1)
        adapter = VisitingPlaceAdapter(this, mBeans)
        rv_visiting.adapter = adapter
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            btn_new_custom_cancel,
            img_back -> {
                finish()
            }
            btn_new_custom_determine->{
                if (bean == null) {
                    toast("请选择")
                    return
                }
                val intent = Intent()
                intent.putExtra("bean",bean)
                setResult(3,intent)
                finish()
            }
            else -> {
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun VisitingAreaBean(event: VisiPlaceBean.Data) {
        LogUtils.e("VisitingAreaActivity","event.isSelect:"+event.arriveArea)
        bean = event
    }
    //数据展示
    override fun showData(data: MutableList<VisiPlaceBean.Data>) {
        mBeans.addAll(data)
        adapter?.refeshData(mBeans)
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map["arriveAreaId"] = arriveAreaId
        map["projectId"] = projectId
        return map
    }

    override fun getUrl(url_type: Int): String {
        return CustomerManageImp.API_CUSTOMER_ARRIVE_ADDR
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
