package com.toker.sys.view.home.activity.task.photoview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.view.image.OnMatrixChangedListener
import com.toker.sys.utils.view.image.OnPhotoTapListener
import com.toker.sys.utils.view.image.OnSingleFlingListener
import kotlinx.android.synthetic.main.activity_photo_view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


/**
 * 查看图片
 * @author yyx
 */

class PhotoViewActivity : BaseActivity<PhotoViewContract.View, PhotoViewPresenter>(),
    PhotoViewContract.View {

    override var mPresenter: PhotoViewPresenter = PhotoViewPresenter()


    override fun layoutResID(): Int = R.layout.activity_photo_view

    override fun initView() {
        EventBus.getDefault().register(this)
        val bmpPath = intent.getStringExtra("bmpPath")
        LogUtils.d(TAG, "bmpPath:$bmpPath ");
        val mCurrentDisplayMatrix = Matrix()
        photo_view.setDisplayMatrix(mCurrentDisplayMatrix)
        photo_view.setOnMatrixChangeListener(MatrixChangeListener())
        photo_view.setOnPhotoTapListener(PhotoTapListener())
        photo_view.setOnSingleFlingListener(SingleFlingListener())
        EventBus.getDefault().post(bmpPath)
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun showImage(imgPath: String) {
        getBitmap(imgPath)
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

    private inner class MatrixChangeListener : OnMatrixChangedListener {

        override fun onMatrixChanged(rect: RectF) {

        }
    }

    private inner class PhotoTapListener : OnPhotoTapListener {

        override fun onPhotoTap(view: ImageView, x: Float, y: Float) {
            val xPercentage = x * 100f
            val yPercentage = y * 100f
        }
    }

    private inner class SingleFlingListener : OnSingleFlingListener {

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            return true
        }
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    /**
     * 根据传入url获取到它的图片
     */
    private fun getBitmap(imgUrl: String) {

        Thread(Runnable {
            var connection: HttpURLConnection? = null
            try {
                val bitmapUrl = URL(imgUrl)
                connection = bitmapUrl.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                //通过返回码判断网络是否请求成功
                if (connection.responseCode == 200) {
                    val inputStream = connection.inputStream
                    val shareBitmap = BitmapFactory.decodeStream(inputStream)
                    val message = wxHandler.obtainMessage()
                    message.what = 0
                    message.obj = shareBitmap
                    wxHandler.sendMessage(message)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }).start()
    }

    @SuppressLint("HandlerLeak")
    private val wxHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    val wxShareBitmap1 = msg.obj as Bitmap
                    photo_view.setImageBitmap(wxShareBitmap1)
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
