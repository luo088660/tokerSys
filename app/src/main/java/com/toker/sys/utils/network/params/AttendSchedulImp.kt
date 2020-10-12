package com.toker.sys.utils.network.params

/**
 * 考勤排班
 */
class AttendSchedulImp{
    companion object{

        //4.1.1.    考勤管理
        val API_CUST_ATTEND_TO_DAY = "attendance/getAttendanceToday"
        
        //4.1.2.	拓客员（含拓客组长）签到
        val API_CUST_ATTEND_CHECKIN = "attendance/checkIn"
        
        //4.1.3.	拓客员（含拓客组长）签退
        val API_CUST_ATTEND_CHECKOUT = "attendance/checkOut"
        
        //4.1.4.	自动上传工作轨迹
        val API_CUST_ATTEND_UPLOADLOCATION = "attendance/uploadLocation"
        
        //4.1.5.	获取个人考勤统计
        val API_CUST_ATTEND_LIST = "attendance/getAttendanceList"
        val API_UPDATE_CHECK_IN_DECLARE = "attendance/updateCheckInDeclare"
        val API_UPDATE_CHECK_OUT_DECLARE = "attendance/updateCheckOutDeclare"

        //4.1.6.	获取用户考勤详情
        val API_CUST_ATTEND_INFO = "attendance/getAttendanceInfo"
        
        
        //4.1.7.	获取项目或者团队考勤统计
        val API_CUST_ATTEND_BY_TEAM = "attendance/countAttendanceByTeam"
        
        
        //4.1.8.	获取指定月份员工考勤统计列表
        val API_CUST_ATTEND_LIST_BY_MONTH = "attendance/countAttendanceListByMonth"
        
        //4.1.9.	获取指定日期员工考勤列表
        val API_CUST_ATTEND_LIST_BY_DAY = "attendance/getAttendanceListByDay"

        //4.1.10.	异常考勤审批
        val API_CUST_ATTEND_UPDATE_STATUS = "attendance/updateAttendanceStatus"
        
        //4.2.	排班管理
        //4.2.1.	统计某项目某月的排班
        val API_CUST_ATTEND_BY_MONTH = "scheduling/countSchedulingByMonth"
        
        //4.2.2.	查询某项目某月的排班日期
        val API_CUST_ATTEND_LIST_DAY = "scheduling/listSchedulingDay"
        
        
        //4.2.3.	查询某项目某一天的排班计划
        val API_CUST_ATTEND_DETAIL_BY_DAY = "scheduling/listUserByDate"
//        val API_CUST_ATTEND_DETAIL_BY_DAY = "scheduling/getSchedulingDetailByDay"

    }
}
