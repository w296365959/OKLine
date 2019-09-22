package com.vboss.okline.ui.auth;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * OKLine(luoxiuxiu) co.,Ltd.<br/>
 * Author  : luoxiuxiu <br/>
 * Email   : show@okline.cn <br/>
 * Date    : 2017/4/27 <br/>
 * Summary : 发送短信
 */

public class SendEms {

    private Context context;
    private final static String SEND_ACTION = "send";
    private final static String DELIVERED_ACTION = "delivered";
    private boolean isSend = true;

    public SendEms(Context context, String receiver, String sendPhone, String text) {
        this.context = context;
        init(receiver, sendPhone, text);
    }


    private void init(String receiver, String sendPhone, String text) {
        SmsManager s = SmsManager.getDefault();
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SEND_ACTION),
                PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED_ACTION),
                PendingIntent.FLAG_CANCEL_CURRENT);
        // 发送完成
        context.registerReceiver(broadcastReceiver, new IntentFilter(SEND_ACTION));

        // 对方接受完成
        context.registerReceiver(broadcastReceiver1, new IntentFilter(DELIVERED_ACTION));

        // 发送短信，sentPI和deliveredPI将分别在短信发送成功和对方接受成功时被广播
        s.sendTextMessage(receiver, sendPhone, text, sentPI, deliveredPI);
    }

    public boolean isSend() {
        return isSend;
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
//                        Toast.makeText(context, "Send Success!", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(context, "信息发送失败.",
                            Toast.LENGTH_SHORT).show();
                    isSend = false;
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(context, "信息发送失败，服务当前不可用.",
                            Toast.LENGTH_SHORT).show();
                    isSend = false;
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(context, "信息发送失败，无PDU.", Toast.LENGTH_SHORT).show();
                    isSend = false;
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(context, "信息发送失败，radio被关闭..",
                            Toast.LENGTH_SHORT).show();
                    isSend = false;
                    break;
                default:
                    Toast.makeText(context, "信息发送失败.", Toast.LENGTH_SHORT).show();
                    isSend = false;
                    break;
            }
        }
    };

    BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
//                        Toast.makeText(context, "Delivered Success!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context, "对方接收失败!", Toast.LENGTH_SHORT).show();
                    isSend = false;
                    break;
            }
        }
    };

    public void onDestory() {
        context.unregisterReceiver(broadcastReceiver);
        context.unregisterReceiver(broadcastReceiver1);
    }


}
