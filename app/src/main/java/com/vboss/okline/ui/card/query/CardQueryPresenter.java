package com.vboss.okline.ui.card.query;

import android.content.Context;
import android.util.Log;

import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.CardRepository;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardLog;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/4/6 15:31 <br/>
 * Summary  : 卡证相关查询操作
 */

class CardQueryPresenter implements CardQueryContact.ICardQueryPresenter {
    private static final String TAG = CardQueryPresenter.class.getSimpleName();
    private CardQueryContact.ICardQueryView queryView;
    private CardRepository cardRepository;

    CardQueryPresenter(Context context, CardQueryContact.ICardQueryView cardQueryView) {
        this.queryView = cardQueryView;
        cardRepository = CardRepository.getInstance(context);
    }


    @Override
    public void queryCards(String merchantName) {
        try {
            cardRepository.searchCard(merchantName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultSubscribe<List<CardEntity>>(TAG) {
                        @Override
                        public void onNext(List<CardEntity> cardEntities) {
                            super.onNext(cardEntities);
                            Log.i(TAG, "cardEntities size :" + (cardEntities == null ? 0 : cardEntities.size()));
                            queryView.updateCards(cardEntities);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            queryView.requestTimeOut(throwable, CardQueryContact.MODE_QUERY_CARD);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void queryCardLogs(int cardType, String cardNo, String merName, int page, int pageSize) {
        cardRepository.getOrSearchCardLog(cardType, cardNo, merName, "", page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<CardLog>>(TAG) {
                    @Override
                    public void onNext(List<CardLog> cardLogs) {
                        super.onNext(cardLogs);
                        Log.i(TAG, "cardLogs size is :" + (cardLogs == null ? 0 : cardLogs.size()));
                        queryView.updateCardLogs(cardLogs);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        queryView.requestTimeOut(throwable, CardQueryContact.MODE_QUERY_CARD_LOG);
                        //Added by wangshuai 2017-04-26
                        queryView.updateCardLogsFailed(throwable);
                    }
                });
    }
}
