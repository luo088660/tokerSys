package com.toker.sys.utils.network.params

/**
 * 任务管理
 */
class TaskManageImp {
    companion object {

        //4.1.	任务管理

        //4.1.1.	查询当前用户待审批任务总数
        val API_TASK_APPROVE_COUNT = "task/getApproveCount"
        //        4.1.2.	查询拓客中层指标型任务列表（分页）
        val API_TASK_MIDDLE_TARGET_TASK_PAGE = "task/getMiddleTargetTaskPage"
        //4.1.3.	查询拓客管理员指标型任务列表
        val API_TASK_MANAGER_TARGET_TASKPAGE = "task/getManagerTargetTaskPage"

        val API_TASK_PERSON_TARGET_TASKPAGE = "task/getPersonTargetTaskPage"

        val API_TASK_GROUP_TARGET_TASK_PAGE = "task/getGroupTargetTaskPage"
        //4.1.8.	查询管理员下发的事务型任务列表
        val API_TASK_MANAGER_EVENT_TASK_PAGE = "task/getManagerEventTaskPage"
        val API_TASK_GROUP_EVENT_TASK_PAGE = "task/getGroupEventTaskPage"
        val API_TASK_PERSON_EVENT_TASK_PAGE = "task/getPersonEventTaskPage"



        //4.1.2.	查询当前用户指标型任务列表（分页）
        val API_TASK_TARGET_TASK_PAGE = "task/getTargetTaskPage"

        //4.1.3.	根据任务ID查询指标型任务详情
        val API_TASK_TARGET_TASK = "task/getTargetTaskInfo"

        //4.1.4.	根据指标性任务ID查看日任务列表
        val API_TASK_TARGET_DAY_TASK = "task/getTargetDayTask"

        //4.1.5.	查询当前用户事务型任务列表（分页）
        val API_TASK_EVENTTASK_PAGE = "task/getEventTaskPage"

        //4.1.6.	根据任务ID查询事物性任务详情
        val API_TASK_EVENTTASK = "task/getEventTask"

        val API_MAIN_PAGE_COUNT = "auth/getMainPageCount"
        val API_TASK_EVENT_TASKR_EPORTLIST = "task/getEventTaskReportList"
        val API_TASK_EVENT_TRACE_COUNT = "task/getEventTraceCount"
        val API_TASK_EVENT_TASK_TRACE_LIST = "task/getEventTaskTraceList"
        val API_TASK_MYEVENT_TASK_REPOR_TLIST = "task/getMyEventTaskReportList"

        //4.1.7.	根据事务型任务ID查看汇报列表(分页)
        val API_TASK_EVENTTASK_REPORTLIST = "task/getEventTaskReportList"

        //4.1.8.	保存事务任务汇报
        val API_TASK_SAVE_EVENTTASK_REPORT = "task/saveEventTaskReport"
        val API_TASK_SAVE_UPLOAD_TASK_RECORD = "file/uploadTaskRecord"

        //4.1.9.	任务审批
        val API_TASK_APPROVE = "task/approve"

        //4.2.	消息模块

        //4.2.1.	未读消息统计
        val API_TASK_NOREAD_COUNT = "msg/noReadCount"

        //4.2.2.	查询我的消息列表(分页)
        val API_TASK_MSG_LIST = "msg/getMsgList"

        //4.2.3.	消息标记已读
        val API_TASK_READ_MSG = "msg/readMsg"


    }
}
