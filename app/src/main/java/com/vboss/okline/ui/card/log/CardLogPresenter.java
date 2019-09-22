package com.vboss.okline.ui.card.log;

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
 * Date  : 2017/4/6 15:24 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

class CardLogPresenter implements CardLogContact.ICardLogPresenter {
    private static final String TAG = CardLogPresenter.class.getSimpleName();
    private CardRepository cardRepository;
    private CardLogContact.ICardLogView cardLogView;

    CardLogPresenter(Context context, CardLogContact.ICardLogView cardLogView) {
        this.cardLogView = cardLogView;
        cardRepository = CardRepository.getInstance(context);

    }

    @Override
    public void queryCardInfo(int cardType, int cardId) {
        Log.e(TAG, "queryCardInfo begin cardType " + cardType + "---cardId " + cardId);
        cardRepository.cardDetail(cardType, cardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<CardEntity>(TAG) {
                    @Override
                    public void onNext(CardEntity cardEntity) {
                        super.onNext(cardEntity);
                        Log.e(TAG, "cardEntity is :" + (cardEntity == null ? "" : cardEntity.toString()));
                        cardLogView.updateCardDetail(cardEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        cardLogView.requestTimeOut(throwable, 0);
                    }
                });
    }

    @Override
    public void queryCardLog(int cardType, String cardNo, String date, int page, int count) {
        cardRepository.getOrSearchCardLog(cardType, cardNo, "", date, page, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<CardLog>>(TAG) {
                    @Override
                    public void onNext(List<CardLog> cardLogs) {
                        super.onNext(cardLogs);
                        Log.i(TAG, "cardLogs size is :" + (cardLogs == null ? 0 : cardLogs.size()));
                        cardLogView.updateData(cardLogs);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        //request failed deal
                        cardLogView.requestFailed(throwable);
                    }
                });

    }
}
