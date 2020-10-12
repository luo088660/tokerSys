package com.toker.sys.view.home.activity.my.qrcode

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.View
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import com.uuzuche.lib_zxing.activity.CodeUtils
import kotlinx.android.synthetic.main.activity_q_r_code.*
import kotlinx.android.synthetic.main.layout_content_title.*
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.toker.sys.AppApplication
import com.toker.sys.common.Constants
import com.toker.sys.dialog.my.QRCodeDialog
import com.toker.sys.utils.tools.PhoneUtils
import com.toker.sys.view.home.activity.my.bean.RCodeEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


/**
 * 我的二维码
 * @author yyx
 */

class QRCodeActivity : BaseActivity<QRCodeContract.View, QRCodePresenter>(), QRCodeContract.View {

    override var mPresenter: QRCodePresenter = QRCodePresenter()


    override fun layoutResID(): Int = R.layout.activity_q_r_code

    override fun initView() {
        tv_title.text = "我的二维码"
    }
    var image:Bitmap?= null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initData() {
        val projectName = intent.getStringExtra("projectName")

        tv_my_code_01.text = when (AppApplication.TYPE) {
            Constants.RESCUE1 -> "${AppApplication.USERNAME}\t拓客中层"
            Constants.RESCUE2 -> "${AppApplication.USERNAME}\t拓客管理员"
            Constants.RESCUE3 -> "${AppApplication.USERNAME}\t拓客员"
            else -> {
                "${AppApplication.USERNAME}\t拓客组长"
            }
        }
        tv_my_code_02.text = PhoneUtils(this).phoneRepla(AppApplication.PHONE)
        tv_my_code_03.text = projectName
        setOnClickListener(img_back)
        setOnClickListener(img_r_code)
       val bitmap =
            drawableToBitmap(resources.getDrawable(R.mipmap.icon_retina_hd_spotlight_, null))

        val map = mutableMapOf<String, String>()
        map["id"] = AppApplication.USERID
        map["name"] = AppApplication.USERNAME
        map["position"] = when (AppApplication.TYPE) {
            Constants.RESCUE1 -> "拓客中层"
            Constants.RESCUE2 -> "拓客管理员"
            Constants.RESCUE3 -> "拓客员"
            else -> "拓客组长"
        }
        map["icon"] = if (!TextUtils.isEmpty(AppApplication.ICON)){
            AppApplication.ICON
        }else{
            "https://appicon.pgyer.com/image/view/app_icons/cf96774466b9fa570296f50d5148a8c9/120"
        }
        val toJson = Gson().toJson(map)
        image = CodeUtils.createImage(toJson, 250, 250, bitmap)
        img_r_code.setImageBitmap(image)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            img_r_code -> QRCodeDialog(this,image!!)

            else -> {
            }
        }
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    private fun drawableToBitmap(drawable: Drawable) // drawable 转换成bitmap
            : Bitmap {
        val width = drawable.intrinsicWidth// 取drawable的长宽
        val height = drawable.intrinsicHeight
        val config =
            if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565// 取drawable的颜色格式
        val bitmap = Bitmap.createBitmap(width, height, config)// 建立对应bitmap
        val canvas = Canvas(bitmap)// 建立对应bitmap的画布
        drawable.setBounds(0, 0, width, height)
        drawable.draw(canvas)// 把drawable内容画到画布中
        return bitmap
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun RCodeEvent(x: RCodeEvent){
        saveToSystemGallery(this,image!!)
    }
    /**
     * 保存到系统相册
     *
     * @param context
     * @param bmp
     */

    public fun saveToSystemGallery(context: Context, bmp:Bitmap ) {
        // 首先保存图片
        val appDir = File(Environment.getExternalStorageDirectory(), "vgmap");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        val fileName ="${ System.currentTimeMillis()}.jpg";
        val file = File(appDir, fileName);
        try {
            val fos = FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch ( e: FileNotFoundException) {
            e.printStackTrace();
        } catch ( e: IOException) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                file.getAbsolutePath(), fileName, null);
        } catch ( e:FileNotFoundException) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));
    }

}
