package com.toker.sys.utils.network.bean

/**(data: String, code: String, desc: String): BaseArrayBean(data, code, desc){
 * 登录
 */
data class LoginBean(
    val `data`: Data,
    val code: String,
    val desc: String
){
    /**
     * 请求是否成功
     */
    fun isSuccess(): Boolean {
        return code == "1"
    }
}

data class Data(
    val phone: String,
    val userId: String,
    val position: String,
    val icon: String,
    val company:String,
    val username: String
)
