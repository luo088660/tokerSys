package com.toker.sys.view.home.fragment.custom.item.invalid

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.R
import com.toker.sys.view.home.fragment.custom.adapter.InvalidAdapter
import com.toker.sys.view.home.fragment.task.bean.PageData
import kotlinx.android.synthetic.main.fragment_invalid.*
import android.support.v4.view.ViewCompat.getMinimumHeight
import android.support.v4.view.ViewCompat.getMinimumWidth
import android.graphics.drawable.Drawable



/**
 * 拓客中层
 * 无效客户
 * @author yyx
 */

class InvalidFragment : BaseFragment<InvalidContract.View, InvalidPresenter>(), InvalidContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): InvalidFragment {
            val args = Bundle()
            val fragment = InvalidFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override var mPresenter: InvalidPresenter = InvalidPresenter()

    private var mBeans = mutableListOf<PageData>()
    private var adapter: InvalidAdapter? = null
    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        log("InvalidFragment", "InvalidFragment")
        if (main_layout == null){
            main_layout = inflateView(R.layout.fragment_invalid, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        rv_t_invalid.layoutManager = GridLayoutManager(context, 1)
    }

    override fun initData() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_invalid_00, null)




        adapter = InvalidAdapter(context!!, mBeans)
        rv_t_invalid.adapter = adapter
        adapter!!.setHeaderView(view)

        setOnClickListener(btn_invalid)
        initSpringView(sp_t_invalid)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
//            btn_invalid -> {sl_invalid.fullScroll(View.FOCUS_UP)
//            }
            else -> {
            }
        }
    }
    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy", "InvalidFragment")
    }
}