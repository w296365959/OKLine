package com.okline.vboss.assistant.net;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/6/7 <br/>
 * Summary : 卡片实体类
 */
public class CardEntity implements Serializable {

    /**
     * imgUrl : http://down.okline.com.cn/trafficicon/ad/HZDT.png
     * cardId : 4944
     * cardNo : 3083907922
     * isQuickPass : 0
     * cardName : 杭州通
     * cardShort : HZDT
     * bindId :
     * isDefault : 0
     * cardMainType : 600
     * iconUrl : http://down.okline.com.cn/trafficicon/HZT.png
     * aid : A000000003869807013100
     * balance
     */

    private String imgUrl;
    private int cardId;
    private String cardNo;
    private int isQuickPass;
    private String cardName;
    private String cardShort;
    private String bindId;
    private int isDefault;
    private int cardMainType;
    private String iconUrl;
    private String aid;
    private int balance;    //余额
    /**
     * isopen : 3
     * merName : 广发银行
     * configId : 13
     */

    @SerializedName("isopen")
    private int isDownload;
    private String merName;
    private int configId;

    public String getImgUrl() {
        return imgUrl;
    }

    public int getCardId() {
        return cardId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public Boolean isQuiclPass() {
        //modify by wangshuai 2017-06-15 quick pass bank card
        return isQuickPass == 2;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardShort() {
        return cardShort;
    }

    public String getBindId() {
        return bindId;
    }


    public Boolean isDefault() {
        return isDefault == 1;
    }

    public int getCardMainType() {
        return cardMainType;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getAid() {
        return aid;
    }

    public boolean isDownload() {
        return isDownload > 0;
    }


    public String getMerName() {
        return merName;
    }

    public int getBalance() {
        return balance;
    }

    public int getConfigId() {
        return configId;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public void setIsQuickPass(int isQuickPass) {
        this.isQuickPass = isQuickPass;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setCardShort(String cardShort) {
        this.cardShort = cardShort;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public void setCardMainType(int cardMainType) {
        this.cardMainType = cardMainType;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setIsDownload(int isDownload) {
        this.isDownload = isDownload;
    }

    public void setMerName(String merName) {
        this.merName = merName;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    @Override
    public String toString() {
        return "CardEntity{" +
                "imgUrl='" + imgUrl + '\'' +
                ", cardId=" + cardId +
                ", cardNo='" + cardNo + '\'' +
                ", isQuickPass=" + isQuickPass +
                ", cardName='" + cardName + '\'' +
                ", cardShort='" + cardShort + '\'' +
                ", bindId='" + bindId + '\'' +
                ", isDefault=" + isDefault +
                ", cardMainType=" + cardMainType +
                ", iconUrl='" + iconUrl + '\'' +
                ", aid='" + aid + '\'' +
                ", balance=" + balance +
                ", isDownload=" + isDownload +
                ", merName='" + merName + '\'' +
                ", configId=" + configId +
                '}';
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    //Added by wangshuai 2017-06-26
    public CardEntity(String imgUrl, int cardId, String cardNo, int isQuickPass, String cardName,
                      String cardShort, String bindId, int isDefault, int cardMainType,
                      String iconUrl, String aid, int balance,
                      int isDownload, String merName, int configId) {
        this.imgUrl = imgUrl;
        this.cardId = cardId;
        this.cardNo = cardNo;
        this.isQuickPass = isQuickPass;
        this.cardName = cardName;
        this.cardShort = cardShort;
        this.bindId = bindId;
        this.isDefault = isDefault;
        this.cardMainType = cardMainType;
        this.iconUrl = iconUrl;
        this.aid = aid;
        this.balance = balance;
        this.isDownload = isDownload;
        this.merName = merName;
        this.configId = configId;
    }
}
