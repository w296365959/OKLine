package com.okline.vboss.http;

import android.support.annotation.NonNull;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/6/2 <br/>
 * Summary : 获取OKLine TOKEN的请求实体类
 */
public class OLToken {

    /**
     * 1.设备号+手机码获取（此TOKEN只用户开户接口）
     * 2.设备号+欧乐号获取
     * 3.设备号+商户号获取
     * 4.设备号+MD5(设备号)（商户开户）
     * 5.设备号+MD5(设备号) (阿里实名认证)
     */
    static final int GRANT_TYPE_PHONE = 1;
    static final int GRANT_TYPE_OLNO = 2;
    static final int GRANT_TYPE_MER_NO = 3;
    static final int GRANT_TYPE_OPEN_MERCHANT = 4;
    static final int GRANT_TYPE_ALI_SECURITY = 5;


    private int grantType;
    private String deviceNo;
    private String imei;
    private String olNo;
    private String merNo;
    private String token;
    private Integer useRule;
    private Integer expTimes;
    private Integer effDate;
    private Integer expDate;

    OLToken(int grantType, @NonNull String deviceNo, @NonNull String imei) {
        this.grantType = grantType;
        this.deviceNo = deviceNo;
        this.imei = imei;
    }

    public void setOlNo(String olNo) {
        this.olNo = olNo;
    }

    public void setMerNo(String merNo) {
        this.merNo = merNo;
    }

    public String getToken() {
        return token;
    }

    public int getUseRule() {
        return useRule;
    }

    public int getExpTimes() {
        return expTimes;
    }

    public int getEffDate() {
        return effDate;
    }

    public int getExpDate() {
        return expDate;
    }
}
