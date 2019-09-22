package com.vboss.okline.jpush;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/22 <br/>
 * Summary : JPush推送数据实体类
 */
public class JPushEntity implements Parcelable {

    /**
     * amount : 0
     * cardId : 4968
     * balance : 1
     * operatorType : 100003
     * detail : 杭州通开卡
     * orderNo : 20170513172505805438
     * cardMainType : 600
     * state : 1
     * errorCode : 0   3078--余额不足
     * method : 杭州通
     * payDate : 2017-05-13 17:25:05
     * slipUrl : pos单
     * receiptUrl ： 小票
     * invoiceUrl ： 发票
     * integral : 积分
     */

    private int amount;
    private int cardId;
    private int balance;
    private int operatorType;
    private int integral;
    private String detail;
    private String orderNo;
    private int cardMainType;
    private int state;
    private int errorCode;
    private String method;
    private String payDate;
    private String slipUrl;
    private String receiptUrl;
    private String invoiceUrl;

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public String getSlipUrl() {
        return slipUrl;
    }

    public void setSlipUrl(String slipUrl) {
        this.slipUrl = slipUrl;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public int getAmount() {
        return amount;
    }

    public int getCardId() {
        return cardId;
    }

    public int getBalance() {
        return balance;
    }

    public int getOperatorType() {
        return operatorType;
    }

    public String getDetail() {
        return detail;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public int getCardMainType() {
        return cardMainType;
    }

    public int getState() {
        return state;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMethod() {
        return method;
    }

    public String getPayDate() {
        return payDate;
    }

    public JPushEntity() {
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public void setCardMainType(int cardMainType) {
        this.cardMainType = cardMainType;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setOperatorType(int operatorType) {
        this.operatorType = operatorType;
    }

    @Override
    public String toString() {
        return "JPushEntity{" +
                "amount=" + amount +
                ", cardId=" + cardId +
                ", balance=" + balance +
                ", operatorType=" + operatorType +
                ", integral=" + integral +
                ", detail='" + detail + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", cardMainType=" + cardMainType +
                ", state=" + state +
                ", errorCode=" + errorCode +
                ", method='" + method + '\'' +
                ", payDate='" + payDate + '\'' +
                ", slipUrl='" + slipUrl + '\'' +
                ", receiptUrl='" + receiptUrl + '\'' +
                ", invoiceUrl='" + invoiceUrl + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.amount);
        dest.writeInt(this.cardId);
        dest.writeInt(this.balance);
        dest.writeInt(this.operatorType);
        dest.writeInt(this.integral);
        dest.writeString(this.detail);
        dest.writeString(this.orderNo);
        dest.writeInt(this.cardMainType);
        dest.writeInt(this.state);
        dest.writeInt(this.errorCode);
        dest.writeString(this.method);
        dest.writeString(this.payDate);
        dest.writeString(this.slipUrl);
        dest.writeString(this.receiptUrl);
        dest.writeString(this.invoiceUrl);
    }

    protected JPushEntity(Parcel in) {
        this.amount = in.readInt();
        this.cardId = in.readInt();
        this.balance = in.readInt();
        this.operatorType = in.readInt();
        this.integral = in.readInt();
        this.detail = in.readString();
        this.orderNo = in.readString();
        this.cardMainType = in.readInt();
        this.state = in.readInt();
        this.errorCode = in.readInt();
        this.method = in.readString();
        this.payDate = in.readString();
        this.slipUrl = in.readString();
        this.receiptUrl = in.readString();
        this.invoiceUrl = in.readString();
    }

    public static final Creator<JPushEntity> CREATOR = new Creator<JPushEntity>() {
        @Override
        public JPushEntity createFromParcel(Parcel source) {
            return new JPushEntity(source);
        }

        @Override
        public JPushEntity[] newArray(int size) {
            return new JPushEntity[size];
        }
    };
}
