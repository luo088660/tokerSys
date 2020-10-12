package com.toker.sys.mvp.base

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.toker.sys.http.subsciber.IProgressDialog

/**
 * @author yyx
 */

interface IBaseView {

    fun getContext(): FragmentActivity?

    fun getUrl(url_type: Int): String

    fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String>

    fun onError(url_type: Int, load_type: Int, error: String)

    fun showLoadingUI(url_type: Int, load_type: Int)

    fun hideLoadingUI(url_type: Int, load_type: Int, success: Boolean)

    fun showDialog(): IProgressDialog
}
