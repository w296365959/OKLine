package com.vboss.okline.data.entities;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/5/23 <br/>
 * Summary : 收付款返回类型实体
 */
public class PaymentsOrder {

    private long amount;
    private String fOlNo;
    private String imgUrl;
    private String realName;
    private int relationState;

    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 交易金额
     */
    public long getAmount() {
        return amount;
    }

    /**
     * 收款人欧乐号码
     */
    public String getPayeeOlNo() {
        return fOlNo;
    }

    /**
     * 收款人头像
     */
    public String getAvatar() {
        return imgUrl;
    }

    /**
     * 收款人真是姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 好友关系
     * <p>
     * 双边关系状态：
     * 1,对方不是欧乐会员,
     * 2,对方是欧乐会员, 单方面加好友,
     * 3,对方是欧乐会员,同时是好友
     */
    public int getRelationState() {
        return relationState;
    }
}
