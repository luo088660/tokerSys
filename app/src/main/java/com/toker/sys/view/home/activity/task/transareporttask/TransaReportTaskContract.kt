package com.toker.sys.view.home.activity.task.transareporttask

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.task.bean.TransactTaskBean
import com.toker.sys.view.home.activity.task.bean.UpLoadRecord
import java.io.File

/**
 * @author yyx
 */

object TransaReportTaskContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showData(data: TransactTaskBean.Data)
        fun onSuccess()
        fun onerror(data: String)
        fun showIamgeData(data: UpLoadRecord.Data)
        fun getFileParh(): File
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun saveEventTaskReport()
        fun uploadTaskRecord(file: File)
        fun resolveUri(mContext: Context, uri: Uri, options: BitmapFactory.Options)
        fun resolveUriForBitmap(
            mContext: Context,
            uri: Uri,
            options: BitmapFactory.Options
        ): Bitmap?

        fun decodeUri(mContext: Context, uri: Uri, maxWidth: Int, maxHeight: Int): Bitmap
        fun AddTimeWatermark(mBitmap: Bitmap): Bitmap
        fun compressImage(bitmap: Bitmap): File
    }
}
