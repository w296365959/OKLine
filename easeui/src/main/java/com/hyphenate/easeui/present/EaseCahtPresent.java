package com.hyphenate.easeui.present;

import android.app.Activity;
import android.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.hyphenate.easeui.ui.EaseChatFragment;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: Mengyupeng<br/>
 * Email:  mengyupeng@okline.cn</br>
 * Date : $(DATE) </br>
 * Summary:
 */

public class EaseCahtPresent implements  ChatContact.ChatPresent{

    private Activity mContext;
    private ChatContact.ChatView chatView;
    PhoneBroadCastReceiver phoneReceiver;

    public EaseCahtPresent(ChatContact.ChatView chatView){
         this.chatView=chatView;
         mContext=((EaseChatFragment)chatView).getActivity();
        phoneReceiver=new PhoneBroadCastReceiver();

    }

    @Override
    public void registerPhoneReceiver() {
            IntentFilter   filter=new IntentFilter();
            filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
            filter.addAction("android.intent.action.PHONE_STATE");
            mContext.registerReceiver(phoneReceiver,filter);
    }

    @Override
    public void unRegisterPhoneReceiver() {
            try {
                mContext.unregisterReceiver(phoneReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public class PhoneBroadCastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
             // 如果是拨打电话
            if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                Log.v("hx1","newcall");
            }
            if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
                TelephonyManager tm = (TelephonyManager) context
                        .getSystemService(Service.TELEPHONY_SERVICE);
                switch (tm.getCallState()) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.v("hx1","ring");
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.v("hx1","offhook");
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.v("hx1","idle");
                        chatView.sendPhoneMessage("01:00");
                        break;
                }
            }
        }
    }



}
