package com.toker.sys.view.register.useragr

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_user_agr.*
import kotlinx.android.synthetic.main.layout_content_title.*

/**
 * 用户协议
 * @author yyx
 */

class UserAgrActivity : BaseActivity<UserAgrContract.View, UserAgrPresenter>(), UserAgrContract.View{

    override var mPresenter: UserAgrPresenter = UserAgrPresenter()


    override fun layoutResID(): Int  = R.layout.activity_user_agr

    override fun initView() {
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initData() {
        tv_title.text = ""
        img_back.setOnClickListener { finish() }
        wv_user_agr.loadUrl("file:////android_asset/index.html")
/*
        wv_user_agr.webViewClient = object: WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.toString());
                return true;
            }

        };

        //支持App内部javascript交互

        wv_user_agr.settings.javaScriptEnabled = true;

        //自适应屏幕

        wv_user_agr.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN;

        wv_user_agr.settings.loadWithOverviewMode = true;

        //设置可以支持缩放

        wv_user_agr.settings.setSupportZoom(true);

        //扩大比例的缩放

        wv_user_agr.settings.useWideViewPort = true;

        //设置是否出现缩放工具

        wv_user_agr.settings.builtInZoomControls = true;*/
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }
}
