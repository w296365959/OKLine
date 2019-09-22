package com.vboss.okline.ui.card.main;

import com.vboss.okline.data.entities.CardEntity;

import java.util.List;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/3/29 19:34 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

interface CardContact {

    interface ICardView {
        /**
         * 更新卡列表
         *
         * @param data card list
         */
        void updateData(List<CardEntity> data);

    }

    interface IPresenter {

        /**
         * 分类查询卡
         *
         * @param cardType 分类
         */
        void queryCards(int cardType);

        /**
         * 设置默认卡
         *
         * @param cardType int card main type
         * @param cardId   int card id
         */
        void setCardDefault(int cardType, int cardId);
    }
}
