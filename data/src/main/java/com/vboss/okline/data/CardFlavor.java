package com.vboss.okline.data;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.vboss.okline.data.entities.CardType.BANK_CARD;
import static com.vboss.okline.data.entities.CardType.COMMON_CARD;
import static com.vboss.okline.data.entities.CardType.CREDENTIALS;
import static com.vboss.okline.data.entities.CardType.DOOR_CARD;
import static com.vboss.okline.data.entities.CardType.EMPLOYEE_CARD;
import static com.vboss.okline.data.entities.CardType.TICKET;
import static com.vboss.okline.data.entities.CardType.TRANS_CARD;
import static com.vboss.okline.data.entities.CardType.ALL;
import static com.vboss.okline.data.entities.CardType.VIP_CARD;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/31 <br/>
 * Summary : 卡证类型
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({BANK_CARD, TRANS_CARD, VIP_CARD, DOOR_CARD, TICKET, COMMON_CARD, EMPLOYEE_CARD, CREDENTIALS, ALL})
public  @interface CardFlavor {
}
