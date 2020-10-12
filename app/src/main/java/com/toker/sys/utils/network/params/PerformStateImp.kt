package com.toker.sys.utils.network.params

/**
 * 业绩报表
 */
class PerformStateImp{
    companion object{

        //4.1.	业绩统计

        //4.1.1.	当前用户的业绩查询
        val API_PER_STATE_PER_FORMANCE= "performance/getMyPerformance"

        //4.1.2.	项目拓客业绩排名
        val API_PER_STATE_BY_PROJECT= "performance/orderByProject"

        //4.1.3.	团队拓客业绩排名
        val API_PER_GROUP_PER_FORMANCE= "performance/getGroupPerformance"
        //4.1.4.	拓客员业绩排名
        val API_PER_STATE_BY_GROUP= "performance/getUserPerformance"
        val API_PER_PROJECT_PERFORMANCE= "performance/getProjectPerformance"
        val API_PER_USER_COMPANY_RANK= "performance/getUserCompanyRank"
        val API_PER_GET_GROUP_ADMIN= "group/getGroupAdmin"
        val API_PER_COUNT_GROUP= "group/countGroup"
        val API_PER_BEE_LIST= "group/getBeeList"
        val API_PER_USER_LIST= "group/getUserList"
        val API_PER_GROUP_BY_PROJECTID= "group/getGroupByProjectId"
        val API_PER_CUR_GROUP_LIST1= "/group/getCurGroupList"
        val API_PER_CUR_GROUP_LIST= "group/getGroupListByAdmin"


        val API_PER_GBEE_LIST= "bee/getBeeList"
        val API_PER_RECORD_LIST= "bee/getAttendanceRecordList"
        val API_PER_CUSTOMER_LIST= "bee/getCustomerList"



    }
}
