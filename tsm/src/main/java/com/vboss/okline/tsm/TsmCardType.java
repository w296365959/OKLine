package com.vboss.okline.tsm;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/5/6 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public interface TsmCardType {

    /**
     * 交通卡
     */
    int CARD_TYPE_TRANSPORT = 1;
    /**
     * 电子钱包
     */
    int CARD_TYPE_PBOC = 2;
    /**
     * 会员卡
     */
    int CARD_TYPE_VIP = 3;
}
