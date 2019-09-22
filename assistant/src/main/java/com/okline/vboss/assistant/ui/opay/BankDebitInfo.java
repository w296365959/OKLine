package com.okline.vboss.assistant.ui.opay;

import java.io.Serializable;

/**
 * Created by zhengjun on 16/12/22.
 */

public class BankDebitInfo implements Serializable {
    public static final String BANK_DEBIT_INFO = "bank_debit_info";
    private int cardType;
    private String orderAmount,cardId,cardName,cardNo,orderNumber,merNo,tn,orderDesc,orderDate;
    private boolean isCardSelected,showResultPage;

    public BankDebitInfo(String orderAmount, int cardType, String cardId, String cardName, String cardNo, String orderNumber, String merNo, String tn, String orderDesc, boolean isCardSelected, boolean showResultPage) {
        this.orderAmount = orderAmount;
        this.cardType = cardType;
        this.cardId = cardId;
        this.cardName = cardName;
        this.cardNo = cardNo;
        this.orderNumber = orderNumber;
        this.merNo = merNo;
        this.tn = tn;
        this.orderDesc = orderDesc;
        this.showResultPage = showResultPage;
        this.isCardSelected = isCardSelected;
    }

    public boolean isCardSelected() {
        return isCardSelected;
    }

    public boolean isShowResultPage() {
        return showResultPage;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public int getCardType() {
        return cardType;
    }

    public String getCardId() {
        return cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getMerNo() {
        return merNo;
    }

    public String getTn() {
        return tn;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public String getOrderDate() {
        return orderDate;
    }

    @Override
    public String toString() {
        return "BankDebitInfo{" +
                "orderAmount='" + orderAmount + '\'' +
                ", cardType=" + cardType +
                ", cardId='" + cardId + '\'' +
                ", cardName='" + cardName + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", merNo='" + merNo + '\'' +
                ", tn='" + tn + '\'' +
                ", orderDesc='" + orderDesc + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", isCardSelected=" + isCardSelected +
                ", showResultPage=" + showResultPage +
                '}';
    }
}
