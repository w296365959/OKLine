package com.okline.vboss.assistant.net;

import java.io.Serializable;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/6/13 18:55 <br/>
 * Summary  : recharge info
 */

public class RechargeEntity implements Serializable {


    //充值卡名称
    private String recharge_card_name;
    //充值金额
    private int recharge_amount;
    //赠送金额
    private int give_amount;
    //实际到账金额
    private int recharge_real_amount;
    //支付金额
    private int recharge_pay_amount;
    //支付方式
    private String recharge_payment;
    //充值日期
    private String recharge_date;
    //充值银行卡url
    private String bank_card_url;
    //被充值卡url
    private String card_url;
    //交易流水号(订单号)
    private String tn;


    /**
     * @param recharge_card_name   充值卡名称
     * @param recharge_amount      充值金额
     * @param give_amount          赠送金额
     * @param recharge_real_amount 实际到账金额
     * @param recharge_pay_amount  支付金额
     * @param recharge_payment     支付方式
     * @param recharge_date        充值日期
     * @param bank_card_url        充值银行卡url
     * @param card_url             被充值卡url
     * @param tn                   交易流水号
     */
    public RechargeEntity(String recharge_card_name, int recharge_amount, int give_amount,
                          int recharge_real_amount, int recharge_pay_amount, String recharge_payment,
                          String recharge_date, String bank_card_url, String card_url, String tn) {
        this.recharge_card_name = recharge_card_name;
        this.recharge_amount = recharge_amount;
        this.give_amount = give_amount;
        this.recharge_real_amount = recharge_real_amount;
        this.recharge_pay_amount = recharge_pay_amount;
        this.recharge_payment = recharge_payment;
        this.recharge_date = recharge_date;
        this.bank_card_url = bank_card_url;
        this.card_url = card_url;
        this.tn = tn;
    }


    public RechargeEntity() {

    }

    public String getRecharge_card_name() {
        return recharge_card_name;
    }

    public void setRecharge_card_name(String recharge_card_name) {
        this.recharge_card_name = recharge_card_name;
    }

    public int getRecharge_amount() {
        return recharge_amount;
    }

    public void setRecharge_amount(int recharge_amount) {
        this.recharge_amount = recharge_amount;
    }

    public int getGive_amount() {
        return give_amount;
    }

    public void setGive_amount(int give_amount) {
        this.give_amount = give_amount;
    }

    public int getRecharge_real_amount() {
        return recharge_real_amount;
    }

    public void setRecharge_real_amount(int recharge_real_amount) {
        this.recharge_real_amount = recharge_real_amount;
    }

    public int getRecharge_pay_amount() {
        return recharge_pay_amount;
    }

    public void setRecharge_pay_amount(int recharge_pay_amount) {
        this.recharge_pay_amount = recharge_pay_amount;
    }

    public String getRecharge_payment() {
        return recharge_payment;
    }

    public void setRecharge_payment(String recharge_payment) {
        this.recharge_payment = recharge_payment;
    }

    public String getRecharge_date() {
        return recharge_date;
    }

    public void setRecharge_date(String recharge_date) {
        this.recharge_date = recharge_date;
    }

    public String getBank_card_url() {
        return bank_card_url;
    }

    public void setBank_card_url(String bank_card_url) {
        this.bank_card_url = bank_card_url;
    }

    public String getCard_url() {
        return card_url;
    }

    public void setCard_url(String card_url) {
        this.card_url = card_url;
    }

    public String getTn() {
        return tn;
    }

    public void setTn(String tn) {
        this.tn = tn;
    }

    @Override
    public String toString() {
        return "RechargeEntity{" +
                "recharge_card_name='" + recharge_card_name + '\'' +
                ", recharge_amount=" + recharge_amount +
                ", give_amount=" + give_amount +
                ", recharge_real_amount=" + recharge_real_amount +
                ", recharge_pay_amount=" + recharge_pay_amount +
                ", recharge_payment='" + recharge_payment + '\'' +
                ", recharge_date='" + recharge_date + '\'' +
                ", bank_card_url='" + bank_card_url + '\'' +
                ", card_url='" + card_url + '\'' +
                '}';
    }
}
