package com.toker.sys.mvp.base;

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.alibaba.fastjson.JSON
import com.toker.reslib.sm.SM4Utils
import com.toker.reslib.sm4.RSAEncryptor
import com.toker.sys.AppApplication
import com.toker.sys.http.EasyHttp
import com.toker.sys.http.callback.ProgressDialogCallBack
import com.toker.sys.http.exception.ApiException
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import kotlin.collections.HashMap
import com.google.gson.Gson
import com.toker.sys.common.Constants
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.tools.Tool


/**
 * @author yyx
 */

abstract class BasePresenter<V : IBaseView> : IBasePresenter<V> {

//    val WEB_URL = "http://123.207.49.214:8082/hd/"//测试服务器
//    val WEB_URL = "https://cstuoke.evergrande.com/hd/"//业主测试服务器
//    val WEB_URL = "https://tuokeapp.evergrande.com/"//正式服务器
//    private val WEB_URL = "http://2652841pv0.zicp.vip:80/"//hd/"//测试花生壳
    private val tag: String = "BasePresenter"
    val WEB_URL = Constants.API_SERVER_URL


        //用随机生成sm4密钥
    private val sm4Key = SM4Utils.createSM4KeyInner()
    protected var mView: V? = null
    protected var compositeDisposable: CompositeDisposable? = null
    // ======== url请求列表 ========
    protected val URL_LIST = 0
    // ======== 网络请求加载模式 ========
    protected val LOAD_AUTO = 0 // 加载方式 （自�?0、顶部刷�?1、加载更�?2�?
    protected val LOAD_TOP = 1
    protected val LOAD_MORE = 2

    open fun addDisposable(disposable: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable!!.add(disposable)
    }

    override fun subscribe() {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
    }

    override fun unsubscribe() {
        if (compositeDisposable != null) {
            compositeDisposable!!.dispose()
        }
    }

    override fun attachView(view: V) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }


    protected val TAG = CommonUtils.getTag(this)
    protected var isShowError = true //cancel上次请求 默认true
    protected var isCancel = true //cancel上次请求 默认true
    //TODO url_type 0 get请求 其余 post请求
    protected var url_type: Int = URL_LIST
    protected var load_type: Int = LOAD_AUTO
    protected var bundle: Bundle? = null

    init {
        this.isCancel = isShowError
        this.isShowError = isCancel
    }

    fun reqData() {
        reqData(url_type, load_type)
    }

    fun reqData(url_type: Int) {
        reqData(url_type, load_type)
    }

    fun reqDataReTry() {
        reqData(url_type, load_type, bundle!!)
    }

    fun reqData(url_type: Int, load_type: Int, vararg bundle: Bundle) {
        val token = Tool.getValue(AppApplication.getInstance(), "do1-Token")
        LogUtils.d(TAG, "token:$token ")
        if (token != null) {
            AppApplication.TokenId = token
        }

        LogUtils.d(TAG, "AppApplication.TokenId:${AppApplication.TokenId} ");
        this.url_type = url_type
        this.load_type = load_type
        val bundle1 = if (bundle.isNotEmpty()) bundle[0] else null
        this.bundle = bundle1

        if (mView?.getContext() != null && !mView?.getContext()!!.isFinishing) {
            mView?.showLoadingUI(url_type, load_type)
        }
        var url = mView?.getUrl(url_type)
        var params: Map<String, String>? = mView?.getParams(url_type, load_type, bundle1)

        if (params == null) {
            params = HashMap()
        }
        var data = JSON.toJSONString(params)
        if (data.indexOf("\"[") != -1) {
            data = data.replace("\"[", "[")
        }
        if (data.indexOf("]\"") != -1) {
            data = data.replace("]\"", "]")
        }
        if (data.indexOf("\\") != -1) {
            data = data.replace("\\", "")
        }



        log("data:$data", tag)
        //用这个密钥给提交的数据加密
        val cipherText = SM4Utils.encryptData_ECB(sm4Key, data)
        log("data:$cipherText", tag)
        var encryptkey = RSAEncryptor.encryptWithBase64(sm4Key)
        val map = mutableMapOf<String, String>()
        map["data"] = cipherText
        map["sm4Key"] = encryptkey
        var urlKey = ""
        map.forEach {
            urlKey += "${it.key}=${it.value}&"
        }
        //TODO url_type 0 get请求 其余 post请求
        url =
            "$WEB_URL$url${if (url_type == 0) "?${urlKey.substring(0, urlKey.length - 1)}" else ""}"
        if (url_type == 0) {
            map.clear()
        }
        log("url:$url", tag)
        val gson = Gson()
        val result = gson.toJson(map)
        LogUtils.d(TAG, "result:$result ");
        EasyHttp.post(url)
            .upJson(result)
            .sign(true)
            .headers("do1-token", AppApplication.TokenId)
            .accessToken(true)
            .timeStamp(true)
            .execute(object : ProgressDialogCallBack<String>(mView?.showDialog(), true, true) {
                override fun onError(e: ApiException?) {
                    super.onError(e)
                    LogUtils.d(TAG, "e.message: ${e?.message}");
                    mView?.onError(url_type, load_type, e?.message!!)
                }

                override fun onSuccess(t: String) {
                    LogUtils.d(TAG, "onSuccess-->t:$t ");
                    val dataEcb = if (!t.contains("{")) {
                        SM4Utils.decryptData_ECB(sm4Key, t.replace("\"", ""))
                    } else {
                        t
                    }
                    log("dataEcb:$url_type---->$dataEcb", tag)
                    val jsonObject = JSONObject(dataEcb)
                    onSuccessData(jsonObject, url_type, load_type, bundle1)
                }
            })
    }




    protected abstract fun onSuccessData(
        jsonObject: JSONObject,
        url_type: Int,
        load_type: Int,
        bundle: Bundle?
    )


    protected fun showError(context: Context?, error: String) {
        if (isShowError && context != null) {
            toast(error)
        }
    }

    /**
     * 判断�?
     */
    fun isEmpty(list: Any): Boolean {
        return CommonUtils.isEmpty(list)
    }

    /**
     * toast
     */
    fun toast(text: CharSequence) {
        CommonUtils.toast(mView?.getContext()!!, text)
    }

    /**
     * log 输出
     */
    fun log(msg: String, vararg tags: String) {
        CommonUtils.log(msg, TAG)
    }

    private fun replaceUtils(data: String): String {
        var dataS = data
        if (data.contains("%")) {
            dataS = dataS.replace("%", "%25")
        }
        if (data.contains("+")) {
            dataS = dataS.replace("+", "%2B")
        }
        if (data.contains(" ")) {
            dataS = dataS.replace(" ", "%20")
        }
        if (data.contains("/")) {
            dataS = dataS.replace("/", "%2F")
        }
        if (data.contains("?")) {
            dataS = dataS.replace("?", "%3F")
        }
        if (data.contains("#")) {
            dataS = dataS.replace("#", "%23")
        }
        if (data.contains("&")) {
            dataS = dataS.replace("&", "%26")
        }
        return dataS
    }

    fun replaceUtilSreverse(data: String): String {
        var dataS = data
        if (data.contains("%25")) {
            dataS = dataS.replace("%25", "%")
        }
        if (data.contains("%2B")) {
            dataS = dataS.replace("%2B", "+")
        }
        if (data.contains("%20")) {
            dataS = dataS.replace("%20", " ")
        }
        if (data.contains("%2F")) {
            dataS = dataS.replace("%2F", "/")
        }
        if (data.contains("%3F")) {
            dataS = dataS.replace("%3F", "?")
        }
        if (data.contains("%23")) {
            dataS = dataS.replace("%23", "#")
        }
        if (data.contains("%26")) {
            dataS = dataS.replace("%26", "&")
        }
        return dataS
    }

}
