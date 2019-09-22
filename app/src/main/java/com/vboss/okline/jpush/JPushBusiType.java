package com.vboss.okline.jpush;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/5/13 <br/>
 * Summary : JPush推送的业务类型
 */
public class JPushBusiType {
    /**
     * 订单同步
     */
    public static final int ORDER_SYN = 100000;
    /**
     * 扣款
     */
    public static final int DEBIT = 100001;
    /**
     * 充值
     */
    public static final int RECHARGE = 100002;
    /**
     * 开卡
     */
    public static final int OPEN = 100003;
    /**
     * 消费撤销
     */
    public static final int REVOKE = 100004;
    /**
     * 退款
     */
    public static final int REFUND = 100005;
    /**
     * C2C转账
     */
    public static final int C2C_TRANS = 100006;
    /**
     * 积分消费
     */
    public static final int INTEGRAL_DEBIT = 100007;
    /**
     * 积分增加
     */
    public static final int INTEGRAL_ADD = 100008;            //积分增加
}
