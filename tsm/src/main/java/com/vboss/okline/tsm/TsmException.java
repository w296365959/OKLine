package com.vboss.okline.tsm;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/5/4 <br/>
 * Summary : Tsm模块异常
 */
public class TsmException extends Exception {

    /**
     * 蓝牙地址错误。
     */
    public static final int ERROR_BLE_ADDRESS = 1000;

    /**
     * 蓝牙连接失败
     */
    public static final int BLE_CONNECTION_FAILURE = 1;
    /**
     * 蓝牙断开连接
     */
    public static final int BLE_CONNECTION_DISCONNECT = 2;
    /**
     * 蓝牙连接超时
     */
    public static final int BLE_CONNECTION_TIMEOUT = 3;
    /**
     * 蓝牙扫描异常
     */
    public static final int BLE_SCANNING_ERROR = 4004;
    /**
     * TSM注册异常
     */
    public static final int TSM_REGISTER_ERROR = 5001;
    /**
     * 卡应用安装异常
     */
    public static final int TSM_APP_INSTALL_ERROR = 5002;
    /**
     * 卡应用删除异常
     */
    public static final int TSM_APP_DELETE_ERROR = 5003;
    /**
     * TSM请求异常
     */
    public static final int TSM_ERROR = 5000;


    /**
     * 错误码
     */
    private int code;

    public TsmException(int code, String message) {
        super(message);
        this.code = code;
    }

    public TsmException(String errMsg) {
        super(errMsg);
    }

    public TsmException(int code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }

    /**
     * 获取错误码
     */
    public int getCode() {
        return code;
    }
}
