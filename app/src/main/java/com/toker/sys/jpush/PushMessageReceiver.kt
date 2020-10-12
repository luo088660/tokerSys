package com.toker.sys.jpush

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log

import org.json.JSONException
import org.json.JSONObject

import cn.jpush.android.api.CmdMessage
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.api.JPushMessage
import cn.jpush.android.api.NotificationMessage
import cn.jpush.android.service.JPushMessageReceiver
import com.toker.sys.utils.tools.LogUtils

class PushMessageReceiver : JPushMessageReceiver() {

    override fun onMessage(context: Context, customMessage: CustomMessage) {
        LogUtils.e(TAG, "[onMessage] $customMessage")

        processCustomMessage(context, customMessage)
    }

    /**
     * 推动内容
     */
    override fun onNotifyMessageOpened(context: Context, message: NotificationMessage) {
        LogUtils.e(TAG, "[onNotifyMessageOpened] $message")

        /* try{
            //打开自定义的Activity
            Intent i = new Intent(context, TestActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE,message.notificationTitle);
            bundle.putString(JPushInterface.EXTRA_ALERT,message.notificationContent);
            i.putExtras(bundle);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            context.startActivity(i);
        }catch (Throwable throwable){

        }*/
    }

    /**
     * 获取极光推送app别名
     */
    fun getAlias(conn: Context) {
        getJpushAlias(conn, 0x3)
    }
    private fun getJpushAlias(conn: Context, sequence: Int) {
        JPushInterface.getAlias(conn, sequence)
    }
    override fun onMultiActionClicked(context: Context, intent: Intent) {
        LogUtils.e(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮")
        val nActionExtra = intent.extras!!.getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA)

        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
        if (nActionExtra == null) {
            LogUtils.d(TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null")
            return
        }
        if (nActionExtra == "my_extra1") {
            LogUtils.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮一")
        } else if (nActionExtra == "my_extra2") {
            LogUtils.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮二")
        } else if (nActionExtra == "my_extra3") {
            LogUtils.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮三")
        } else {
            LogUtils.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮未定义")
        }
    }

    override fun onNotifyMessageArrived(context: Context, message: NotificationMessage) {
        LogUtils.e(TAG, "[onNotifyMessageArrived] $message")
    }

    override fun onNotifyMessageDismiss(context: Context?, message: NotificationMessage?) {
        LogUtils.e(TAG, "[onNotifyMessageDismiss] " + message!!)
    }

    override fun onRegister(context: Context?, registrationId: String?) {
        LogUtils.e(TAG, "[onRegister] " + registrationId!!)
    }

    override fun onConnected(context: Context?, isConnected: Boolean) {

        LogUtils.e(TAG, "[onConnected] $isConnected")
//        Log.e(TAG, "[onMessage]---getAlias-- ${getAlias(context!!)}")
    }

    override fun onCommandResult(context: Context?, cmdMessage: CmdMessage?) {
        LogUtils.e(TAG, "[onCommandResult] " + cmdMessage!!)
    }

    override fun onTagOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        TagAliasOperatorHelper.instance.onTagOperatorResult(context!!, jPushMessage!!)
        super.onTagOperatorResult(context, jPushMessage)
    }

    override fun onCheckTagOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        TagAliasOperatorHelper.instance.onCheckTagOperatorResult(context!!, jPushMessage!!)
        super.onCheckTagOperatorResult(context, jPushMessage)
    }

    override fun onAliasOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        TagAliasOperatorHelper.instance.onAliasOperatorResult(context!!, jPushMessage!!)
        super.onAliasOperatorResult(context, jPushMessage)
    }

    override fun onMobileNumberOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        TagAliasOperatorHelper.instance.onMobileNumberOperatorResult(context!!, jPushMessage!!)
        super.onMobileNumberOperatorResult(context, jPushMessage)
    }

    //send msg to MainActivity
    private fun processCustomMessage(context: Context, customMessage: CustomMessage) {
        /*if (MainActivity.isForeground) {
            String message = customMessage.message;
            String extras = customMessage.extra;
            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (extraJson.length() > 0) {
                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {

                }

            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
        }*/
    }

    companion object {
        private val TAG = "PushMessageReceiver"
    }
}
