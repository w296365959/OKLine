package com.ICE.VOIP.ui;

class VOIPPhone {
    static final String TAG = "VOIPPhone";

    static {
        System.loadLibrary("Common_android");
        System.loadLibrary("udt_android");
        System.loadLibrary("ISSN_Protocol_android");
        System.loadLibrary("ICM_android");
        System.loadLibrary("VOIPCore_android");
        System.loadLibrary("Upgrade_android");
        System.loadLibrary("IMCore_android");
        System.loadLibrary("VOIPPhoneJNI");
    }

    // VOIPPhone
    public static boolean initVOIPPhone() {
        return VOIPPhoneInit();
    }

    public static void termVOIPPhone() {
        VOIPPhoneTerm();
    }

    public static boolean taskVOIPPhone() {
        VOIPPhoneTask();
        return true;
    }

    public static boolean getVOIPPhoneTaskStatus() {
        boolean status;
        status = VOIPPhoneTaskStatusGet();
        return status;
    }

    public static void setVOIPPhoneTaskStatus(boolean statue) {
        VOIPPhoneTaskStatusSet(statue);
    }

    // Message
    public static boolean getVOIPMessage(VOIPMessage message) {
        return VOIPMessageGet(message);
    }

    public boolean handleVOIPMessage(VOIPMessage message) {
        return VOIPMessageHandle(message);
    }

    // only used to post the SIGNALSTATE_DESTROY message
    public void postVOIPMessage(VOIPMessage message) {
        if (message == null)
            return;
        VOIPMessagePost(message);
    }

    public static String getCallerNumber() {
        return VOIPCallerNumberGet();
    }

    // 开始拨打电话
    public static boolean startVOIPPhoneCall(String dialString) {
        if (dialString == null || dialString.length() == 0)
            return false;
        return VOIPPhoneCallStart(dialString);
    }

    // 开始说话
    public static boolean talkVOIPPhone() {
        return VOIPPhoneTalk();
    }

    // 初始化录音缓冲大小和播放声音缓冲大小
    public boolean initVOIPPhoneTalkData(int recordBufSize, int playBufSize) {
        if (recordBufSize == 0 || playBufSize == 0)
            return false;
        return VOIPPhoneTalkDataInit(recordBufSize, playBufSize);
    }

    public boolean sendVOIPPhoneTalkData(short recordBuffer[], int bufferSize) {
        if (bufferSize == 0)
            return false;
        return VOIPPhoneTalkDataSend(recordBuffer, bufferSize);
    }

    public int recvVOIPPhoneTalkData(short playBuffer[], int playBufferSize) {
        int recvSize;
        if (playBuffer == null || playBufferSize <= 0)
            return 0;
        recvSize = VOIPPhoneTalkDataRecv(playBuffer, playBufferSize);
        return recvSize;
    }

    public static boolean stopVOIPPhoneCall() {
        return VOIPPhoneCallStop();
    }

    public static boolean refuseVOIPPhoneCall() {
        return VOIPPhoneCallRefuse();
    }


    // 电话初始化
    private static native boolean VOIPPhoneInit();

    private static native boolean VOIPPhoneTerm();

    private static native boolean VOIPPhoneTask();

    private static native boolean VOIPPhoneTaskStatusGet();

    private static native void VOIPPhoneTaskStatusSet(boolean statue);

    // 设置电话模式
    public native boolean setPhoneMode(VoiceMode phonemodedataa);


    // 获取消息
    private static native boolean VOIPMessageGet(VOIPMessage message);

    private native boolean VOIPMessageHandle(VOIPMessage message);

    // 发送消息
    private native void VOIPMessagePost(VOIPMessage message);

    // Phone Info
    private static native String VOIPCallerNumberGet();

    // 拨号
    private static native boolean VOIPPhoneCallStart(String dialString);

    private static native boolean VOIPPhoneTalk();

    private native boolean VOIPPhoneTalkDataInit(int recordBufSize, int playBufSize);

    private native boolean VOIPPhoneTalkDataSend(short recordBuffer[], int bufferSize);

    private native int VOIPPhoneTalkDataRecv(short playBuffer[], int playBufferSize);

    // 挂电话
    private static native boolean VOIPPhoneCallStop();

    // 拒绝接听
    private static native boolean VOIPPhoneCallRefuse();

    // binding
    public native boolean VOIPCheckLocalSimID(String SimId);

    // 绑定请求
    public native boolean VOIPBindRequst(String myNum, String model);


    public native boolean PePTestSdPath(String path);

    ////////////////////////////////////////////////////
    //	 typedef enum {
    //		  INM_CABLE = 0,
    //		  INM_WIFI  = 1,
    //		  INM_4G    = 2,
    //	 }ICM_NET_MODE;
    ////////////////////////////////////////////////////
    public static native boolean VOIPNetSwitchNotify( int netMode );

}