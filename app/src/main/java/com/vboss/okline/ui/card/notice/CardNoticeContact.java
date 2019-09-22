package com.vboss.okline.ui.card.notice;

import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardLog;

import java.util.ArrayList;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/4/8 10:02 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

interface CardNoticeContact {

    interface ICardNoticeView {
        /**
         * update card detail info
         *
         * @param cardEntity CardEntity
         */
        void updateCardDetail(CardEntity cardEntity);

        /**
         * update card Card Order detail info
         *
         * @param cardLog CardLog
         */
        void updateTradeInfo(CardLog cardLog);
    }

    interface ICardNoticePresenter {
        /**
         * card no enough balance recharge
         *
         * @param merOrderNo merchant Order Number
         * @param merNo      merchant number
         * @param amount     amount money
         * @param cardEntity CardEntity card detail info
         */
        void recharge(String merOrderNo, String merNo, long amount, CardEntity cardEntity);

        /**
         * query card detail info
         *
         * @param cardType int card main type
         * @param cardId   int card id
         */
        void queryCardDetail(int cardType, int cardId);


        /**
         * query card trade order detail info
         *
         * @param cardType int card main type
         * @param cardId   int card id
         * @param orderNo  String orderNo
         * @param date     String date for example 2017-04-24
         * @param tag      String tag
         */
        void queryOrderInfo(int cardType, int cardId, String orderNo, String date, String tag);
    }

    /**
     * Added by wangshuai 2017-05-14
     * query all QuickPass card
     */
    interface IQuickPassPresenter {
        /**
         * Added by wangshuai 2017-05-14
         * query all QuickPass card
         */
        void queryQuickPassCards();
    }

    /**
     * Added by wangshuai 2017-05-14
     * show all QuickPass card
     */
    interface IQuickPassView {
        /**
         * show all QuickPass card
         *
         * @param quickPass ArrayList<CardEntity>
         *                  {@link CardEntity}
         */
        void showQuickPassCard(ArrayList<CardEntity> quickPass);
    }
}
