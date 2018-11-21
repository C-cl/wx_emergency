package com.wxsoft.emergency.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log

import org.json.JSONException
import org.json.JSONObject

import cn.jpush.android.api.JPushInterface
import com.wxsoft.emergency.data.prefs.SharedPreferenceStorage
import javax.inject.Inject

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
class JPushReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val bundle = intent.extras
            when (intent.action) {
                JPushInterface.ACTION_REGISTRATION_ID -> {
                    val regId = bundle!!.getString(JPushInterface.EXTRA_REGISTRATION_ID)
                }
                JPushInterface.ACTION_MESSAGE_RECEIVED -> {
                    val notifactionId = bundle!!.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)
                    processCustomMessage(context, bundle)
                }
                JPushInterface.ACTION_NOTIFICATION_RECEIVED -> {//Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                    val notifactionId = bundle!!.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)
                    Log.i("推送",notifactionId.toString())
                }
                JPushInterface.ACTION_NOTIFICATION_OPENED -> {

                    val notifactionId = bundle!!.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)
                }
                JPushInterface.ACTION_RICHPUSH_CALLBACK -> {
                    val notifactionId = bundle!!.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)
                }
                JPushInterface.ACTION_CONNECTION_CHANGE -> {
                    val connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false)
                    Log.i("极光连接",if(connected)"成功" else "失败")
//                    val regId = bundle!!.getString(JPushInterface.EXTRA_REGISTRATION_ID)
//                    sharedPreferenceStorage.jpushRegId=regId
                }
                else -> {
                    //Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
                }
            }
        } catch (e: Exception) {
            Log.i("错误",e.message)
        }

    }

    //send msg to MainActivity
    private fun processCustomMessage(context: Context, bundle: Bundle?) {
        //		if (MainActivity.isForeground) {
        //			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        //			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        //			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
        //			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
        //			if (!ExampleUtil.isEmpty(extras)) {
        //				try {
        //					JSONObject extraJson = new JSONObject(extras);
        //					if (extraJson.length() > 0) {
        //						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
        //					}
        //				} catch (JSONException e) {
        //
        //				}
        //
        //			}
        //			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
        //		}
    }

    companion object {

        // 打印所有的 intent extra 数据
        private fun printBundle(bundle: Bundle): String {
            val sb = StringBuilder()
            for (key in bundle.keySet()) {
                if (key == JPushInterface.EXTRA_NOTIFICATION_ID) {
                    sb.append("\nkey:" + key + ", value:" + bundle.getInt(key))
                } else if (key == JPushInterface.EXTRA_CONNECTION_CHANGE) {
                    sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key))
                } else if (key == JPushInterface.EXTRA_EXTRA) {
                    if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                        //Logger.i(TAG, "This message has no Extra data");
                        continue
                    }

                    try {
                        val json = JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA))
                        val it = json.keys()

                        while (it.hasNext()) {
                            val myKey = it.next()
                            sb.append(
                                "\nkey:" + key + ", value: [" +
                                        myKey + " - " + json.optString(myKey) + "]"
                            )
                        }
                    } catch (e: JSONException) {
                        //Logger.e(TAG, "Get message extra JSON error!");
                    }

                } else {
                    sb.append("\nkey:" + key + ", value:" + bundle.get(key))
                }
            }
            return sb.toString()
        }
    }
}
