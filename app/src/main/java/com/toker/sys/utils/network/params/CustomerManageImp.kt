package com.toker.sys.utils.network.params

/**
 * 客户管理
 */
class CustomerManageImp {

    companion object {
        //4.1.1.	获取项目列表
        val API_CUST_MANAGE_GROUPLIST = "groupList"
        //4.1.2.	获取拓客员列表（分页）
        val API_CUST_MANAGE_USERLIST = "userList"
        //4.1.3.	获取小蜜蜂成员列表（分页）
        val API_CUST_MANAGE_BEELIST = "beeList"

        //1.	客户管理

        //1.1.1获取客户信息列表（分页）
        val API_CUST_CUSTOMER_LIST = "customer/recommendCustomerList"
        val API_CUST_MANAGE_LIST = "customer/customerList"
        val API_CUST_CUSTOMER_VAIN = "customer/customerInvalidList"
        val API_CUST_PUBLIC = "customer/customerPublic"
        val API_CUST_PROJECT_PAGE_PROJECT = "customer/getProjectToFollowUserPageByProjectId"
        val API_CUST_PROJECT_FOLLOW_USERPAGE = "customer/normalUserGetProjectToFollowUserPage"
        val API_CUST_SELECT_USER_LIST = "customer/selectUserList"
        //1.1.2	新增意向客户
        val API_CUST_MANAGE_ADD = "customer/customerAdd"
        //1.1.3    意向客户的详细信息
        val API_CUST_MANAGE_DETAIL = "customer/customerDetail"
        //1.1.4    编辑客户信息
        val API_CUST_MANAGE_COMPILE = "customer/customerCompile"
        //1.1.5    填写客户跟进记录
        val API_CUST_MANAGE_FOLLOW = "customer/customerFollow"
        //1.1.6    推荐意向客户（全量信息推荐）
        val API_CUST_MANAGE_RECOMMEND = "customer/customerRecommend"
        //1.1.7    标记无效客户信息
        val API_CUST_MANAGE_INVALID = "customer/customerInvalid"
        //1.1.8    已推荐客户详细信息
        val API_CUST_MANAGE_YTJ_DETAIL = "customer/customerytjDetail"
        val API_CUST_CUSTOMER_Y_DETAIL = "customer/customerYDetail"
        //1.1.9    撤销推荐客户
        //1.1.10    给无效和公客分配拓客员
        //1.1.11    统计每个项目已推荐客户(分页)
        val API_CUST_MANAGE_REVOCATION = "customer/customerRevocation"
        val API_CUST_CUSTOMER_ALLOCATION = "customer/customerAllocation"
        //1.1.12    推荐客户信息（不全量的）
        val API_CUST_MANAGE_BRECOMMEND = "customer/customerBRecommend"
        //1.1.13    客户信息删除
        val API_CUST_MANAGE_DEL = "customer/customerDel"
        val API_CUST_customer_Add = "customer/customerAdd"
        val API_CUST_CUSTOMER_ACTIVATE = "customer/customerActivate"

        //        公客池
        val API_CUST_CUSTOMER_PUBLIC = "customer/customerPublic"
        val API_CUST_CUSTOMER_INVALID_LIST = "customer/customerInvalidList"
        val API_CUST_CUSTOMER_FOLLOWUP = "customer/customerFollowUp"

        //获取所有省份
        val API_CUSTOMER_CITIES = "customer/getCities"
        //根据省份ID获取省份下的项目
        val API_CUSTOMER_PROJECT_ONCITYID = "customer/getProjectOnCityId"
        //获取联销项目地区
        val API_URL_CUSTOMER_ARRIVE_AREA = "customer/getArriveArea"
        //获取联销项目地点
        val API_CUSTOMER_ARRIVE_ADDR = "customer/getArriveAddr"
        val API_CUSTOMER_MGRUSER_PROJECTTO_FOLLOW = "customer/mgrUserProjectToFollow"
        val API_CUSTOMER_MGRUSER_PROJECT_ONFOLLOW = "customer/mgrUserProjectOnFollow"
        val API_CUSTOMER_CUSTOMER_ACTIVATE = "customer/customerActivate"
    }
}

////4.1.	团队管理
//3 接口规范	7
//3.1 团队管理
//3.1.1 获取项目列表
//3.1.2 获取拓客员列表
//3.1.1 获取小蜜蜂列表
//
//1.	客户管理	2
//1.1.1获取客户信息列表（分页）	2
//1.1.2	新增意向客户	7
//1.1.3	意向客户的详细信息	10
//1.1.4	编辑客户信息	16
//1.1.5	填写客户跟进记录	19
//1.1.6	推荐意向客户（全量信息推荐）	21
//1.1.7	标记无效客户信息	25
//1.1.8	已推荐客户详细信息	27
//1.1.9	撤销推荐客户	34
//1.1.10	给无效和公客分配拓客员	36
//1.1.11	统计每个项目已推荐客户(分页)	39
//1.1.12	推荐客户信息（不全量的）	43
//1.1.13	客户信息删除	46


//4.1.	团队管理

//4.1.1.	获取项目列表
//请求方式	POST
//请求地址	http://[ip:port/appname]/ groupList

//4.1.2.	获取拓客员列表（分页）
//请求方式	POST
//请求地址	http://[ip:port/appname]/ userList

//4.1.3.	获取小蜜蜂成员列表（分页）
//请求方式	POST
//请求地址	http://[ip:port/appname]/ beeList


//1.	客户管理

//1.1.1获取客户信息列表（分页）
//请求方式	POST
//请求地址	http://[ip:port/appname]/customer/customerList

//1.1.2	新增意向客户
//请求方式	POST
//请求地址	http://[ip:port/appname] /customer /customerAdd

//1.1.3    意向客户的详细信息
//请求方式    POST
//请求地址    http://[ip:port/appname]/customer/customerDetail

//1.1.4    编辑客户信息
//请求方式    POST
//请求地址    http://[ip:port/appname]/customer/customerCompile

//1.1.5    填写客户跟进记录
//请求方式    POST
//请求地址    http://[ip:port/appname]/customer/customerFollow

//1.1.6    推荐意向客户（全量信息推荐）
//请求方式 POST
//请求地址 http ://[ip:port/appname]/customer/customerRecommend

//1.1.7    标记无效客户信息
//请求方式 POST
//请求地址 http ://[ip:port/appname]/customer/customerInvalid

//1.1.8    已推荐客户详细信息
//请求方式 POST
//请求地址 http ://[ip:port/appname]/customer/customerytjDetail

//1.1.9    撤销推荐客户
//请求方式 POST
//请求地址 http ://[ip:port/appname]/customer/customerRevocation

//1.1.10    给无效和公客分配拓客员
//请求方式 POST
//请求地址 http ://[ip:port/appname]/customer/customerRevocation

//1.1.11    统计每个项目已推荐客户(分页)
//请求方式 POST
//请求地址 http ://[ip:port/appname]/customer/customerRevocation

//1.1.12    推荐客户信息（不全量的）
//请求方式 POST
//请求地址 http ://[ip:port/appname]/customer/customerBRecommend

//1.1.13    客户信息删除
//请求方式 POST
//请求地址 http ://[ip:port/appname]/customer/customerDel




