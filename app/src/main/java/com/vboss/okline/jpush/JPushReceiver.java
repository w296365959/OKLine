package com.vboss.okline.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/13 <br/>
 * Summary : 极光推送广播接收器
 */
public class JPushReceiver extends BroadcastReceiver {
    private JPushHelper pushHelper;
    public static final String TAG = "JIGUANG";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (pushHelper == null) {
            pushHelper = new JPushHelper(context);
        }
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();
        Timber.tag(TAG).i("JIGUANG-JPUSH: %s, \nBundle : %s", action, printBundle(bundle));
        if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(action)) {
            boolean connection = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            if (connection) {
                Timber.tag(TAG).w("JIGUANG push is connected");
            }

        } else if (JPushInterface.ACTION_REGISTRATION_ID.equals(action)) {
            String regId = intent.getStringExtra(JPushInterface.EXTRA_REGISTRATION_ID);
            Timber.tag(TAG).w("RegistrationId : %s", regId);

        } else if (JPushInterface.ACTION_NOTIFICATION_CLICK_ACTION.equals(action)) {
            //JPush通知被点击

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(action)) {
            //收到JPush通知


        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(action)) {
            //JPush通知被打开

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {
            //收到JPush自定义消息
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Timber.tag(TAG).i("JIGUANG PUSH : title=%s, message=%s, extra=%s", title, message, extra);
            String data = null;
            try {
                JSONObject json = new JSONObject(extra);
                data = json.optString("extras");
                Timber.tag(TAG).i("Data : " + data);
                Timber.tag(TAG).i("Received data : %s", data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pushHelper.dispatchJPush(title, message, data);
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
