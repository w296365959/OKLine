package com.vboss.okline.ui.contact.callPhone;

import android.media.AudioManager;

import com.ICE.VOIP.ui.PhoneListener;
import com.ICE.VOIP.ui.RingtoneManager;
import com.vboss.okline.test.TestPhoneActivity;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/5 18:47
 * Desc :
 */

public class PhoneListenerImp implements PhoneListener{
    public static final int STATE_NULL = 0;
    public static final int STATE_INIT_FAILED = 1;
    public static final int STATE_INIT_SUCCESS = 2;
    public static final int STATE_LOGIN = 3;
    public static final int STATE_READY = 10;
    public static final int STATE_RINGING_ACTIVE = 11;
    public static final int STATE_RINGING = 12;
    public static final int STATE_CALLING = 13;
    CallingActivity activity;
    private int state = STATE_NULL;

    public PhoneListenerImp(CallingActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onLoginResult(boolean isSuccess) {
//        activity.showLog("登陆结果 " + isSuccess);
        state = STATE_READY;
    }

    @Override
    public void onCallOut(String phoneNumber) {
//        activity.showLog("正在拨号 " + phoneNumber);
        RingtoneManager.getInstance().ringback(true);
        state = STATE_RINGING_ACTIVE;
    }

    @Override
    public void onRinging(boolean active, String phoneNumber) {
//        activity.showLog("正在响铃 " + phoneNumber);
        if (!active) {
            RingtoneManager.getInstance().playRingtone();
            state = STATE_RINGING;
        } else {
            state = STATE_RINGING_ACTIVE;
        }
        activity.setVolumeControlStream(AudioManager.STREAM_RING);
    }

    @Override
    public void onHangUp(boolean active, String phoneNumber) {
//        activity.showLog("已挂断 " + phoneNumber);
        RingtoneManager.getInstance().ringback(false);
        RingtoneManager.getInstance().stopRingtone();
        RingtoneManager.getInstance().phoneTalkOver();
        activity.setVolumeControlStream(AudioManager.STREAM_RING);
        state = STATE_READY;
    }

    @Override
    public void onCalling(boolean active, String phoneNumber) {
        RingtoneManager.getInstance().ringback(false);
        RingtoneManager.getInstance().stopRingtone();
        RingtoneManager.getInstance().phoneTalkInit();
        activity.setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        if (active) {
//            activity.showLog("对方已接听，正在通话 " + phoneNumber);
        } else {
//            activity.showLog("接听，正在通话 " + phoneNumber);
        }
        state = STATE_CALLING;
    }

    public int getState() {
        return state;
    }
}
