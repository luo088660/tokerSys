package com.toker.sys.view.home.activity.task.transareporttask

import android.content.ContentResolver
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.toker.sys.AppApplication
import com.toker.sys.common.Constants
import com.toker.sys.http.EasyHttp
import com.toker.sys.http.callback.ProgressDialogCallBack
import com.toker.sys.http.exception.ApiException
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.task.bean.TransaRTBean
import com.toker.sys.view.home.activity.task.bean.TransactTaskBean
import com.toker.sys.view.home.activity.task.bean.UpLoadRecord
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author yyx
 */

class TransaReportTaskPresenter : BasePresenter<TransaReportTaskContract.View>(),
    TransaReportTaskContract.Presenter {
    override fun onSuccessData(
        jsonObject: JSONObject,
        url_type: Int,
        load_type: Int,
        bundle: Bundle?
    ) {
        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, TransactTaskBean.TransactBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showData(toBean.data)
                }
            }
            2 -> {
                val toBean = GsonUtil.GsonToBean(toJson, TransaRTBean.Bean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.onSuccess()
                } else {
                    mView?.onerror(toBean.desc)
                }
            }

            4->{
                //图片
                val toBean  = GsonUtil.GsonToBean(toJson, UpLoadRecord.UpRecord::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.showIamgeData(toBean.data)
                }

            }
            else -> {
            }
        }
    }

    override fun loadRepositories() {
        reqData(1)
    }

    override fun saveEventTaskReport() {
        reqData(2)
    }

    /**
     * 图片上传
     */
    override fun uploadTaskRecord(file:File) {
//        val url = "https://cstuoke.evergrande.com/hd/file/uploadTaskRecord"
        val url = "${Constants.API_SERVER_URL}file/uploadTaskRecord"
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
                    LogUtils.d(TAG,"dataEcb:---->")
                    val jsonObject = JSONObject(t)
                    onSuccessData(jsonObject,4,1,Bundle())
                }
            })
    }



    /**
     * 读取一个缩放后的图片，限定图片大小，避免OOM
     * http://blog.sina.com.cn/s/blog_5de73d0b0100zfm8.html
     * @param uri       图片uri，支持“file://”、“content://”
     * @param maxWidth  最大允许宽度
     * @param maxHeight 最大允许高度
     * @return  返回一个缩放后的Bitmap，失败则返回null
     */
    override fun decodeUri(mContext: Context,uri: Uri, maxWidth: Int, maxHeight: Int): Bitmap {
        val options = BitmapFactory.Options();
        options.inJustDecodeBounds = true; //只读取图片尺寸
        resolveUri(mContext,uri, options);

        //计算实际缩放比例
        var scale = 1;
        for (i in 0..Integer.MAX_VALUE) {
            if ((options.outWidth / scale > maxWidth &&
                        options.outWidth / scale > maxWidth * 1.4) ||
                (options.outHeight / scale > maxHeight &&
                        options.outHeight / scale > maxHeight * 1.4)
            ) {
                scale++;
            } else {
                break;
            }
        }

        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;//读取图片内容
        options.inPreferredConfig = Bitmap.Config.RGB_565; //根据情况进行修改
        var bitmap: Bitmap? = null;
        try {
            bitmap = resolveUriForBitmap(mContext,uri, options);
        } catch (e: Throwable) {
            e.printStackTrace();
        }
        return bitmap!!;
    }

    override fun resolveUri(mContext: Context, uri: Uri, options: BitmapFactory.Options) {
        if (uri == null) {
            return;
        }

        var scheme = uri.scheme;
        if (ContentResolver.SCHEME_CONTENT == scheme ||
            ContentResolver.SCHEME_FILE == scheme
        ) {
            var stream: InputStream? = null;
            try {
                stream = mContext.contentResolver.openInputStream(uri);
                BitmapFactory.decodeStream(stream, null, options);
            } catch (e: Exception) {
                LogUtils.w("resolveUri", "Unable to open content: $uri");
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (e: IOException) {
                        LogUtils.w("resolveUri", "Unable to close content: $uri");
                    }
                }
            }
        } else if (ContentResolver.SCHEME_ANDROID_RESOURCE == scheme) {
            LogUtils.w("resolveUri", "Unable to close content: $uri");
        } else {
            LogUtils.w("resolveUri", "Unable to close content: $uri");
        }
    }

    override fun resolveUriForBitmap(mContext: Context,uri: Uri, options: BitmapFactory.Options): Bitmap? {
        if (uri == null) {
            return null;
        }

        var bitmap: Bitmap? = null;
        var scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme) ||
            ContentResolver.SCHEME_FILE.equals(scheme)
        ) {
            var stream: InputStream? = null;
            try {
                stream = mContext.getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(stream, null, options);
            } catch (e: Exception) {
                LogUtils.w("resolveUriForBitmap", "Unable to open content: $uri");
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (e: IOException) {
                        LogUtils.w("resolveUriForBitmap", "Unable to close content: $uri");
                    }
                }
            }
        } else if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
            LogUtils.w("resolveUriForBitmap", "Unable to close content: $uri");
        } else {
            LogUtils.w("resolveUriForBitmap", "Unable to close content: $uri");
        }

        return bitmap;
    }

    /**
     * 添加时间水印
     * @param mBitmap
     * @return mNewBitmap
     */
    override fun AddTimeWatermark(mBitmap: Bitmap): Bitmap {
        //获取原始图片与水印图片的宽与高
        val mBitmapWidth = mBitmap.width;
        val mBitmapHeight = mBitmap.height;
        val mNewBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Bitmap.Config.ARGB_8888);
        var mCanvas = Canvas(mNewBitmap);
        //向位图中开始画入MBitmap原始图片
        mCanvas.drawBitmap(mBitmap, 0f, 0f, null);
        //添加文字
        val mPaint = Paint();
        val mFormat = SimpleDateFormat("yyyy-MM-dd hh:mm").format(Date());
        mPaint.color = Color.RED;
        mPaint.textSize = 20f;
        //水印的位置坐标
        LogUtils.d(TAG, "mBitmapWidth:$mBitmapWidth ");
        LogUtils.d(TAG, "mBitmapWidth:${(mBitmapWidth * 14) / 20f} ");
        mCanvas.drawText(mFormat, (mBitmapWidth * 14) / 24f, (mBitmapHeight * 14) / 15f, mPaint);
        mCanvas.save();
        mCanvas.restore();
        return mNewBitmap;
    }

    /**
     * 压缩图片（质量压缩）
     * @param bitmap
     */
   override fun compressImage(bitmap: Bitmap): File {
        var baos = ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 100;
        while (baos.toByteArray().size / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                options,
                baos
            );//这里压缩options%，把压缩后的数据存放到baos中
            val length = baos.toByteArray().size;
        }
        var file = mView?.getFileParh()!!
        try {
            val fos = FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (e: IOException) {
                e.printStackTrace();
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace();
        }
        recycleBitmap(bitmap);
        return file;
    }

    fun recycleBitmap(bitmaps: Bitmap) {
        if (bitmaps == null) {
            return;
        }
        if (null != bitmaps && !bitmaps.isRecycled) {
            bitmaps.recycle();
        }
    }


}