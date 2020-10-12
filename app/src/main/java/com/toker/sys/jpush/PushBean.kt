package com.toker.sys.jpush

/**
 * 推送内容
 */
data class PushBean(
    val appkey: String,
    val developerArg0: String,
    val msgId: String,
    val notificationAlertType: Int,
    val notificationBigPicPath: String,
    val notificationBigText: String,
    val notificationBuilderId: Int,
    val notificationCategory: String,
    val notificationContent: String,
    val notificationExtras: String,
    val notificationId: Int,
    val notificationInbox: String,
    val notificationLargeIcon: String,
    val notificationPriority: Int,
    val notificationSmallIcon: String,
    val notificationStyle: Int,
    val notificationTitle: String,
    val notificationType: Int,
    val platform: Int
)
/*
{notificationId=465240903,
    msgId='9007214856543457',
    appkey='7de29d730477b732f431e7a9',
    notificationContent='推动hasodfasofda',
    notificationAlertType=7,
    notificationTitle='TokerSys',
    notificationSmallIcon='',
    notificationLargeIcon='',
    notificationExtras='{}',
    notificationStyle=0,
    notificationBuilderId=0,
    notificationBigText='',
    notificationBigPicPath='',
    notificationInbox='',
    notificationPriority=0,
    notificationCategory='',
    developerArg0='',
    platform=0,
    notificationType=0}
*/
