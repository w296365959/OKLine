package com.vboss.okline.jpush;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/5/14 15:37 <br/>
 * Summary  : 卡片操作类型
 */

public class OperatorType {
    public static final int ORDER_SYN = 100000;            //订单同步
    public static final int DEBIT = 100001;                //扣款
    public static final int RECHARGE = 100002;            //充值
    public static final int OPEN = 100003;            //开卡
    public static final int REVOKE = 100004;            //消费撤销
    public static final int REFUND = 100005;            //退款
    public static final int C2C_TRANS = 100006;            //C2C转账
    public static final int INTEGRAL_DEBIT = 100007;            //积分消费
    public static final int INTEGRAL_ADD = 100008;            //积分增加
}
