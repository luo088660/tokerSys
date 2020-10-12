package com.toker.sys.view.home.activity.my.perinfor

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.api.TagAliasCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.dialog.my.CustomDialog
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.SystemSettImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.tools.PhoneUtils
import com.toker.sys.utils.tools.Tool
import com.toker.sys.view.home.activity.main.event.MainEvent
import com.toker.sys.view.home.activity.my.bean.PreInforImageBean
import com.toker.sys.view.home.activity.my.perinfor.event.PerInforEvent
import com.toker.sys.view.home.activity.my.pwdmanage.PWdManageActivity
import com.toker.sys.view.home.activity.my.qrcode.QRCodeActivity
import com.toker.sys.view.register.login.LoginActivity
import kotlinx.android.synthetic.main.activity_per_infor.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File


/**
 * 个人信息
 * @author yyx
 */

class PerInforActivity : BaseActivity<PerInforContract.View, PerInforPresenter>(),
    PerInforContract.View {

    override var mPresenter: PerInforPresenter = PerInforPresenter()


    override fun layoutResID(): Int = R.layout.activity_per_infor
    private val PHOTO_REQUEST = 110
    private val RC_CHOOSE_PHOTO = 111
    private val CROP_PHOTO = 112

    override fun initView() {
        val pjtName = intent.getStringExtra("name")
        mPresenter.setContext(this)
        EventBus.getDefault().register(this)
        tv_title.text = "个人信息"
        tv_infor_03.text = pjtName
        requestPermission()

    }

    override fun initData() {
        if (AppApplication.TYPE == Constants.RESCUE1 || AppApplication.TYPE == Constants.RESCUE2) {
            ll_info_code.visibility = GONE
        }

        if (AppApplication.TYPE == Constants.RESCUE1){
            ll_infor_project.visibility = GONE
        }
        setOnClickListener(img_back)
        setOnClickListener(rl_info_img)
        setOnClickListener(ll_info_code)
        setOnClickListener(ll_info_pwd)
        setOnClickListener(btn_info_quit)


        tv_infor_01.text = AppApplication.USERNAME
        tv_infor_02.text = PhoneUtils(this).phoneRepla(AppApplication.PHONE)
        if (!TextUtils.isEmpty(AppApplication.ICON)) {

            Glide.with(this).load(AppApplication.ICON)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(25)))//圆角半径
                .into(cv_title)
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            //更换头像
            rl_info_img -> {
                CustomDialog(this)

            }
            //我的二维码QRCodeActivity
            ll_info_code -> {
                val intent = Intent(this, QRCodeActivity::class.java)
                intent.putExtra("projectName", "${tv_infor_03.text}")
                startActivity(intent)
            }
            //密码管理PWdManage
            ll_info_pwd -> startActivity(Intent(this, PWdManageActivity::class.java))
            else -> {
                EventBus.getDefault().post(MainEvent(99))
                startActivity(Intent(this, LoginActivity::class.java))
                //清除头像
                AppApplication.ICON = ""
//                Tool.remove(this, Constants.THATTIME)
                //设置别名
                setAlias()
                finish()
            }
        }
    }

    override fun getImg(): ImageView {
        return cv_title
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun PerInforEvent(event: PerInforEvent) {
        log("event.type-->${event.type}", TAG)
        when (event.type) {
            //拍照
            0 -> {

                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    // 权限还没有授予，进行申请
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), 300
                    ); // 申请的 requestCode 为 300
                } else {
                    // 权限已经申请，直接拍照
                    takePhoto()
                }

            }
            //从相机选择
            else -> {
                choosePhoto()
            }
        }

    }

    private var url = ""
    override fun showDataIamge(data: PreInforImageBean.Data) {
        url = data.Url
        AppApplication.ICON = data.Url
        Glide.with(this).load(AppApplication.ICON)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(25)))//圆角半径
            .into(cv_title)
        /*Glide.with(getContext()).load(url).asBitmap().centerCrop().into(
            object : BitmapImageViewTarget(cv_title) {
                override fun setResource(resource: Bitmap) {
                    val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(
                        this@PerInforActivity.resources,
                        resource
                    );
                    circularBitmapDrawable.isCircular = true;
                    cv_title.setImageDrawable(circularBitmapDrawable);
                }
            })*/
        mPresenter.loadRepositories()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mPresenter.onActivityResult(requestCode, resultCode, data)
    }

    var filePath:File?  = null
    override fun getFileParh(): File {

        return filePath!!
    }
    /**
     * 拍照
     */
    private fun takePhoto() {

        var pictureFile = File(Environment.getExternalStorageDirectory(), "${System.currentTimeMillis()}head.jpg")
        filePath = pictureFile
        //也就是我之前创建的存放头像的文件夹（目录）
        var intent: Intent? = null
        var pictureUri: Uri? = null

        // 判断当前系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //这一句非常重要
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //""中的内容是随意的，但最好用package名.provider名的形式，清晰明了
            pictureUri = FileProvider.getUriForFile(
                this,
                if (Constants.SERVER_DEBUG)"com.toker.systest.fileprovider" else "com.toker.sys.fileprovider", pictureFile
            )

        } else {
            intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            pictureUri = Uri.fromFile(pictureFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri)
        LogUtils.e(TAG, "before take photo$pictureUri");
        startActivityForResult(intent, PHOTO_REQUEST);

    }

    /**
     * 系统相册获取图片
     */
    private fun choosePhoto() {
        val intentToPickPic = Intent(Intent.ACTION_PICK, null)
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO)
    }

    /**
     * 添加权限
     */
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                //扫码
                Manifest.permission.CAMERA
            ), 0
        )
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {

        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
                map["url"] = url
            }
            else -> {
            }
        }
        return map
    }

    override fun getUrl(url_type: Int): String {

        return when (url_type) {
            1 -> {
                SystemSettImp.API_SYSTEM_UPDATE_HEADERPIC
            }
            else -> {
                ""
            }
        }
    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private fun setAlias() {

        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, "1231231"))
    }

    private var mAliasCallback: TagAliasCallback = TagAliasCallback { code, alias, tags ->
        val logs: String
        when (code) {
            0 -> {
                logs = "Set tag and alias success"
                LogUtils.i(TAG, logs)
            }
            6002 -> {
                logs = "Failed to set alias and tags due to timeout. Try again after 60s."
                LogUtils.i(TAG, logs)
                // 延迟 60 秒来调用 Handler 设置别名
                mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias),(1000 * 60).toLong())
            }
            else -> {
                logs = "Failed with errorCode = $code"
                LogUtils.e(TAG, logs)
            }
        }// 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
//        ExampleUtil.showToast(logs, this)
    }
    private val MSG_SET_ALIAS = 1001
    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_SET_ALIAS -> {
                    LogUtils.d(TAG, "Set alias in handler.")
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(this@PerInforActivity, msg.obj as String, null, this@PerInforActivity.mAliasCallback)
                }
                else -> LogUtils.i(TAG, "Unhandled msg - " + msg.what)
            }
        }
    }

    /**
     * 图片裁剪
     */
    override fun cropPhoto(data: Uri) {

        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(data, "image/*")
        intent.putExtra("crop", "true")
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250)
        intent.putExtra("outputY", 250)
        intent.putExtra("return-data", true)
        val uritempFile = Uri.fromFile(File(Environment.getExternalStorageDirectory(), "head.jpg"))
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        startActivityForResult(intent, CROP_PHOTO)
    }

}
