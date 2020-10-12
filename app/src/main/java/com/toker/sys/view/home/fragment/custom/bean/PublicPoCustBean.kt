package com.toker.sys.view.home.fragment.custom.bean

data class PublicPoCustBean(
    val `data`: Data,
    val code: String,
    val desc: String
) {

    /**
     * 请求是否成功
     */
    fun isSuccess(): Boolean {
        return code == "1"
    }

    data class Data(
        val current: Int,
        val pages: Int,
        val records: MutableList<Record>,
        val searchCount: Boolean,
        val size: Int,
        val total: Int
    )

    data class Record(
        val id: String,
        val name: String
    ) {
        var  isfo: Boolean = false;
        constructor(
            id: String,
            name: String, isfo: Boolean
        ) : this(id, name){
            this.isfo = isfo
        }
    }
}
