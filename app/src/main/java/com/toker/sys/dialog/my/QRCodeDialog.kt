package com.toker.sys.dialog.my

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.toker.sys.R
import com.toker.sys.dialog.adapter.NationProvinAdapter
import com.toker.sys.view.home.activity.custom.bean.CityBean
import org.greenrobot.eventbus.EventBus
import java.io.*
import android.content.Intent
import android.net.Uri
import com.toker.sys.view.home.activity.my.bean.RCodeEvent


/**
 * packageName: com.toker.sys.dialog.my
 * author: star
 * created on: 2019/12/3 8:56
 * description:
 */

class QRCodeDialog(
    private val mContext: Context, val bitmap: Bitmap
) : View.OnClickListener, NationProvinAdapter.NationProListener {
    private val Tag = "ProjectListDialog";
    var dialog: Dialog? = null

    override fun itemListener(bean: CityBean.Data) {
        EventBus.getDefault().post(bean)
        dialog!!.dismiss()
    }

    init {

        dialog = Dialog(mContext, R.style.DialogTheme)
        val view = View.inflate(mContext, R.layout.dialog_layout_qr_code, null)
        dialog!!.setContentView(view)
        val window = dialog!!.window
        //设置弹出位置
        window.setGravity(Gravity.CENTER)

        val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = wm.defaultDisplay.width
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val img = dialog!!.findViewById<ImageView>(R.id.img_qr_code)
        dialog?.findViewById<TextView>(R.id.tv_qr_code)!!.setOnClickListener(this)
        val params = img.layoutParams
        params.width = width
        params.height = width
        img.layoutParams = params
        img.setImageBitmap(bitmap)
        img.setOnClickListener(this)
        showDialog(dialog!!)
    }

    private fun showDialog(dialog: Dialog) {

        dialog.show()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_qr_code -> {
                //保存到本地
//                setPicToView(bitmap)
//                setPicToView(bitmap)
                EventBus.getDefault().post(RCodeEvent(0))
            }
            else -> {
                dialog!!.dismiss()
            }
        }

    }

    private val path = "/sdcard/myHead/"// sd路径
    private fun setPicToView(mBitmap: Bitmap) {
        val sdStatus = Environment.getExternalStorageState()+"/fingerprintimages/"
        if (sdStatus != Environment.MEDIA_MOUNTED) { // 检测sd是否可用
            return
        }
        var b: FileOutputStream? = null
        val file = File(path)
        file.mkdirs()// 创建文件夹
        val fileName = path + "${System.currentTimeMillis()}head.jpg"// 图片名字
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

        // 通知图库更新
        mContext.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
            Uri.parse("file://" + "/sdcard/namecard/")));
        Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show()
    }
    //保存的确切位置，根据自己的具体需要来修改

//    private val  SAVE_PIC_PATH = if (Environment.getExternalStorageState()== IgnoreCase(Environment.MEDIA_MOUNTED)) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";//保存到SD卡
    private val  SAVE_REAL_PATH = "/good/savePic";

    public fun saveFile( bm:Bitmap) {
        val  subForder = "/mnt/sdcard$SAVE_REAL_PATH";
        val  foder = File(subForder);
        if (!foder.exists()) {
            foder.mkdirs();
        }
        val myCaptureFile = File(subForder, "${System.currentTimeMillis()}head.jpg");
        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();
        }
        val bos = BufferedOutputStream(FileOutputStream (myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();

        val intent = Intent(Intent.ACTION_MEDIA_MOUNTED) //这是刷新SD卡
//    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);  // 这是刷新单个文件
        val uri = Uri.fromFile(File(SAVE_REAL_PATH))
        intent.data = uri
        mContext.sendBroadcast(intent)
    }
}
