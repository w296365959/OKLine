package com.vboss.okline.ui.card.log;

import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardLog;

import java.util.List;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/3/30 19:45 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

interface CardLogContact {

    interface ICardLogView {
        void updateData(List<CardLog> data);

        void updateCardDetail(CardEntity cardEntity);

        /**
         * SocketTimeOutException deal
         *
         * @param methodFlag int method flag
         * @param throwable  Throwable
         */
        void requestTimeOut(Throwable throwable, int methodFlag);

        /**
         * Added by wangshuai 2017-04-26
         * the method when request happened exception call
         *
         * @param throwable Throwable Exception message
         */
        void requestFailed(Throwable throwable);
    }

    interface ICardLogPresenter {
        /**
         * request card information
         *
         * @param cardType card type
         * @param cardId   card id
         */
        void queryCardInfo(int cardType, int cardId);

        /**
         * query card's logs
         *
         * @param cardType card type
         * @param cardNo   card number
         * @param date     time
         */
        void queryCardLog(int cardType, String cardNo, String date, int page, int count);
    }
}
