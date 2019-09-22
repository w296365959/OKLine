package com.vboss.okline.ui.contact.bean;

import java.io.Serializable;

import static android.R.attr.accountType;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/6/13 19:44
 * Desc :
 */

public class AccountItem implements Serializable{

    /**
     * 户名
     */
    private String cardName;
    /**
     * 帐号
     */
    private String cardNo;
    /**
     * 银行图片
     */
    private String bankIcon;

    /**
     * 银行名称
     */

    private String bankName;



    private int accountType; // 0 是绑定账户 1 是手动添加账户

    @Override
    public String toString() {
        return "AccountItem{" +
                "cardName='" + cardName + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", bankIcon='" + bankIcon + '\'' +
                ", bankName='" + bankName + '\'' +
                ", accountType=" + accountType +
                '}';
    }

    public String getName() {
        return cardName;
    }

    public void setName(String name) {
        this.cardName = name;
    }

    public String getAccount() {
        return cardNo;
    }

    public void setAccount(String account) {
        this.cardNo = account;
    }

    public String getIcon() {
        return bankIcon;
    }

    public void setIcon(String icon) {
        this.bankIcon = icon;
    }

    public String getBank() {
        return bankName;
    }

    public void setBank(String bank) {
        this.bankName = bank;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }




}
