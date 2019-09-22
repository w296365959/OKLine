package com.vboss.okline.ui.opay;

import com.google.gson.annotations.SerializedName;

/**
 * OKLine(ShenZhen) co.,Ltd.<br/>
 * 作者 : Shi Haijun <br/>
 * 邮箱 : haijun@okline.cn<br/>
 * 日期 : 2016/3/7 14:50<br/>
 * 描述 : 网络请求响应数据结构
 */
public class OLResult<DATA> {
    @SerializedName("resultCode")
    private int code;
    @SerializedName("resultMsg")
    private String message;

    private DATA data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DATA getData() {
        return data;
    }

    public boolean isSuccess() {
        return OLConfig.RESULT_OK == getCode();
    }
}
