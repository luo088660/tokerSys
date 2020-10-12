package com.toker.sys.view.home.activity.my.bean

class STTeamRankBean {
    data class STTeamRBean(
        val `data`: Data,
        val code: String,
        val desc: String
    ){
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }


    data class Data(
        val page: String,
        val pageData: MutableList<PageData>,
        val pageSize: String,
        val pageTotal: String,
        val rowTotal: String
    )

    data class PageData(
        /*val completed: Int,
        val dealNum: Int,
        val groupId: String,
        val phoneNum: Int,
        val projectId: String,
        val turnover: Double,
        val visitNum: Int,*/

        val curNum: String,//完成
        val groupName: String,
        val percent: String,//完成率
        val projectName: String,
        val rank: String,
        val targetNum: String,//目标
        val userName: String
    )
}

class aa{

}