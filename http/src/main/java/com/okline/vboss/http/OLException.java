package com.okline.vboss.http;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/29 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class OLException extends RuntimeException {
    public static final int CODE_HTTP_EXCEPTION = 1000;

    private int code = -1;
    private String bodyType;

    public OLException(int code, String message) {
        this(message);
        this.code = code;
    }

    public OLException(int code, String bodyType, String message) {
        this(message);
        this.code = code;
        this.bodyType = bodyType;
    }

    public OLException(String message) {
        super(message);
    }

    public OLException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public OLException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    /**
     * 获取异常错误码
     */
    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        String s = bodyType != null ? String.format(" %s : [%s] ", getClass().getName(), bodyType) : getClass().getName() + " : ";
        String message = getLocalizedMessage();
        String codeDesc = code == -1 ? "Unknown" : String.valueOf(code);
//        String addition = bodyType != null ? String.format("[%s] %s", bodyType, codeDesc) : codeDesc;

        return (message != null) ? (s + codeDesc + " , " + message) : s + " " + codeDesc;

    }
}
