package com.toker.sys.view.home.activity.task.transareporttask

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.TaskManageImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.task.adapter.TransaReportTaskAdapter
import com.toker.sys.view.home.activity.task.bean.TransactTaskBean
import com.toker.sys.view.home.activity.task.bean.UpLoadRecord
import com.toker.sys.view.home.activity.task.event.TransaReportTaskEvent
import com.toker.sys.view.home.activity.task.photoview.PhotoViewActivity
import com.toker.sys.view.home.fragment.event.IamgeBitmap
import kotlinx.android.synthetic.main.activity_transa_report_task.*
import kotlinx.android.synthetic.main.layout_admini_tran_detail_01.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author yyx
 */

class TransaReportTaskActivity :
    BaseActivity<TransaReportTaskContract.View, TransaReportTaskPresenter>(),
    TransaReportTaskContract.View {

    var imgLists: MutableList<String> = mutableListOf()
    override var mPresenter: TransaReportTaskPresenter = TransaReportTaskPresenter()
    var id: String = ""
    var tableTag: String = ""
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    var adapter: TransaReportTaskAdapter? = null
    val REQUEST_IMAGE_CAPTURE = 1
    override fun layoutResID(): Int = R.layout.activity_transa_report_task

    override fun initView() {
        EventBus.getDefault().register(this)
        id = intent.getStringExtra("id")
        tableTag = intent.getStringExtra("tableTag")
        rv_img_transa.layoutManager = GridLayoutManager(this, 4)
        mPresenter.loadRepositories()
    }

    override fun initData() {
        setOnClickListener(img_back)
        setOnClickListener(img_transa_report)
        setOnClickListener(tv_fill_task_report)

        adapter = TransaReportTaskAdapter(this, imgLists)
        rv_img_transa.adapter = adapter
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> {
                finish()
            }
            tv_fill_task_report -> {
                if (imgLists.isNotEmpty()) {
                    if (!"${et_fill_report.text}".isNullOrEmpty()) {
                        mPresenter.saveEventTaskReport()
                    } else {
                        toast("请输入完成情况")
                    }
                } else {
                    toast("请添加附件")
                }
            }
            //拍照
            img_transa_report -> {
                if (ContextCompat.checkSelfPermission(
                        this@TransaReportTaskActivity,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(
                        this@TransaReportTaskActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    // 权限还没有授予，进行申请
                    ActivityCompat.requestPermissions(
                        this@TransaReportTaskActivity,
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), 300
                    ); // 申请的 requestCode 为 300
                } else {
                    // 权限已经申请，直接拍照
                    imageCapture();
                }
            }
            else -> {
            }
        }
    }


    //数据展示
    override fun showData(data: TransactTaskBean.Data) {

        tv_title.text = data.taskName
        tv_admini_tran_01.text = data.projectName
        tv_admini_tran_02.text =
            "${sdf.format(Date(data.startDate))}至${sdf.format(Date(data.endDate))}\t${data.startTime}~${data.endTime}"
        tv_admini_tran_03.text = data.address
        tv_admini_tran_04.text = data.content
        tv_admini_tran_05.text = data.objectList.joinToString { it.objectName }
        tv_admini_tran_06.text = when (data.status.toInt()) {
            1 -> {
                tv_admini_tran_06.setTextColor(resources.getColor(R.color.c_txt_weikaishi))
                tv_admini_tran_06.setBackgroundColor(resources.getColor(R.color.c_bg_weikaishi))
                "未开始"
            }
            2 -> {
                tv_admini_tran_06.setTextColor(resources.getColor(R.color.c_txt_yirenchou))
                tv_admini_tran_06.setBackgroundColor(resources.getColor(R.color.c_bg_yirenchou))
                "进行中"
            }
            3 -> {
                tv_admini_tran_06.setTextColor(resources.getColor(R.color.c_txt_tjchenggong))
                tv_admini_tran_06.setBackgroundColor(resources.getColor(R.color.c_bg_tjchenggong))
                "已完成"
            }
            else -> {
                "草稿中"
            }
        }

    }

    override fun onSuccess() {
        toast("提交成功")
        finish()
    }

    override fun onerror(data: String) {
        toast(data)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun bitmapDrawable(event: IamgeBitmap){
        val intent = Intent(this, PhotoViewActivity::class.java)
        intent.putExtra("bmpPath",event.bmpPath)
        startActivity(intent)
    }
    //上传图片数据
    override fun showIamgeData(data: UpLoadRecord.Data) {
        imgLists.add(data.Url)
        LogUtils.d(TAG, "da: $data");

        rv_img_transa.layoutManager = GridLayoutManager(this, if (imgLists.size < 5) 4 else 5)
        img_transa_report.visibility = if (imgLists.size < 5) VISIBLE else GONE
        adapter?.reFreshData(imgLists)
    }

    /**
     * 删除图片
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun transaReportTaskEvent(event: TransaReportTaskEvent) {
        imgLists.removeAt(event.position)
        rv_img_transa.layoutManager = GridLayoutManager(this, if (imgLists.size < 5) 4 else 5)
        img_transa_report.visibility = if (imgLists.size < 5) VISIBLE else GONE
        adapter?.reFreshData(imgLists)
        val joinToString = imgLists.joinToString { it }
        val joinToString1 = joinToString.replace(", ", ",")
        LogUtils.e(TAG, "joinToString:$joinToString1 ");
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> {
                TaskManageImp.API_TASK_EVENTTASK
            }
            2 -> {
                TaskManageImp.API_TASK_SAVE_EVENTTASK_REPORT
            }
            3 -> {
                TaskManageImp.API_TASK_SAVE_UPLOAD_TASK_RECORD
            }
            else -> {
                ""
            }
        }


    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
                map["taskId"] = id
                map["tableTag"] = tableTag
            }
            2 -> {
                map["taskId"] = id
                map["tableTag"] = tableTag
                map["content"] = "${et_fill_report.text}"
                map["filePath"] = imgLists.joinToString { it }.replace(", ", ",")
            }
        }

        return map
    }


    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    var filePath: File? = null
    override fun getFileParh(): File {

        return filePath!!
    }

    private fun imageCapture() {
        //也就是我之前创建的存放头像的文件夹（目录）
        var intent: Intent? = null
        var pictureUri: Uri? = null
//        var pictureFile = File(Environment.getExternalStorageDirectory(), "${imgLists.size}head.jpg")
        var pictureFile =
            File(Environment.getExternalStorageDirectory(), "${System.currentTimeMillis()}head.jpg")
        filePath = pictureFile
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
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        LogUtils.d(TAG, "requestCode:$requestCode ");
        LogUtils.d(TAG, "resultCode:$resultCode ");
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val file = getFileParh()
                    val uri = Uri.fromFile(file)

                    val decodeUri = mPresenter.decodeUri(this,uri, 500, 500)
                    val addTimeWatermark = mPresenter.AddTimeWatermark(decodeUri)
                    val compressImage = mPresenter.compressImage(addTimeWatermark)

                    mPresenter.uploadTaskRecord(compressImage)
                }
                else -> {
                }
            }
        }
    }





    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
