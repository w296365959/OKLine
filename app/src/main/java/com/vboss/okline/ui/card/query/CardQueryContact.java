package com.vboss.okline.ui.card.query;

import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardLog;

import java.util.List;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/4/6 14:50 <br/>
 * Summary  : card model query's interface
 */

public interface CardQueryContact {

    int MODE_QUERY_CARD = 0x01;
    int MODE_QUERY_CARD_LOG = 0x02;


    interface ICardQueryView {

        /**
         * update query cards list
         *
         * @param data cardEntity list
         */
        void updateCards(List<CardEntity> data);

        /**
         * update card logs list
         *
         * @param data CardLog list
         */
        void updateCardLogs(List<CardLog> data);

        /**
         * SocketTimeOutException deal
         *
         * @param methodFlag int method flag
         */
        void requestTimeOut(Throwable throwable, int methodFlag);

        /**
         * Added by wangshuai 2017-04-26
         * the method when request data happen exception call
         *
         * @param throwable Throwable exception
         */
        void updateCardLogsFailed(Throwable throwable);

    }

    interface ICardQueryPresenter {
        /**
         * search all card
         *
         * @param cardName card name or query key
         */
        void queryCards(String cardName);

        /**
         * query card's logs
         *
         * @param cardType card type
         * @param cardNo   card number
         * @param merName  merchant name or query key
         * @param page     int page
         * @param pageSize int page count
         */
        void queryCardLogs(int cardType, String cardNo, String merName, int page, int pageSize);
    }
}
