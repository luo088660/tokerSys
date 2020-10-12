package com.toker.sys.view.home.activity.my.recomextpoint

import android.view.View
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.view.home.fragment.my.event.MyEvent
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus

/**
 * 推荐拓客点
 * @author yyx
 */

class RecomExtPointActivity : BaseActivity<RecomExtPointContract.View, RecomExtPointPresenter>(), RecomExtPointContract.View{

    override var mPresenter: RecomExtPointPresenter = RecomExtPointPresenter()


    override fun layoutResID(): Int  = R.layout.activity_recom_ext_point


    override fun initView() {
        tv_title.text = "推荐拓客点"
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
