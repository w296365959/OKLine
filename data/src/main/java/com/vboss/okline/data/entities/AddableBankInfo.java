package com.vboss.okline.data.entities;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/6/13 <br/>
 * Summary : 可支持的银行列表
 */
public class AddableBankInfo {

    /**
     * 商户名
     */
    private String merName;
    /**
     * 商户编号
     */
    private String merNo;
    /**
     * 商户logo
     */
    private String merIcon;

    private int cardLen;

    public String getMerName() {
        return merName;
    }

    public String getMerNo() {
        return merNo;
    }

    public String getMerIcon() {
        return merIcon;
    }

    public int getCardLen() {
        return cardLen;
    }

    @Override
    public String toString() {
        return "AddableBankInfo{" +
                "merName='" + merName + '\'' +
                ", merNo='" + merNo + '\'' +
                ", merIcon='" + merIcon + '\'' +
                ", cardLen=" + cardLen +
                '}';
    }
}
