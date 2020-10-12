package com.toker.sys.view.home.activity.my.bean


class ProjectFormanBean {

    data class ProjectForBean(
        val `data`: Data,
        val code: String,
        val desc: String
    ){
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }


    data class Data(
//        val dealNum: Int,
//        val phoneNum: Int,
//        val turnover: Int,
//        val visitNum: Int,
        val curDealNum: String,
        val curPhoneNum: String,
        val curTurnover: String,
        val curVisitNum: String
    )
}

/*
{
    "code" : "1",
    "desc" : "操作成功",
    "data" : {
    "curPhoneNum" : "0",
    "curVisitNum" : "0",
    "curDealNum" : "0",
    "curTurnover" : "0.00"
}
}*/
