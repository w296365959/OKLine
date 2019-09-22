package com.okline.vboss.assistant.net;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/30 <br/>
 * Summary : 卡证类型实体类
 */
public class CardType {
    /**
     * 银行卡
     */
    public static final int BANK_CARD = 100;
    /**
     * 交通卡
     */
    public static final int TRANS_CARD = 200;
    /**
     * 会员卡
     */
    public static final int VIP_CARD = 300;
    /**
     * 门禁卡
     */
    public static final int DOOR_CARD = 400;
    /**
     * 票务
     */
    public static final int TICKET = 500;
    /**
     * 常用卡
     */
    public static final int COMMON_CARD = 600;
    /**
     * 工作证
     */
    public static final int EMPLOYEE_CARD = 700;
    /**
     * 证件
     */
    public static final int CREDENTIALS = 800;

    /**
     * 全部类型
     */
    public static final int ALL = -1;


    /**
     * 类型列表的排序序号
     */
    private int sortNo;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 卡证类型值
     */
    private int cardMainType;

    public int getSortNo() {
        return sortNo;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getCardMainType() {
        return cardMainType;
    }

    @Override
    public String toString() {
        return "CardType{" +
                "sortNo=" + sortNo +
                ", typeName='" + typeName + '\'' +
                ", cardMainType=" + cardMainType +
                '}';
    }
}
