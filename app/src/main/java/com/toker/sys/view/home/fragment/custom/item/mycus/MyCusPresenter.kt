package com.toker.sys.view.home.fragment.custom.item.mycus

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import com.toker.reslib.recyckerview.OnItemMenuClickListener
import com.toker.reslib.recyckerview.SwipeMenu
import com.toker.reslib.recyckerview.SwipeMenuCreator
import com.toker.reslib.recyckerview.SwipeMenuItem
import com.toker.reslib.recyckerview.SwipeRecyclerView
import com.toker.reslib.recyckerview.widget.DefaultItemDecoration
import com.toker.sys.R
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.fragment.custom.bean.FollowCTBean
import org.json.JSONObject

/**
 * @author yyx
 */

class MyCusPresenter() : BasePresenter<MyCusContract.View>(), MyCusContract.Presenter {
    var context: Context? = null
    fun getContext(context: Context?) {
        this.context = context
    }


    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {
        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, FollowCTBean.FollowCsTBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showData(toBean.data)

                }
            }
            2->{
                val toBean = GsonUtil.GsonToBean(toJson, Beandata::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.deleteSuccess()
                }
            }
            else -> {
            }
        }

    }

    override fun loadRepositories() {
        reqData(1)
    }

    override fun deleteView() {

        reqData(2)
    }

    public fun createLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }

    public fun createItemDecoration(): RecyclerView.ItemDecoration {
        return DefaultItemDecoration(ContextCompat.getColor(context!!, R.color.divider_color))
    }

    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    public val swipeMenuCreator = object : SwipeMenuCreator {
        override fun onCreateMenu(swipeLeftMenu: SwipeMenu, swipeRightMenu: SwipeMenu, position: Int) {
            LogUtils.d(TAG, "position:${position} ");
            if (position == 0) {
                return
            }

            val width = context!!.resources.getDimensionPixelSize(R.dimen.dimen_70)

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            val height = ViewGroup.LayoutParams.MATCH_PARENT

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            run {
                val deleteItem = SwipeMenuItem(context).setBackground(R.color.btn_red)
                    .setImage(R.mipmap.icon_delete)
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height)
                swipeRightMenu.addMenuItem(deleteItem)// 添加菜单到右侧。
            }
        }
    }

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    public val mMenuItemClickListener = OnItemMenuClickListener { menuBridge, position ->
        menuBridge.closeMenu()

        val direction = menuBridge.direction // 左侧还是右侧菜单。
        val menuPosition = menuBridge.position // 菜单在RecyclerView的Item中的Position。

        if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
            if (menuPosition == 0) {
                Toast.makeText(context, "删除list第$position; 右侧菜单第$menuPosition", Toast.LENGTH_SHORT)
                    .show()

                mView?.deleteView(position)

            }

        }
    }

    class Beandata(
        val `data`: Any,
        val code: String,
        val desc: String
    ){
        /**
         * 请求是否成功
         */
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }
}