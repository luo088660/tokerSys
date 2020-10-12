package com.toker.sys.view.home.activity.custom.editcustominfor

import android.view.View
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import kotlinx.android.synthetic.main.layout_content_title.*

/**
 * 编辑客户资料
 * @author yyx
 */

class EditCustomInforActivity : BaseActivity<EditCustomInforContract.View, EditCustomInforPresenter>(), EditCustomInforContract.View{

    override var mPresenter: EditCustomInforPresenter = EditCustomInforPresenter()


    override fun layoutResID(): Int  = R.layout.activity_edit_custom_infor
    override fun initView() {
        tv_title.text = "编辑客户资料"
    }

    override fun initData() {
        setOnClickListener(img_back)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            else -> {
            }
        }
    }


    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }
}
