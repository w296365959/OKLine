package com.vboss.okline.test;

import android.app.Activity;
import android.media.AudioManager;

import com.ICE.VOIP.ui.PhoneListener;
import com.ICE.VOIP.ui.RingtoneManager;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/4 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class PhoneListenerImpl implements PhoneListener {
    public static final int STATE_NULL = 0;
    public static final int STATE_INIT_FAILED = 1;
    public static final int STATE_INIT_SUCCESS = 2;
    public static final int STATE_LOGIN = 3;
    public static final int STATE_READY = 10;
    public static final int STATE_RINGING_ACTIVE = 11;
    public static final int STATE_RINGING = 12;
    public static final int STATE_CALLING = 13;
    TestPhoneActivity activity;
    private int state = STATE_NULL;

    public PhoneListenerImpl(TestPhoneActivity activity) {
        this.activity = activity;
    }

    public void setActivity(Activity activity){

    }

    @Override
    public void onLoginResult(boolean isSuccess) {
        activity.showLog("登陆结果 " + isSuccess);
        state = STATE_READY;
    }

    @Override
    public void onCallOut(String phoneNumber) {
        activity.showLog("正在拨号 " + phoneNumber);
        RingtoneManager.getInstance().ringback(true);
        state = STATE_RINGING_ACTIVE;
    }

    @Override
    public void onRinging(boolean active, String phoneNumber) {
        activity.showLog("正在响铃 " + phoneNumber);
        if (!active) {
            RingtoneManager.getInstance().playRingtone();
            state = STATE_RINGING;
        } else {
            state = STATE_RINGING_ACTIVE;
        }
        activity.setVolumeControlStream(AudioManager.STREAM_RING);
        //TODO 拿到当前的activity intent到callingactivity
    }

    @Override
    public void onHangUp(boolean active, String phoneNumber) {
        activity.showLog("已挂断 " + phoneNumber);
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
            activity.showLog("对方已接听，正在通话 " + phoneNumber);
        } else {
            activity.showLog("接听，正在通话 " + phoneNumber);
        }
        state = STATE_CALLING;
    }

    public int getState() {
        return state;
    }
}
