package com.toker.sys.view.home.menu

import com.toker.sys.R
import com.toker.sys.view.home.fragment.custom.CustomFragment
import com.toker.sys.view.home.fragment.custom.cutm.CUTmFragment
import com.toker.sys.view.home.fragment.my.MyFragment
import com.toker.sys.view.home.fragment.sheet.SheetFragment
import com.toker.sys.view.home.fragment.task.TaskFragment

enum class MainTabs  constructor(var i: Int, var names: String?, var icon: Int, var cla: Class<*>?) {
    Home0(0, "首页", R.drawable.tab_sheet_selector, SheetFragment.newInstance()::class.java),
    Home1(1, "任务", R.drawable.tab_task_selector, TaskFragment.newInstance()::class.java),
//    Home2(2, "客户", R.drawable.tab_custom_selector, CustomFragment.newInstance()::class.java),
    Home2(2, "客户", R.drawable.tab_custom_selector, CUTmFragment.newInstance()::class.java),
    Home3(3, "我的", R.drawable.tab_my_selector, MyFragment.newInstance()::class.java)
}