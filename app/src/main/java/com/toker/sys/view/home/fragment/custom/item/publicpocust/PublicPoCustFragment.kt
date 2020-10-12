package com.toker.sys.view.home.fragment.custom.item.publicpocust

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.toker.sys.R
import com.toker.sys.dialog.custom.PublicPoCustDialog
import com.toker.sys.dialog.custom.SelectUserDialog
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.CustomerManageImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.custom.fcustomdetail.FCustomDetailActivity
import com.toker.sys.view.home.fragment.custom.adapter.PublicPoCustAdapter
import com.toker.sys.view.home.fragment.custom.bean.FollowCustBean
import com.toker.sys.view.home.fragment.custom.bean.PublicPoCust
import com.toker.sys.view.home.fragment.custom.bean.PublicPoCustBean
import com.toker.sys.view.home.fragment.custom.bean.SelectUserList
import com.toker.sys.view.home.fragment.custom.event.MyCusEvent
import com.toker.sys.view.home.fragment.custom.event.SelectUserEvent
import com.toker.sys.view.home.fragment.event.CustomProjectEvent
import kotlinx.android.synthetic.main.fragment_public_po_cust.*
import kotlinx.android.synthetic.main.layout_public_po_cust_01.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 拓客管理员
 * 公客池
 * @author yyx
 */

class PublicPoCustFragment : BaseFragment<PublicPoCustContract.View, PublicPoCustPresenter>(),
    PublicPoCustContract.View, PublicPoCustAdapter.RecyclerViewOnItemClickListener {

    override fun onItemClickListener(view: View, position: Int) {
        poCustAdapter?.MultiSelection(position)
    }

    companion object {
        @JvmStatic
        fun newInstance(): PublicPoCustFragment {
            val args = Bundle()
            val fragment = PublicPoCustFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var fList = mutableListOf<Map<String, String>>()
    private var tkName: String = ""
    var page = "1"
    var Userpage = "1"
    var condition = ""
    private var mBeans = mutableListOf<FollowCustBean.PageData>()
    var poCustAdapter: PublicPoCustAdapter? = null
    private var isChoose = false
    private var records = mutableListOf<PublicPoCustBean.Record>()
    private var list = mutableListOf<Int>()//创建数据
    private val listDatas = mutableListOf<FollowCustBean.PageData>()//记录选择的数据
    private var tv01: TextView? = null
    private var tv06: TextView? = null
    private var cb01: CheckBox? = null
    private var tv11: TextView? = null
    private var et01: EditText? = null
    private var tvProject: TextView? = null
    private var custDialog: PublicPoCustDialog? = null
    private var selectUserDialog: SelectUserDialog? = null
    private var puPoCustEvent: PublicPoCust? = null

    override var mPresenter: PublicPoCustPresenter = PublicPoCustPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_public_po_cust, container!!)
        }
        return main_layout!!
    }

    override fun initView() {

//        mPresenter.loadRepositories()
        rv_public_po_cust.layoutManager = GridLayoutManager(activity, 1)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)

        onFRefresh()
    }

    override fun initData() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_public_po_cust_01, null)

        tvProject = view.findViewById<TextView>(R.id.tv_receivde1_02)
        tv11 = view.findViewById<TextView>(R.id.tv_receivde1_011)
        et01 = view.findViewById<EditText>(R.id.tv_receivde1_01)
        tv01 = view.findViewById<TextView>(R.id.tv_po_cust_05)
        tv06 = view.findViewById<TextView>(R.id.tv_po_cust_06)
        cb01 = view.findViewById<CheckBox>(R.id.cbx_po_cust)


        poCustAdapter = PublicPoCustAdapter(activity!!, mBeans, 1)

        rv_public_po_cust.adapter = poCustAdapter
        poCustAdapter!!.setHeaderView(view)
        rv_public_po_cust.addItemDecoration(getItemDecoration())
        setOnClickListener(tvProject!!)
        setOnClickListener(cb01!!)
        setOnClickListener(tv06!!)
        setOnClickListener(tv11!!)
        poCustAdapter?.setItemClickListener(this)
        initSpringView(sp_t_public_po_cust)
    }


    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            tvProject -> EventBus.getDefault().post(CustomProjectEvent(1))
            //全选
            cb01 -> {
                if (!isChoose) {
                    isChoose = true
                    poCustAdapter?.All()
                    cbx_po_cust.text = "取消全选"

                } else {
                    isChoose = false
                    poCustAdapter?.neverall()
                    cbx_po_cust.text = "全选"

                }
            }
            //批量分配
            tv06 -> {

                listDatas.clear()
                fList.clear()


                val map = poCustAdapter?.getMap()
                map?.forEach {
                    LogUtils.d(TAG, "map:${it.key}--${it.value} ");
                    if (it.value) {
                        val element = mBeans[it.key - 1]
                        listDatas.add(element)
                        //组装选中分配数据
                        val hmap = HashMap<String, String>()
                        hmap["id"] = element.id
                        hmap["tableTag"] = element.tableTag
                        fList.add(hmap)
                    }
                }
                val id: String = listDatas.joinToString {
                    "${it.id}"
                }.replace(" ", "")
                val name: String = listDatas.joinToString {
                    "${it.name}"
                }.replace(" ", "")
                val phone: String = listDatas.joinToString {
                    "${it.phone}"
                }.replace(" ", "")
                val tableTag: String = listDatas.joinToString {
                    "${it.tableTag}"
                }.replace(" ", "")
                LogUtils.d(TAG, "it.id:${id} ");
                LogUtils.d(TAG, "it.name:${name} ");
                LogUtils.d(TAG, "it.phone:${phone} ");
                LogUtils.d(TAG, "it.tableTag:${tableTag} ");
                if (TextUtils.isEmpty(id)) {
                    toast("请选择客户")
                    return
                }
                custDialog = PublicPoCustDialog(context!!, PublicPoCust(name, phone, id, tableTag))
            }
            tv11 -> {
                condition = "${et01!!.text}"
                onFRefresh()
            }
            else -> {
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ProjectList(event: com.toker.sys.view.home.bean.Data) {
        tvProject!!.text = event.projectName
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun MyCusEvent(event: MyCusEvent) {
        val intent = Intent(activity, FCustomDetailActivity::class.java)
        intent.putExtra("type", 2)
        intent.putExtra("bean", event.mBeans)
        activity!!.startActivity(intent)
    }

    override fun onFRefresh() {
        super.onFRefresh()
        mBeans.clear()
        page = "1"
        mPresenter.loadRepositories()
    }

    override fun onFLoadmore() {
        super.onFLoadmore()

        var toInt = page.toInt()
        toInt++
        page = toInt.toString()
        mPresenter.loadRepositories()
    }

    override fun showData(data: FollowCustBean.Data) {
        list.clear()
        mBeans.addAll(data.pageData)
        for (i in 0..mBeans.size) {
            list.add(i)
        }
        poCustAdapter?.refreshData(mBeans)

        tv01!!.text = Html.fromHtml(String.format(getString(R.string.tip_start_04), data.rowTotal))
    }

    //分配拓客员
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun RecordEvent(event: FollowCustBean.PageData) {
        LogUtils.d(TAG, "RecordEvent: ");
        custDialog =
            PublicPoCustDialog(
                context!!,
                PublicPoCust(event.name, event.phone, event.id, event.tableTag)
            )
        val hmap = HashMap<String, String>()
        hmap["id"] = event!!.id
        hmap["tableTag"] = event!!.tableTag
        fList.add(hmap)
        LogUtils.d(
            TAG,
            "Arrays.toString(fList.toTypedArray()):${Arrays.toString(fList.toTypedArray())} "
        );
        LogUtils.d(TAG, "Arrays.toString(fList.toTypedArray()):${fList} ");
    }

    //获取拓客员列表
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun selectUserEvent(event: SelectUserEvent) {
        this.tkName = event.name
        LogUtils.d(TAG, "event.page: ${event.page}");
        Userpage = "${event.page}"
        if (Userpage == "1") {
            recordsBeans.clear()
        }
        mPresenter.selectUserEvent()
    }

    //确认拓客员名称
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun recoRd(event: SelectUserList.PageData) {
        selectUserDialog = null
        custDialog?.setData(event)
    }

    //分配客户
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun publicPoCust(event: PublicPoCust) {
        this.puPoCustEvent = event
        mPresenter.publicPoCust()
    }

    var recordsBeans: MutableList<SelectUserList.PageData> = mutableListOf()
    override fun showUserList(records: MutableList<SelectUserList.PageData>) {
        LogUtils.d(TAG, "showUserList: ${records.toString()}")
        if (selectUserDialog == null) {
            selectUserDialog = SelectUserDialog(context!!, records)
        } else {
            selectUserDialog?.setData(records)
        }
    }

    //拓客分配结果
    override fun showAllocation() {
        toast("分配成功")
        fList.clear()
        onFRefresh()
    }

    override fun showAllocationError(desc: String) {
        fList.clear()
        toast(desc)
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
                map["validFlag"] = "1"
                map["status"] = "1"
                if (condition.isNotEmpty()) {
                    map["condition"] = condition
                }
                map["pageSize"] = "10"
                map["page"] = page
            }
            2 -> {
                map["name"] = this.tkName
                map["pageSize"] = "10"
                map["page"] = Userpage
            }
            3 -> {
                map["customerIds"] = JSON.toJSONString(fList)
                map["status"] = "1"
//                map["tableTag"] = this.puPoCustEvent!!.tableTag
                map["userId"] = this.puPoCustEvent!!.tkId!!
            }

            else -> {
            }
        }
        return map
    }

    override fun getUrl(url_type: Int): String {

        return when (url_type) {
            1 -> CustomerManageImp.API_CUST_MANAGE_LIST
            2 -> CustomerManageImp.API_CUST_SELECT_USER_LIST
            3 -> CustomerManageImp.API_CUST_CUSTOMER_ALLOCATION

            else -> ""

        }
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }
}

