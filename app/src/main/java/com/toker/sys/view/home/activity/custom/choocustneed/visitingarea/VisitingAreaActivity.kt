package com.toker.sys.view.home.activity.custom.choocustneed.visitingarea

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.CustomerManageImp
import com.toker.sys.view.home.activity.custom.bean.ArriveAreaBean
import com.toker.sys.view.home.activity.custom.choocustneed.adapter.VisitingAreaAdapter
import com.toker.sys.view.home.activity.custom.choocustneed.visitingarea.bean.VisitingAreaBean
import kotlinx.android.synthetic.main.activity_visiting_area.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 到访地区
 * @author yyx
 */

class VisitingAreaActivity : BaseActivity<VisitingAreaContract.View, VisitingAreaPresenter>(),
    VisitingAreaContract.View {

    override var mPresenter: VisitingAreaPresenter = VisitingAreaPresenter()

    val arriveAreaBeans: MutableList<ArriveAreaBean.Data> = mutableListOf()
    override fun layoutResID(): Int = R.layout.activity_visiting_area
    var adapter :VisitingAreaAdapter?= null
    var bean : ArriveAreaBean.Data?= null
    var projectId=""
    override fun initView() {
        EventBus.getDefault().register(this)
        projectId = intent.getStringExtra("projectId")
        tv_title.text = "请选择到访地区"
        mPresenter.loadRepositories()
    }

    override fun initData() {
        dataView()
        setOnClickListener(img_back)
        setOnClickListener(btn_new_custom_cancel)
        setOnClickListener(btn_new_custom_determine)
        rv_visit_area.layoutManager = GridLayoutManager(this, 1)
        adapter = VisitingAreaAdapter(this, arriveAreaBeans)
        rv_visit_area.adapter = adapter

    }

    private fun dataView() {

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
                setResult(2,intent)
                finish()
            }
            else -> {
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun VisitingAreaBean(event: ArriveAreaBean.Data) {
        Log.e("VisitingAreaActivity","event.isSelect:"+event.arriveArea)
        bean = event
    }

    override fun showArriveAreaData(data: MutableList<ArriveAreaBean.Data>) {
        arriveAreaBeans.clear()
        arriveAreaBeans.addAll(data)
        adapter?.refeshData(arriveAreaBeans)

    }
    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String,String>()
        map["projectId"] = projectId
        return map
    }

    override fun getUrl(url_type: Int): String {
        return   CustomerManageImp.API_URL_CUSTOMER_ARRIVE_AREA
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
