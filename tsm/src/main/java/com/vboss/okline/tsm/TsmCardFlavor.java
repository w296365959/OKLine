package com.vboss.okline.tsm;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.vboss.okline.tsm.TsmCardType.CARD_TYPE_PBOC;
import static com.vboss.okline.tsm.TsmCardType.CARD_TYPE_TRANSPORT;
import static com.vboss.okline.tsm.TsmCardType.CARD_TYPE_VIP;


/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/5/4 <br/>
 * Summary : 在这里描述Class的主要功能
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({CARD_TYPE_PBOC, CARD_TYPE_TRANSPORT, CARD_TYPE_VIP})
public @interface TsmCardFlavor {
}
