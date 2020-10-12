package com.toker.sys.view.home.activity.custom.choocustneed

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.view.home.activity.custom.choocustneed.adapter.ChCustStatuAdapter
import kotlinx.android.synthetic.main.activity_choo_cust_need.*
import kotlinx.android.synthetic.main.layout_content_title.*
import android.view.MotionEvent
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.custom.event.ChCustStatuBean
import com.toker.sys.view.home.activity.custom.event.ChCustStatuEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception
import java.util.regex.Pattern


/**
 * 选择客户需求
 * @author yyx
 */

class ChooCustNeedActivity : BaseActivity<ChooCustNeedContract.View, ChooCustNeedPresenter>(),
    ChooCustNeedContract.View {

    private val mBeans1 = arrayOf("高层", "洋房", "别墅", "公寓", "商铺", "写字楼")
    private val mBeans2 = arrayOf("小于60㎡", "60~80㎡", "80~100㎡", "100~120㎡", "120~140㎡", "140㎡以上")
    private val mBeans3 = arrayOf("一室", "二室", "三室", "四室", "五室及以上")
    private val mBeans4 = arrayOf("地段", "学区", "周边配套", "区域发展前景", "自然生态环境", "精装修", "现房", "物业服务")
    override var mPresenter: ChooCustNeedPresenter = ChooCustNeedPresenter()

    var bean: ChCustStatuBean = ChCustStatuBean(0)

    override fun layoutResID(): Int = R.layout.activity_choo_cust_need

    override fun initView() {
        EventBus.getDefault().register(this)
        val bean = intent!!.getSerializableExtra("bean") as ChCustStatuBean
        LogUtils.d(TAG, "bean:$bean ");
        if (bean.type == 1) {
            this.bean = bean
        }
        tv_title.text = "请选择购房客户需求"
    }

    override fun initData() {
        setOnClickListener(img_back)
        rv_choo_cust_01.layoutManager = GridLayoutManager(this, 3)
        rv_choo_cust_02.layoutManager = GridLayoutManager(this, 3)
        rv_choo_cust_03.layoutManager = GridLayoutManager(this, 3)
        rv_choo_cust_04.layoutManager = GridLayoutManager(this, 3)
        //产品类型
        val statuAdapter1 = ChCustStatuAdapter(this, mBeans1, 1)
        rv_choo_cust_01.adapter = statuAdapter1
        val statuAdapter2 = ChCustStatuAdapter(this, mBeans2, 2)
        rv_choo_cust_02.adapter = statuAdapter2
        val statuAdapter3 = ChCustStatuAdapter(this, mBeans3, 3)
        rv_choo_cust_03.adapter = statuAdapter3
        val statuAdapter4 = ChCustStatuAdapter(this, mBeans4, 4)
        rv_choo_cust_04.adapter = statuAdapter4
        rv_choo_cust_04.isNestedScrollingEnabled = false

        setItemDecoration(rv_choo_cust_01)
        setItemDecoration(rv_choo_cust_02)
        setItemDecoration(rv_choo_cust_03)
        setItemDecoration(rv_choo_cust_04)

        setOnClickListener(btn_choo_cust_01)
        setOnClickListener(btn_choo_cust_02)
//        bean:ChCustStatuEvent(type=1, productType=洋房,商铺, productAcreage=60~80㎡,120~140㎡, houseType=二室,五室及以上, purchaseFocus=学区,自然生态环境,物业服务)

        LogUtils.d(TAG, "bean11:$bean ");
        if (bean.type == 1) {
            refreshAdapterDate(bean.productType, statuAdapter1)
            refreshAdapterDate(bean.productAcreage, statuAdapter2)
            refreshAdapterDate(bean.houseType, statuAdapter3)
            refreshAdapterDate(bean.purchaseFocus, statuAdapter4)
        }

    }

    //刷新 列表数据
    private fun refreshAdapterDate(
        productType: String?,
        statuAdapter1: ChCustStatuAdapter
    ) {
        var productType1 = productType
        if (productType1 != null) {
            var beanType = mutableListOf<String>()
            var i1 = productType1.indexOf(",")
            val i = appearNumber(productType1, ",")
            LogUtils.d(TAG, "i: $i");
            for (i2 in 0 until i) {
                LogUtils.d(TAG, "i2: $i2");
                val substring = productType1!!.substring(0, i1)!!
                beanType.add(substring)
                LogUtils.d(TAG, "$i---$substring")
                productType1 = productType1?.substring(i1 + 1, productType1.length)
                i1 = productType1!!.indexOf(",")


            }
            beanType.add(productType1!!)
            statuAdapter1.refeshData(beanType)
        }
    }

    /**
     * 获取指定字符串出现的次数
     *
     * @param srcText  源字符串
     * @param findText 要查找的字符串
     * @return
     */
    fun appearNumber(srcText: String, findText: String): Int {
        var count = 0
        val p = Pattern.compile(findText)
        val m = p.matcher(srcText)
        while (m.find()) {
            count++
        }
        return count
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back,
            btn_choo_cust_01 -> finish()
            btn_choo_cust_02 -> {
                LogUtils.d(TAG, "bean:${bean.toString()} ");
                intent.putExtra("bean", bean)
                setResult(1, intent)
                finish()
            }
            else -> {
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun chCustStatuEvent(event: ChCustStatuEvent) {
        LogUtils.d(TAG, "event.name:${event.name}");
        bean.type = 1
        when (event.type) {
            1 -> {

                bean.productType = if (event.name == "0") "" else event.name?.length?.minus(1)?.let {
                    event.name?.substring(
                        0,
                        it
                    )
                }
            }
            2 -> {
                bean.productAcreage = if (event.name == "0") "" else event.name?.length?.minus(1)?.let {
                    event.name?.substring(
                        0,
                        it
                    )
                }
            }
            3 -> {
                bean.houseType = if (event.name == "0") "" else event.name?.length?.minus(1)?.let {
                    event.name?.substring(
                        0,
                        it
                    )
                }
            }
            else -> {
                bean.purchaseFocus = if (event.name == "0") "" else event.name?.length?.minus(1)?.let {
                    event.name?.substring(
                        0,
                        it
                    )
                }
            }
        }
        LogUtils.d(TAG, "bean:$bean")
    }


    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    //设置分隔
    private fun setItemDecoration(rv_choo_cust: RecyclerView) {
        rv_choo_cust.addItemDecoration(getRecyclerView1())
        rv_choo_cust.addItemDecoration(getRecyclerView2())
    }

    /**
     * 获取分割线
     * @param drawableId 分割线id
     * @return
     */
    private fun getRecyclerView1(): RecyclerView.ItemDecoration {
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
        itemDecoration.setDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.search_history_solid
            )!!
        )
        return itemDecoration
    }

    fun getRecyclerView2(): RecyclerView.ItemDecoration {
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.search_history_solid
            )!!
        )
        return itemDecoration
    }

    fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        return action != MotionEvent.ACTION_DOWN
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
