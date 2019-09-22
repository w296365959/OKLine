package com.vboss.okline.ui.card.notice;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.jpush.JPushEntity;

import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/4/24 14:52 <br/>
 * Summary  : card notice helper
 */

class CardNoticeHelper {
    private static final String TAG = CardNoticeHelper.class.getSimpleName();
    static final int CARD_NO_BALANCE = 3078;    //余额不足;
    //jpush key Added by wangshuai 2017-06-14
    static final String KEY_CARD = "card";
    //Added by wangshuai 2017-06-15 SharedPreferences
    private static final String SP_NAME = "card_notice";
    private static final String KEY_MODE_SILENT = "mode_silent";

    //Added by wangshuai 2017-06-14
    static final int MODE_BANK_PAY = 0x01;
    static final int MODE_BANK_PAY_SUCCESS = 0x02;
    static final int MODE_BANK_PAY_FAILED = 0x06;
    static final int MODE_MEMBER_PAY_SUCCESS = 0x04;
    static final int MODE_MEMBER_PAY_FAILED = 0x05;
    static final int MODE_BUS_PAY_SUCCESS = 0x07;
    static final int MODE_BUS_PAY_FAILED = 0x08;
    static final int MODE_BALANCE_FAILED = 0x09;
    static final int RECHARGE_REQUEST_CODE = 0x011;


    /**
     * get pay mode
     *
     * @param entity JPushEntity
     * @return mode
     */
    int getMode(JPushEntity entity) {
        String orderNo = entity.getOrderNo();
        int state = entity.getState();
        if (TextUtils.isEmpty(orderNo)) {
            //start pay card
            switch (entity.getCardMainType()) {
                case CardType.BANK_CARD:
                    return CardNoticeFragment.MODE_BANK_PAY;
                case CardType.VIP_CARD:
                case CardType.COMMON_CARD:
                case CardType.TRANS_CARD:
                    return CardNoticeFragment.MODE_MEMBER_PAY;
                default:
                    return CardNoticeFragment.MODE_BANK_PAY;
            }
        }
        //pay card result
        switch (entity.getCardMainType()) {
            case CardType.BANK_CARD:
                return CardNoticeFragment.MODE_BANK_PAY_SUCCESS;
            case CardType.VIP_CARD:
            case CardType.COMMON_CARD:
            case CardType.TRANS_CARD:
                return state == 0 ? CardNoticeFragment.MODE_MEMBER_PAY_FAILED : CardNoticeFragment.MODE_MEMBER_PAY_SUCCESS;
            default:
                break;
        }
        return 0;
    }

    /**
     * get pay state
     *
     * @param entity {@link JPushEntity}
     * @return int
     * {@link CardNoticeFragment_1#MODE_BANK_PAY_SUCCESS}
     * {@link CardNoticeFragment_1#MODE_BANK_PAY_FAILED}
     * {@link CardNoticeFragment_1#MODE_BUS_PAY_SUCCESS}
     * {@link CardNoticeFragment_1#MODE_BUS_PAY_FAILED}
     * {@link CardNoticeFragment_1#MODE_MEMBER_PAY_SUCCESS}
     * {@link CardNoticeFragment_1#MODE_MEMBER_PAY_FAILED}
     */
    int getPayMode(JPushEntity entity) {
        //modify by wangshuai 2017-05-31 card pay mode
        if (TextUtils.isEmpty(entity.getOrderNo())) {
            Timber.tag(TAG).i(" pay card");
            if (CARD_NO_BALANCE == entity.getErrorCode() && CardType.VIP_CARD == entity.getCardMainType()) {
                return CardNoticeFragment_1.MODE_MEMBER_PAY_FAILED;
            }
            if (CARD_NO_BALANCE == entity.getErrorCode() &&
                    CardType.COMMON_CARD == entity.getCardMainType() ||
                    CardType.TRANS_CARD == entity.getCardMainType()) {
                return CardNoticeFragment_1.MODE_BUS_PAY_FAILED;
            }
            return CardNoticeFragment_1.MODE_BANK_PAY;
        }
        if (entity.getCardMainType() == CardType.BANK_CARD) {
            if (entity.getState() == 1) {
                return CardNoticeFragment_1.MODE_BANK_PAY_SUCCESS;
            }
            return CardNoticeFragment_1.MODE_BANK_PAY_FAILED;
        }
        if (entity.getCardMainType() == CardType.VIP_CARD) {
            if (entity.getState() == 1) {
                return CardNoticeFragment_1.MODE_MEMBER_PAY_SUCCESS;
            } else if (entity.getState() == 0 && entity.getErrorCode() == CARD_NO_BALANCE) {
                //会员卡余额不足，
                return CardNoticeFragment_1.MODE_BUS_PAY_FAILED;
            }
            return CardNoticeFragment_1.MODE_MEMBER_PAY_FAILED;
        }
        if (entity.getCardMainType() == CardType.COMMON_CARD || entity.getCardMainType() == CardType.TRANS_CARD) {
            if (entity.getState() == 1) {
                return CardNoticeFragment_1.MODE_BUS_PAY_SUCCESS;
            }
            return CardNoticeFragment_1.MODE_BUS_PAY_FAILED;
        }
        return CardNoticeFragment_1.MODE_BANK_PAY;
    }


    /**
     * Added by wangshuai 2017-06-15
     * <p>
     * voice is silent mode
     *
     * @param context Context
     * @return boolean
     */
    static boolean isSilent(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_MODE_SILENT, false);
    }

    /**
     * Added by wangshuai 2017-06-15
     * <p>
     * save voice silent mode
     *
     * @param context Context
     * @param silent  boolean
     */
    static void setSilent(Context context, boolean silent) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_MODE_SILENT, silent).apply();
    }
}
