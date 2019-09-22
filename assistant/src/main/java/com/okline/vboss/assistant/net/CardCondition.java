package com.okline.vboss.assistant.net;

import java.io.Serializable;
import java.util.List;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/11 <br/>
 * Summary : 开卡条件实体类
 */

public class CardCondition implements Serializable {
    /**
     * 是否需要认证
     */
    private int isCert;
    /**
     * 是否需要开卡费
     */
    private int isOpenFee;
    /**
     * 是否需要开发预存
     */
    private int isOpenPre;


    /**
     * 充值金额列表
     */
    private List<Integer> amountList;

    /**
     * 开卡金额列表
     */
    private List<CardFee> cardFeeList;

    public int getIsCert() {
        return isCert;
    }

    public int getIsOpenFee() {
        return isOpenFee;
    }

    public int getIsOpenPre() {
        return isOpenPre;
    }

    public List<Integer> getAmountList() {
        return amountList;
    }

    public List<CardFee> getCardFeeList() {
        return cardFeeList;
    }

    public static class CardFee implements Serializable {
        /**
         * 开卡金额
         */
        private int amount;
        /**
         * 金额备注
         */
        private String memo;

        public int getAmount() {
            return amount;
        }

        public String getMemo() {
            return memo;
        }

        @Override
        public String toString() {
            return "CardFee{" +
                    "amount=" + amount +
                    ", memo='" + memo + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CardCondition{" +
                "isCert=" + isCert +
                ", isOpenFee=" + isOpenFee +
                ", isOpenPre=" + isOpenPre +
                ", amountList=" + amountList +
                ", cardFeeList=" + cardFeeList +
                '}';
    }
}
