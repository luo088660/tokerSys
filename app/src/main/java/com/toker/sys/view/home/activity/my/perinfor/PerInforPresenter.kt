package com.toker.sys.view.home.activity.my.perinfor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import com.toker.sys.AppApplication
import com.toker.sys.common.Constants
import com.toker.sys.http.EasyHttp
import com.toker.sys.http.callback.ProgressDialogCallBack
import com.toker.sys.http.exception.ApiException
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.Base64BitmapUtil
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.my.bean.PreInforImageBean
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.tools.URIUtil




/**
 * @author yyx
 */

class PerInforPresenter : BasePresenter<PerInforContract.View>(), PerInforContract.Presenter {


    private val PHOTO_REQUEST = 110
    private val RC_CHOOSE_PHOTO = 111
    private val CROP_PHOTO = 112

    private var mContext: Context? = null

    private val path = "/sdcard/myHead/"// sd路径

    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        val toJson = jsonObject.toString()
        when (url_type) {
            4 -> {
                val toBean =
                    GsonUtil.GsonToBean(toJson, PreInforImageBean.PreInImageBean::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.showDataIamge(toBean.data)
                }
            }
            else -> {
            }
        }

    }

    fun setContext(mContext: Context) {
        this.mContext = mContext
    }


    override fun loadRepositories() {
        reqData(1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        LogUtils.d(TAG, "requestCode: $requestCode");
        LogUtils.d(TAG, "resultCode: $resultCode");
        LogUtils.d(TAG, "data:${data?.data} ");
        when (requestCode) {
            PHOTO_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    LogUtils.d(TAG, "data:$data");

//                    val file = File(Environment.getExternalStorageDirectory(), "head.jpg")
                    val file =  mView?.getFileParh()!!
                    val uri = Uri.fromFile(file)
                    uploadTaskRecord(file)
//                    mView?.cropPhoto(uri)
                }

            }
            RC_CHOOSE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    val data1 = data!!.data!!
                    val file = URIUtil.uriToFile(data1, mContext)
                    uploadTaskRecord(file)
//                    mView?.cropPhoto(data!!.data)
                }
            }
            else -> {
                if (data != null) {
                    val extras = data.extras
                    //head = extras.getParcelable("data");
                    val uritempFile = Uri.fromFile(File(Environment.getExternalStorageDirectory(), "head.jpg"))
                    var bitmap: Bitmap? = null
                    try {
                        bitmap = BitmapFactory.decodeStream(mContext?.contentResolver?.openInputStream(uritempFile))
                        if (bitmap != null) {
                            /**
                             * 上传服务器代码
                             */
                            val imgBase64 = Base64BitmapUtil.bitmapToBase64(bitmap)
//                            UploadImage(imgBase64)
                            setPicToView(bitmap)// 保存在SD卡中
                            mView!!.getImg().setImageBitmap(bitmap)// 用ImageButton显示出来
                        }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }

                }
            }
        }

    }
    /**
     * 图片上传
     */
    override fun uploadTaskRecord(file:File) {
//        val url = "https://cstuoke.evergrande.com/hd/file/uploadUserPic"
        val url = "${Constants.API_SERVER_URL}file/uploadUserPic"
        EasyHttp.post(url)
            .params(
                "file", file
            ) { bytesWritten, contentLength, done ->
                LogUtils.d(TAG, "done:$done ")
            }
            .sign(true)
            .headers("do1-Token", AppApplication.TokenId)
            .accessToken(true)
            .timeStamp(true)
            .execute(object : ProgressDialogCallBack<String>(mView?.showDialog(), true, true) {
                override fun onError(e: ApiException?) {
                    super.onError(e)
                    LogUtils.d(TAG, "e.message: ${e?.message}")
                }
                override fun onSuccess(t: String) {
                    LogUtils.d(TAG, "onSuccess-->t:$t ")
                    LogUtils.d("dataEcb:---->")
                    val jsonObject = JSONObject(t)
                    onSuccessData(jsonObject,4,1,Bundle())
                }
            })
    }
    private fun setPicToView(mBitmap: Bitmap) {
        val sdStatus = Environment.getExternalStorageState()
        if (sdStatus != Environment.MEDIA_MOUNTED) { // 检测sd是否可用
            return
        }
        var b: FileOutputStream? = null
        val file = File(path)
        file.mkdirs()// 创建文件夹
        val fileName = path + "head.jpg"// 图片名字
        try {
            b = FileOutputStream(fileName)
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b)// 把数据写入文件
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            try {
                // 关闭流
                b!!.flush()
                b.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
}