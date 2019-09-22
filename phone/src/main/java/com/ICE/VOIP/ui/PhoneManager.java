package com.ICE.VOIP.ui;

import android.content.Context;
import android.os.Message;

import org.jarvis.liu.VoiceEngineConfig;

import java.util.ArrayList;
import java.util.List;

import static com.ICE.VOIP.ui.PepSdkInterface.PePCfg;
import static com.ICE.VOIP.ui.PepSdkInterface.PePInitialize;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/4 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class PhoneManager {
    private static final PhoneManager instance = new PhoneManager();
    private boolean stopFlag = false;
    //    private VOIPPhone voipPhone;
    private VOIPMessageHandler voipMessageHandler = new VOIPMessageHandler();
    private int pepHandle;
    private boolean isInit = false;
    private boolean isLogin = false;
    private Thread voipPhoneThread;
    private List<PhoneListener> listenerList = new ArrayList<>();
    private Context context;

    private PhoneManager() {
    }

    public static PhoneManager getInstance() {
        return instance;
    }

    public void initConfigFile(Context appContext) {
        context = appContext;
        VoiceEngineConfig.init(context);
        RingtoneManager.getInstance().init(context);
        isInit = VOIPPhone.initVOIPPhone();
        if (isInit) {
            isInit = initPep(context);
        } else {
            Util.log(this, "initVOIPPhone faild");
        }
        if (isInit) {
            if (voipPhoneThread == null) {
                initMessageThreads();
            }
        } else {
            Util.log(this, "initPep faild");
        }
    }

    /**
     * 拨打电话
     *
     * @param number
     */
    public void call(String number) {
        if (isInit) {
            VOIPPhone.startVOIPPhoneCall(number);
            voipMessageHandler.setPhoneNumber(number);
        } else {
            throw new RuntimeException("not init or init failed");
        }
    }

    /**
     * 接听电话
     */
    public void answer() {
        boolean result = VOIPPhone.talkVOIPPhone();
        if (result) {
            for (PhoneListener phoneListener : listenerList) {
                phoneListener.onCalling(false, voipMessageHandler.getPhoneNumber());
            }
        }
    }

    /**
     * 挂断电话
     */
    public void hangUp() {
        boolean result = VOIPPhone.stopVOIPPhoneCall();
    }

    /**
     * 拒接电话
     */
    public void refuse() {
        boolean result = VOIPPhone.refuseVOIPPhoneCall();
    }

    public void addPhoneListener(PhoneListener phoneListener) {
        if (!listenerList.contains(phoneListener)) {
            listenerList.add(phoneListener);
        }
    }

    public boolean removePhoneListener(PhoneListener phoneListener) {
        return listenerList.remove(phoneListener);
    }

    public List<PhoneListener> getListenerList() {
        return listenerList;
    }

    public void destory() {
        stopFlag = true;
        PepSdkInterface.PePDestroy(pepHandle);
        Util.log(this, "destory pepSdk.PePDestroy");
        VOIPPhone.setVOIPPhoneTaskStatus(false);
        VOIPPhone.termVOIPPhone();
        Util.log(this, "destory voipPhone.termVOIPPhone");
    }

    private boolean initPep(Context context) {
        PepSdkInterface.Pep_Cfg_Para pCfgPara = new PepSdkInterface.Pep_Cfg_Para();
        pCfgPara.dwAutoDownLoadFileSize = 0;
        pCfgPara.dwAutoUploadFileSize = 0;
        pCfgPara.sdkVerId = 0;
        pCfgPara.szCharSet = new String("GB2312");
        pCfgPara.szAppDataSaveDir = context.getFilesDir().getPath();
        pCfgPara.szPepServer3N = new String("20001112");
        pCfgPara.szFileSaveDir = context.getFilesDir().getPath();

        if (PePCfg(pCfgPara) == false) {
            Util.log(this, "PePCfg failed ");
            return false;
        }
        pepHandle = PePInitialize();
        if (pepHandle == 0) {
            Util.log(this, "PePInitialize failed ");
            return false;
        }
        return true;
    }

    private void initMessageThreads() {
        voipPhoneThread = new Thread(new Runnable() {
            public void run() {
                while (!stopFlag && VOIPPhone.getVOIPPhoneTaskStatus()) {
                    VOIPPhone.taskVOIPPhone();
                    try {
                        voipPhoneThread.join(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                voipPhoneThread = null;
                Util.log(PhoneManager.this, "voip Phone Thread stop");
            }
        });
        voipPhoneThread.start();
        new Thread(new Runnable() {
            public void run() {
                VOIPMessage message = new VOIPMessage();
                boolean mesResult = true;
                while (!stopFlag && mesResult) {
                    mesResult = VOIPPhone.getVOIPMessage(message);
                    Util.log(PhoneManager.this, "mesResult : " + mesResult + " , " + message);
                    Message msg = new Message();
                    msg.obj = message;
                    voipMessageHandler.sendMessage(msg);
                    message = new VOIPMessage();
                }
                Util.log(PhoneManager.this, "voip message Thread stop");
            }
        }).start();
    }

    public boolean isInit() {
        return isInit;
    }

    public boolean isLogin() {
        return isLogin;
    }

    void setLogin(boolean login) {
        isLogin = login;
    }

    public void onNetWorkReconnect(int netMode) {
        if (isInit) {
            VOIPPhone.VOIPNetSwitchNotify(netMode);
        }
    }

}
