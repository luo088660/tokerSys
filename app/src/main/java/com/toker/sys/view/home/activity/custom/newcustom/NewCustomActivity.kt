package com.toker.sys.view.home.activity.custom.newcustom

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.*
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import com.toker.sys.R
import com.toker.sys.dialog.ProjectListDialog
import com.toker.sys.dialog.custom.NCExistedDialog
import com.toker.sys.dialog.custom.NCSubPromptDialog
import com.toker.sys.dialog.my.NationProvinDialog
import com.toker.sys.dialog.my.ProjectCityDialog
import com.toker.sys.dialog.my.SelectIntentDialog
import com.toker.sys.dialog.task.ApprTaskResultDialog
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.service.LoaService
import com.toker.sys.utils.network.params.CustomerManageImp
import com.toker.sys.utils.network.params.SystemSettImp
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.view.home.activity.custom.bean.*
import com.toker.sys.view.home.activity.custom.choocustneed.ChooCustNeedActivity
import com.toker.sys.view.home.activity.custom.choocustneed.visitingarea.VisitingAreaActivity
import com.toker.sys.view.home.activity.custom.choocustneed.visitingplace.VisitingPlaceActivity
import com.toker.sys.view.home.activity.custom.event.ChCustStatuBean
import com.toker.sys.view.home.activity.custom.event.NCExistedEvent
import com.toker.sys.view.home.activity.custom.event.NCSubPrompEvent
import com.toker.sys.view.home.activity.custom.selectdd.SelectddActivity
import com.toker.sys.view.home.activity.task.event.ApprovalTaskEvent
import com.toker.sys.view.home.bean.Data
import kotlinx.android.synthetic.main.activity_new_custom.*
import kotlinx.android.synthetic.main.layout_content_title.*
import kotlinx.android.synthetic.main.layout_new_custom_01.*
import kotlinx.android.synthetic.main.layout_new_custom_02.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*
import android.support.annotation.NonNull
import com.toker.sys.utils.tools.LogUtils
import java.io.Serializable


/**
 * 新增客户
 * @author yyx
 */

class NewCustomActivity : BaseActivity<NewCustomContract.View, NewCustomPresenter>(),
    NewCustomContract.View,
    RadioGroup.OnCheckedChangeListener {


    override var mPresenter: NewCustomPresenter = NewCustomPresenter()
    private var projectData: MutableList<Data> = mutableListOf()
    private val sdf = SimpleDateFormat("yyyy/MM/dd")
    private var mDatePicker1: CustomDatePicker? = null

    private var cityBeans: MutableList<CityBean.Data> = mutableListOf()
    private var pjtCityBeans: MutableList<ProjectOnCityBean.Data> = mutableListOf()
    override fun layoutResID(): Int = R.layout.activity_new_custom
    var isUnionSale = "0"
    var arriveAddr = ""
    var tableTag = ""
    var id = ""
    var arriveArea = ""
    var registerAddress = ""
    var registerArea = ""
    var registerCity = ""
    var registerProvince = ""
    var registerStreet = ""
    var registerLatitude  = ""
    var registerLongitude = ""
    var extraType = false
    var mDBean:Serializable? = null
    init {
        mBean = ChCustStatuBean(0)
    }

    companion object {
        var mBean = ChCustStatuBean(0)
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        tv_title.text = "新增客户"
        mPresenter.getProjectList()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        extraType = intent.getBooleanExtra("type", false)
        mDBean = intent.getSerializableExtra("mBean")

        setOnClickListener(ll_new_custom_click)
        setOnClickListener(img_back)
        setOnClickListener(btn_new_custom_cancel)
        setOnClickListener(btn_new_custom_determine)
        setOnClickListener(tv_new_custom_01)
        setOnClickListener(btn_new_custom_2)
        setOnClickListener(btn_new_custom_3)
        setOnClickListener(btn_new_custom_4)
        setOnClickListener(tv_new_custom_05)
        setOnClickListener(btn_new_custom_5)
        setOnClickListener(tv_new_custom2_01)
        setOnClickListener(tv_new_cust_moren)
        setOnClickListener(btn_new_custom_3)
        setOnClickListener(btn_new_custom_4)
        rg_new_custom.setOnCheckedChangeListener(this)
        rg_new_custom.check(if (extraType) R.id.tb_new_custom_02 else R.id.tb_new_custom_01)

        tv_new_custom_05.text = LoaService.address!!.replace("中国", "")

//        btn_new_custom_5.text = sdf.format(Date(System.currentTimeMillis()))
        initDatePicker()
        LogUtils.d(TAG, "mBean:$mBean ");

        tv_new_cst_01!!.text = Html.fromHtml(String.format(getString(R.string.tip_new_custom_01)))
        tv_new_cst_02!!.text = Html.fromHtml(String.format(getString(R.string.tip_new_custom_02)))
        tv_new_cst_03!!.text = Html.fromHtml(String.format(getString(R.string.tip_new_custom_03)))
//        tv_new_cst_04!!.text = Html.fromHtml(String.format(getString(R.string.tip_new_custom_04)))
//        tv_new_cst_05!!.text = Html.fromHtml(String.format(getString(R.string.tip_new_custom_05)))
        tv_new_cst_06!!.text = Html.fromHtml(String.format(getString(R.string.tip_new_custom_06)))
//        tv_new_cst_07!!.text = Html.fromHtml(String.format(getString(R.string.tip_new_custom_07)))
        tv_new_custom_033!!.text = Html.fromHtml(String.format(getString(R.string.tip_new_custom_08)))
        tv_new_custom_044!!.text = Html.fromHtml(String.format(getString(R.string.tip_new_custom_09)))

        registerAddress = LoaService.address
        registerArea = LoaService.district
        registerCity = LoaService.city
        registerProvince = LoaService.province
        registerStreet = LoaService.street
        registerLatitude   = LoaService.latitude
        registerLongitude  = LoaService.longitude


        if (null != mDBean) {
            mDBean as FCustomDetailTBean.Data
            LogUtils.d(TAG, "mDBean:$mDBean ");
            if ((mDBean as FCustomDetailTBean.Data).intentionCustomerVO == null) {
                var customerVO = (mDBean as FCustomDetailTBean.Data)?.recommendVO
                et_new_custom_03.setText(customerVO?.name)
                et_new_custom_04.setText(customerVO?.phone)
                tv_new_custom_05.text = customerVO?.customerAddress
                et_new_custom_033.setText(customerVO?.remark)
//                id = customerVO?.customerId!!
                id = customerVO?.customerId!!
                tableTag = customerVO?.tableTag!!
//                val reCustomerVO = mDBean.reCustomerVO

            } else {
                var customerVO = (mDBean as FCustomDetailTBean.Data).intentionCustomerVO
                id = customerVO.id!!
                tableTag = customerVO.tableTag!!
                et_new_custom_03.setText(customerVO.name)
                et_new_custom_04.setText(customerVO.phone)
                tv_new_custom_05.text = customerVO.customerAddress
                et_new_custom_033.setText(customerVO.remark)
//                id = customerVO?.id!!
                if (!extraType && customerVO.status == "2") {
                    //编辑意向客户 已推荐
                    et_new_custom_03.isEnabled = false
                    tv_new_custom_05.isEnabled = false
                    et_new_custom_06.isEnabled = false
                }
            }
            val vo = (mDBean as FCustomDetailTBean.Data).reCustomerVO

            LogUtils.d(TAG, "vo:$vo ");
            LogUtils.d(TAG, "id--:$id ");

            et_new_custom_04.isEnabled = false

            et_new_custom_06.setText(if (vo.sixNumbers == "2") "" else vo.sixNumbers)
            mBean = ChCustStatuBean(
                1,
                vo.productType,
                vo.productAcreage,
                vo.houseType,
                vo.purchaseFocus
            )
            getProductData(mBean)
            if (!extraType) {
                //编辑意向客户
                et_new_custom_04.isEnabled = false
                tv_title.text = "编辑客户资料"
                rg_new_custom.visibility = GONE
                et_new_custom_00.visibility = VISIBLE
                et_new_custom_00.text = "意向客户"
                intentionLevel = vo.intentionLevel
                tv_new_custom_01.text =
                    if (intentionLevel.isNullOrEmpty()) "" else "${intentionLevel}级"
            } else {
                //编辑意向客户
                tv_title.text = "推荐项目"
                tb_new_custom_01.isEnabled = false
            }
        }


    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            //选择客户需求
            ll_new_custom_click -> {
                val intent = Intent(this, ChooCustNeedActivity::class.java)
                intent.putExtra("bean", mBean)
                startActivityForResult(intent, 1)
            }
            //提交
            btn_new_custom_determine -> {
                LogUtils.d(TAG, "bean:${mBean} ")
                if (et_new_custom_03.text.toString().isEmpty()) {
                    toast("客户姓名不能为空")
                    return
                }
                if (et_new_custom_04.text.toString().isEmpty()) {
                    toast("手机号码不能为空")
                    return
                }
                if (isCheck && et_new_custom_06.text.isEmpty()) {
                    toast("推荐人身份证后六位数不能为空")
                    return
                }

                if (ll_new_custom_03.visibility == VISIBLE) {
                    if (TextUtils.isEmpty("${btn_new_custom_3.text}")) {
                        toast("到访地区不能为空")
                        return
                    }
                    if (TextUtils.isEmpty("${btn_new_custom_4.text}")) {
                        toast("到访地点不能为空")
                        return
                    }

                }
                NCSubPromptDialog(this, "确认提交吗?")
            }
            btn_new_custom_cancel,
            img_back -> finish()
            //意向楼盘
            btn_new_custom_2 -> {

                if (pjtCityBeans.isNotEmpty()) {
                    ProjectCityDialog(this, pjtCityBeans)
                } else {
                    ProjectListDialog(this, projectData);
                }
            }
            //到访地区选择
            btn_new_custom_3 -> {
                if ("${btn_new_custom_2.text}".isEmpty()) {
                    return
                }
                val intent = Intent(this, VisitingAreaActivity::class.java)
                intent.putExtra("projectId", projectId)
                startActivityForResult(intent, 2)
            }
            //到访地点选择
            btn_new_custom_4 -> {
                if ("${btn_new_custom_3.text}".isEmpty()) {
                    return
                }
                val intent = Intent(this, VisitingPlaceActivity::class.java)
                intent.putExtra("arriveAreaId", arriveAreaId)
                intent.putExtra("projectId", projectId)
                startActivityForResult(intent, 2)
            }
            tv_new_custom_05 -> {
                //选择地址
                val b = lacksPermissions(this)
                LogUtils.d(TAG, "b:$b ");
                if (b) {
                    toast("请打开定位权限")
                    requestPermission()
                    return
                } else {
                    val intent = Intent(this, SelectddActivity::class.java)
                    startActivityForResult(intent, 2)
                }

            }
            //选择省份
            tv_new_custom2_01 -> NationProvinDialog(this, cityBeans)
            //选择意向级别
            tv_new_custom_01 -> {
                SelectIntentDialog(this)
            }
            //时间选择
            btn_new_custom_5 -> {
                mDatePicker1?.show(System.currentTimeMillis())
            }
            //默认
            tv_new_cust_moren -> {
                btn_new_custom_2.text = this.projectData[0].projectName
                projectId = this.projectData[0].projectId
                ll_new_custom_03.visibility = GONE
                tv_new_custom2_01.text = ""
                btn_new_custom_3.text = ""
                btn_new_custom_4.text = ""
                arriveAreaId = ""
                arriveAddr = ""
                arriveArea = ""
                pjtCityBeans.clear()
            }

            else -> {
            }
        }
    }
    //我的项目

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showProjectDate(data: Data) {
        btn_new_custom_2.text = data.projectName
        projectId = data.projectId
    }

    //选择省份
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun nPBean(event: CityBean.Data) {
        tv_new_custom2_01.text = event.provinceName
        provinceId = event.provinceId
        mPresenter.getProjectOnCityId()
        //TODO 清除意向楼盘,到访地区，到访地点
        ll_new_custom_03.visibility = GONE
        btn_new_custom_2.text = ""
        btn_new_custom_3.text = ""
        btn_new_custom_4.text = ""
        arriveAreaId = ""
        projectId = ""
        arriveAddr = ""
        arriveArea = ""
    }

    var intentionLevel = ""
    var projectId = ""
    var subProjectCode = ""
    var provinceId = ""
    var arriveAreaId = ""
    //选择意向级别
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun nPBean(event: SelectIntentDialog.SelectIntentBean) {
        tv_new_custom_01.text = "${event.type}级"
        intentionLevel = event.type
    }

    //选择意向楼盘

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun nPBean(event: ProjectOnCityBean.Data) {
        btn_new_custom_2.text = "${event.projectName}"

        subProjectCode = event.subProjectCode
        projectId = event.id
        ll_new_custom_03.visibility = if (event.isUnionSale == "1") VISIBLE else GONE
        isUnionSale = event.isUnionSale
        //TODO 清除 到访地区，到访地点
        btn_new_custom_3.text = ""
        btn_new_custom_4.text = ""
        arriveAreaId = ""
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ApprovalTaskEvent(event: ApprovalTaskEvent) {
        when (event.type) {
            2 -> {
                finish()
            }
            else -> {
            }
        }
    }

    override fun showSuccess(data: String) {
        if (TextUtils.isEmpty(data)) {
            ApprTaskResultDialog(this, data)
        } else {
            //TODO
            ApprTaskResultDialog(this, if (isCheck) "推荐客户成功" else {
                if (mDBean != null && !extraType)"保存成功" else "新增客户成功"
            })
        }
    }

    override fun showError(desc: String) {
        if (desc.contains("请联系管理员")) {
            NCExistedDialog(this,"推荐失败,该拓客未认证恒房通")
        }else{
            NCExistedDialog(this, desc)
        }

    }

    //省份数据
    override fun showCityData(data: MutableList<CityBean.Data>) {
        cityBeans.clear()
        cityBeans.addAll(data)
    }

    //选择项目
    override fun showProjectCityData(data: MutableList<ProjectOnCityBean.Data>) {
        pjtCityBeans.clear()
        pjtCityBeans.addAll(data)
    }

    var arriveAreaBeans: MutableList<ArriveAreaBean.Data> = mutableListOf()
    //选择到访地区
    override fun showArriveAreaData(data: MutableList<ArriveAreaBean.Data>) {
        arriveAreaBeans.clear()
        arriveAreaBeans.addAll(data)
    }


    /**
     * 确认提交
     *
     * 已存在 NCExistedDialog
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun NCSubPrompEvent(event: NCSubPrompEvent) {
        if (!isCheck) {
            //新增意向客户
            mPresenter.loadRepositories()
        } else {
            mPresenter.customerRecommend()
        }

    }

    /**
     *提示 新增客户异常
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun NCExistedEvent(event: NCExistedEvent) {
        ApprTaskResultDialog(this, "新增客户成功")
    }

    var isCheck = false
    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

        val drawable = resources.getDrawable(R.mipmap.icon_xunzhong)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        when (checkedId) {
            R.id.tb_new_custom_01 -> {//icon_xunzhong
                tb_new_custom_01.setCompoundDrawables(drawable, null, null, null)
                tb_new_custom_02.setCompoundDrawables(null, null, null, null)
                ll_new_custom_01.visibility = VISIBLE
                ll_new_custom_02.visibility = GONE
                tv_new_cst_04.text = "被推荐人身份\n证后六位"
                et_new_custom_06.hint = "非必填"
                isCheck = false
            }
            else -> {
                tb_new_custom_01.setCompoundDrawables(null, null, null, null)
                tb_new_custom_02.setCompoundDrawables(drawable, null, null, null)
                mPresenter.getCities()
                ll_new_custom_01.visibility = GONE
                ll_new_custom_02.visibility = VISIBLE
                tv_new_cst_04!!.text =
                    Html.fromHtml(String.format(getString(R.string.tip_new_custom_04)))
                et_new_custom_06.hint = "必填"
                isCheck = true
            }
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogUtils.d(TAG, "requestCode: $requestCode")
        LogUtils.d(TAG, "resultCode: $resultCode")

        when (resultCode) {
            1 -> {
                mBean = data!!.getSerializableExtra("bean") as ChCustStatuBean
                LogUtils.d(TAG, "mBean:$mBean ");
                getProductData(mBean)
            }
            2 -> {
                val bean = data!!.getSerializableExtra("bean") as ArriveAreaBean.Data
                btn_new_custom_3.text = bean.arriveArea
                arriveAreaId = bean.arriveAreaId
                arriveArea = bean.arriveArea
                btn_new_custom_4.text = ""
            }
            3 -> {
                val bean = data!!.getSerializableExtra("bean") as VisiPlaceBean.Data
                btn_new_custom_4.text = bean.arriveArea
                arriveAddr = bean.arriveArea

            }
            4 -> {
                val bean = data!!.getSerializableExtra("bean") as RegisterBean
                tv_new_custom_05.text = bean.registerAddress
                registerAddress = bean.registerAddress
                registerArea = bean.registerArea
                registerCity = bean.registerCity
                registerProvince = bean.registerProvince
                registerStreet = bean.registerStreet
                registerLatitude = bean.registerLatitude
                registerLongitude = bean.registerLongitude
            }
            else -> {
            }
        }
    }

    //用户购房需求
    private fun getProductData(mBean1: ChCustStatuBean) {
        mBean1.productType = mBean1.productType?.trim()
        mBean1.productAcreage = mBean1.productAcreage?.trim()
        mBean1.houseType = mBean1.houseType?.trim()
        mBean1.purchaseFocus = mBean1.purchaseFocus?.trim()
        var replace =
            "${if (mBean1.productType.isNullOrEmpty()) "" else "${mBean1.productType},"}${if (mBean1.productAcreage.isNullOrEmpty()) "" else "${mBean1.productAcreage},"}${if (mBean1.houseType.isNullOrEmpty()) "" else "${mBean1.houseType},"},${if (mBean1.purchaseFocus.isNullOrEmpty()) "" else "${mBean1.purchaseFocus}"}".replace(
                ",,",
                ","
            )
        if (replace.startsWith(",")) {
            replace = replace.substring(1, replace.length)
        }
        if (replace.endsWith(",")) {
            replace = replace.substring(0, replace.length - 1)
        }
        LogUtils.d(TAG, "replace:$replace ");
        if (!replace.contains("2,2")) {
            tv_new_custom_click_00.text = replace
        } else {
            mBean = ChCustStatuBean(0)
        }
        tv_new_custom_click.visibility = GONE
        LogUtils.d(TAG, "bean:${mBean1} ")
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {

        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
                map["customerProvince"] = LoaService.province
                map["phone"] = et_new_custom_04.text.toString()
//
                if (!TextUtils.isEmpty(mBean.houseType)) {
                    map["houseType"] = mBean.houseType!!
                }
                if (!TextUtils.isEmpty(mBean.productAcreage)) {
                    map["productAcreage"] = mBean.productAcreage!!
                }
                if (!TextUtils.isEmpty(mBean.productType)) {
                    map["productType"] = mBean.productType!!
                }
                if (!TextUtils.isEmpty(mBean.purchaseFocus)) {
                    map["purchaseFocus"] = mBean.purchaseFocus!!
                }
                map["customerCity"] = LoaService.city
                if (!TextUtils.isEmpty(intentionLevel)) {
                    map["intentionLevel"] = intentionLevel
                }


                map["name"] = et_new_custom_03.text.toString()
                map["sixNumbers"] = et_new_custom_06.text.toString()
                map["customerArea"] = registerArea
                map["customerLatitude"] = registerLatitude
                map["customerLongitude"] = registerLongitude
                map["customerStreet"] = registerStreet
                map["customerAddress"] =registerAddress

                map["registerAddress"] = LoaService.address
                map["registerArea"] = LoaService.district
                map["registerCity"] = LoaService.city
                map["registerProvince"] = LoaService.province
                map["registerStreet"] =  LoaService.street
                //备注
                map["remark"] = et_new_custom_033.text.toString()

                if (!TextUtils.isEmpty(id)) {
                    map["id"] = id //  *	string客户id
                }

                if (!TextUtils.isEmpty(tableTag)) {
                    map["tableTag"] = tableTag// *	string tableTag
                }
            }
            3 -> {

                map["customerProvince"] = registerProvince
                map["customerCity"] = registerCity
                map["customerArea"] = registerArea
                map["customerLatitude"] = registerLatitude
                map["customerLongitude"] = registerLongitude
                map["customerStreet"] = registerStreet
                map["customerAddress"] =registerAddress

                map["projectCity"] ="${tv_new_custom2_01.text}"

                map["registerAddress"] = LoaService.address
                map["registerArea"] =  LoaService.district
                map["registerCity"] = LoaService.city
                map["registerProvince"] = LoaService.province
                map["registerStreet"] = LoaService.street

                map["isUnionSale"] = isUnionSale//?
                map["name"] = "${et_new_custom_03.text}"
                map["phone"] = "${et_new_custom_04.text}"

                if (!TextUtils.isEmpty(mBean.houseType)) {
                    map["houseType"] = mBean.houseType!!
                }

                if (!TextUtils.isEmpty(mBean.productAcreage)) {
                    map["productAcreage"] = mBean.productAcreage!!
                }

                if (!TextUtils.isEmpty(mBean.productType)) {
                    map["productType"] = mBean.productType!!
                }

                if (!TextUtils.isEmpty(mBean.purchaseFocus)) {
                    map["purchaseFocus"] = mBean.purchaseFocus!!
                }

                map["projectId"] = if (pjtCityBeans.isNotEmpty()) subProjectCode else projectId
//                map["province"] = "${tv_new_custom2_01.text}"
                map["recommendProject"] = btn_new_custom_2.text.toString()

                map["remark"] = et_new_custom_033.text.toString()
                map["sixNumbers"] = et_new_custom_06.text.toString()
                map["visitTime"] = btn_new_custom_5.text.toString()

                if (!TextUtils.isEmpty(arriveAddr)) {
                    map["arriveAddr"] = arriveAddr // 	string到访地点
                }

                if (!TextUtils.isEmpty(arriveArea)) {
                    map["arriveArea"] = arriveArea // 	string到访地区
                }else{
                    //城市
                    map["arriveArea"] ="${tv_new_custom2_01.text}"
                }
                LogUtils.d(TAG, "id--:$id ");
                if (!TextUtils.isEmpty(id)) {
                    map["id"] = id //  *	string客户id
                }

                if (!TextUtils.isEmpty(tableTag)) {
                    map["tableTag"] = tableTag// *	string tableTag
                }

            }
            5 -> {
                map["provinceId"] = provinceId
            }

            6 -> {
                map["projectId"] = projectId
            }
            7 -> {
                map["arriveAreaId"] = arriveAreaId
            }
            else -> {
            }
        }

        return map
    }

    /**
     * 时间选择
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initDatePicker() {
        // 通过时间戳初始化日期，毫秒级别
        mDatePicker1 = CustomDatePicker(this, object : CustomDatePicker.CallbackSEListen {
            override fun onTimeSelected(startTime: Long, endTime: Long, timeType: Int) {
                if (startTime == 0L){
                    btn_new_custom_5.text = sdf.format(Date(System.currentTimeMillis()))
                    return
                }

                LogUtils.d(TAG, "timeType:$timeType ");
                btn_new_custom_5.text = sdf.format(Date(startTime))
            }
        }, 5, isVisiTab = false, isSEndTime = false)

        // 不允许点击屏幕或物理返回键关闭
//        mDatePicker1?.setCancelable(false)
        // 不显示时和分
        mDatePicker1?.setCanShowPreciseTime(false)
        // 不允许循环滚动
        mDatePicker1?.setScrollLoop(true)
        // 不允许滚动动画
        mDatePicker1?.setCanShowAnim(false)


    }

    //我的项目
    override fun showProjectDate(data: MutableList<Data>) {
        if (!data.isNullOrEmpty()) {
            this.projectData.addAll(data)
            btn_new_custom_2.text = data[0].projectName
            projectId = data[0].projectId
            EventBus.getDefault().post(data)
        }

    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> CustomerManageImp.API_CUST_customer_Add
            2 -> SystemSettImp.API_SYSTEM_PROJECT_LIST
            3 -> CustomerManageImp.API_CUST_MANAGE_RECOMMEND
            //获取所有省份
            4 -> CustomerManageImp.API_CUSTOMER_CITIES
            //根据省份ID获取省份下的项目
            5 -> CustomerManageImp.API_CUSTOMER_PROJECT_ONCITYID
            //获取联销项目地区
            6 -> CustomerManageImp.API_URL_CUSTOMER_ARRIVE_AREA
            //获取联销项目地点
            7 -> CustomerManageImp.API_CUSTOMER_ARRIVE_ADDR

            else -> ""
        }

    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    fun permissionsREAD() = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    /**
     * 判断权限集合
     * permissions 权限数组
     * return true-表示没有改权限 false-表示权限已开启
     */
    fun lacksPermissions(mContexts: Context): Boolean {
        for (permission in permissionsREAD()) {
            if (lacksPermission(mContexts, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否缺少权限
     */
    private fun lacksPermission(mContexts: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            mContexts,
            permission
        ) === PackageManager.PERMISSION_DENIED
    }

    /**
     * 添加权限
     */
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 0
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            for (i in permissions.indices) {
                if (grantResults[i] != -1) {
                    //T.showShort(mContext,"权限设置成功");
                    initDatS()
                }
            }

        }
    }


    fun initDatS() {
        val intent = Intent(this, LoaService::class.java)
        bindService(intent, connection, BIND_AUTO_CREATE)
        LoaService.handler = handler

    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            //获取服务的代理对象
//            binder = service as LoaService.MyBinder
        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    /**
     * 接受到消息后将城市名显示出来
     */
    private val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
//            if (binder != null) {
//                log("----数据${binder!!.city}", "MainActivity")
//            }
        }
    }

}
