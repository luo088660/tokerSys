package com.toker.sys.view.home.fragment.my.item.bean

class MainCountBean {
    data class MainCBean(
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
    }


    data class Data(


        val toDistributedCustomerNum: Int,//待分配客户数量	int	管理员
        val overFollowCustomerNum: Int,    //待跟进客户数量	int	中层、管理员、组长、拓客员
        val toFollowTaskNum: Int,//待跟进任务数量	int	管理员
        val pendingTaskNum: Int,//	待审批任务数量	int	中层
        val myTaskNum: Int,//	我的任务数量	int	组长、拓客员
        val groupTaskNum: Int//	小组待跟进任务	int	组长
    )

}