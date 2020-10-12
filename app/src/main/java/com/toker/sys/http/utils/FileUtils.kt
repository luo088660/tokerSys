package com.toker.sys.http.utils

import android.content.Context
import com.toker.sys.AppApplication

import java.io.*

object FileUtils {

    @JvmStatic
    fun setData( list: Map<String, String>, tag: String) {
        val file = AppApplication.getInstance().cacheDir
        var Cache: File? = null
        val name: String = "file$tag"
        Cache = File(file, name)
        if (Cache.exists()) {
            Cache.delete()
        }
        try {
            val outputStream = ObjectOutputStream(FileOutputStream(Cache) as OutputStream?)
            outputStream.writeObject(list)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
    @JvmStatic
    fun getData(tag: String): Map<String, String>? {
        val file = AppApplication.getInstance().cacheDir
        val name: String = "file$tag"
        val cache: File
        var list: Map<String, String>? = null
        cache = File(file, name)
        if (!cache.exists()) {
            return null
        }
        try {
            val inputStream = ObjectInputStream(FileInputStream(cache))
            list = inputStream.readObject() as Map<String, String>
            return list
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null

    }

}
