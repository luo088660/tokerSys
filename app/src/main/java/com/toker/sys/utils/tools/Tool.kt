package com.toker.sys.utils.tools

import android.content.Context

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken

import java.util.ArrayList
object Tool {

    @JvmStatic
    fun commit(mContext: Context, key: String, value: String) {
        val sp = mContext.getSharedPreferences("Meitu", Context.MODE_MULTI_PROCESS)
        val edit = sp.edit()
        edit.putString(key, value)
//        edit.commit()
        edit.apply()
    }


    fun commit(mContext: Context, key: String, value: Boolean) {
        val sp = mContext.getSharedPreferences("Meitu", 0)
        val edit = sp.edit()
        edit.putBoolean(key, value)
        edit.commit()
    }

    fun commit(mContext: Context, key: String, value: Int) {
        val sp = mContext.getSharedPreferences("Meitu", 0)
        val edit = sp.edit()
        edit.putInt(key, value)
        edit.commit()
    }

    fun getValue(mContext: Context, key: String): String? {
        val value: String?
        val sp = mContext.getSharedPreferences("Meitu", 0)
        value = sp.getString(key, "")
        return value
    }

    fun getBooleanValue(mContext: Context, key: String): Boolean {
        val value: Boolean
        val sp = mContext.getSharedPreferences("Meitu", 0)
        value = sp.getBoolean(key, true)
        return value
    }

    fun getintValue(mContext: Context, key: String): Int {
        val value: Int
        val sp = mContext.getSharedPreferences("Meitu", 0)
        value = sp.getInt(key, 0)
        return value
    }


    fun remove(mContext: Context, key: String) {
        //
        val sp = mContext.getSharedPreferences("Meitu", 0)
        val edit = sp.edit()
        edit.remove(key)
        edit.commit()
    }


    /**
     * 用于保存集合
     *
     * @param key  key
     * @param list 集合数据
     * @return 保存结果
     */
  /*  fun <T> putListData(mContext: Context, key: String, list: List<T:Class>): Boolean {
        var result: Boolean
        val type = list[0]!!.javaClass.simpleName
        val sp = mContext.getSharedPreferences("Meitu", 0)
        val editor = sp.edit()
        val array = JsonArray()
        try {
            when (type) {
                "Boolean" -> for (i in list.indices) {
                    array.add(list[i] as Boolean)
                }
                "Long" -> for (i in list.indices) {
                    array.add(list[i] as Long)
                }
                "Float" -> for (i in list.indices) {
                    array.add(list[i] as Float)
                }
                "String" -> for (i in list.indices) {
                    array.add(list[i] as String)
                }
                "Integer" -> for (i in list.indices) {
                    array.add(list[i] as Int)
                }
                else -> {
                    val gson = Gson()
                    for (i in list.indices) {
                        val obj = gson.toJsonTree(list[i])
                        array.add(obj)
                    }
                }
            }
            editor.putString(key, array.toString())
            result = true
        } catch (e: Exception) {
            result = false
            e.printStackTrace()
        }

        editor.apply()
        return result
    }
*/
    /**
     * 获取保存的List
     *
     * @param key key
     * @return 对应的Lis集合
     */
    fun <T> getListData(mContext: Context, key: String, cls: Class<T>): List<T> {
        val list = ArrayList<T>()
        val sp = mContext.getSharedPreferences("Meitu", 0)
        val json = sp.getString(key, "")
        if (json != "" && json!!.length > 0) {
            val gson = Gson()
            val array = JsonParser().parse(json).asJsonArray
            for (elem in array) {
                list.add(gson.fromJson(elem, cls))
            }
        }
        return list
    }


    /**
     * 保存List
     *
     * @param tag
     * @param datalist
     */
    fun setDataList(mContext: Context, tag: String, datalist: Array<String>?) {
        if (null == datalist || datalist.size <= 0)
            return
        val sp = mContext.getSharedPreferences("Meitu", 0)
        val editor = sp.edit()
        val gson = Gson()
        //转换成json数据，再保存
        val strJson = gson.toJson(datalist)
        editor.clear()
        editor.putString(tag, strJson)
        editor.commit()

    }

    /**
     * 获取List
     *
     * @param tag
     * @return
     */
    fun getDataList(mContext: Context, tag: String): Array<String>? {

        var arrays: Array<String>? = null
        val sp = mContext.getSharedPreferences("Meitu", 0)
        val strJson = sp.getString(tag, null) ?: return arrays
        val gson = Gson()
        arrays = gson.fromJson<Array<String>>(strJson, object : TypeToken<Array<String>>() {

        }.type)
        return arrays

    }

    fun removeList(mContext: Context, key: String) {
        val sp = mContext.getSharedPreferences("Meitu", 0)
        val edit = sp.edit()
        edit.remove(key)
        edit.commit()
    }

    /**
     * 用于保存集合
     *
     * @param key key
     * @param map map数据
     * @return 保存结果
     */
    fun <K, V> putHashMapData(mContext: Context, key: String, map: Map<K, V>): Boolean {
        var result: Boolean
        val sp = mContext.getSharedPreferences("Meitu", 0)
        val editor = sp.edit()
        try {
            val gson = Gson()
            val json = gson.toJson(map)
            editor.putString(key, json)
            result = true
        } catch (e: Exception) {
            result = false
            e.printStackTrace()
        }

        editor.apply()
        return result
    }


}