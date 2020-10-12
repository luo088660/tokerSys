package com.toker.sys.view.home.activity.my.perinfor

import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.my.bean.PreInforImageBean
import java.io.File

/**
 * @author yyx
 */

object PerInforContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun cropPhoto(data: Uri)
        abstract fun getImg(): ImageView
        fun showDataIamge(data: PreInforImageBean.Data)
        fun getFileParh():File
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
        fun uploadTaskRecord(file: File)
    }
}
