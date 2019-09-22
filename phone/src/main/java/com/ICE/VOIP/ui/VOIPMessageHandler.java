package com.ICE.VOIP.ui;

import android.os.Handler;
import android.os.Message;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/4 <br/>
 * Summary : 在这里描述Class的主要功能
 */
class VOIPMessageHandler extends Handler {

    private String phoneNumber;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        VOIPMessage message = (VOIPMessage) msg.obj;
        Util.log(this, "message state = " + message + " \n phoneNumber = " + phoneNumber);
        for (PhoneListener listener : PhoneManager.getInstance().getListenerList()) {
            handleVOIPMessage(listener, message);
        }
    }

    private void handleVOIPMessage(PhoneListener phoneListener, VOIPMessage message) {
        switch ((int) message.nState) {
            case SIGNALSTATE.SIGNALSTATE_LOG_ING:
                Util.log(this, "SIGNALSTATE_LOG_ING");
                break;

            case SIGNALSTATE.SIGNALSTATE_LOGON:
                Util.log(this, "登录成功");
                PhoneManager.getInstance().setLogin(true);
                phoneListener.onLoginResult(true);
                break;

            case SIGNALSTATE.SIGNALSTATE_INFOREADY:
                Util.log(this, "SIGNALSTATE_INFOREADY");
                break;

            case SIGNALSTATE.SIGNALSTATE_LOGONFAIL:
                Util.log(this, "登陆失败");
                PhoneManager.getInstance().setLogin(false);
                phoneListener.onLoginResult(false);
                break;

            case SIGNALSTATE.SIGNALSTATE_REMOTERINGING:
                Util.log(this, "收到新来电");
                String readNumber = VOIPPhone.getCallerNumber();
                if (readNumber != null) {
                    phoneNumber = readNumber;
                }
                phoneListener.onRinging(false, phoneNumber);
                break;

            case SIGNALSTATE.SIGNALSTATE_CONNECTING:
                Util.log(this, "等待接通中");
                phoneListener.onRinging(true, phoneNumber);
                break;

            case SIGNALSTATE.SIGNALSTATE_ACCEPTCONNECTION:
                Util.log(this, "被呼叫方已接听电话");
                phoneListener.onCalling(true, phoneNumber);
                break;

            case SIGNALSTATE.SIGNALSTATE_DISCONNECT:
                Util.log(this, "已挂断");
                phoneListener.onHangUp(true, phoneNumber);
                break;

            case SIGNALSTATE.SIGNALSTATE_RESET:
                Util.log(this, "对方已挂断");
                phoneListener.onHangUp(false, phoneNumber);
                break;
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        for (PhoneListener phoneListener : PhoneManager.getInstance().getListenerList()) {
            phoneListener.onCallOut(phoneNumber);
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
