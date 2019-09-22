package com.vboss.okline.ui.card.main;

import android.content.Context;
import android.util.Log;

import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.CardRepository;
import com.vboss.okline.data.entities.CardEntity;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/3/29 19:36 <br/>
 * Summary  : card data request helper class
 */

public class CardPresenter implements CardContact.IPresenter {
    private static final String TAG = CardPresenter.class.getSimpleName();
    private CardContact.ICardView mICardView;
    private CardRepository cardRepository;
    Subscription queryCardSubscription;

    CardPresenter(Context context, CardContact.ICardView cardView) {
        this.mICardView = cardView;
        cardRepository = CardRepository.getInstance(context);
    }

    @Override
    public void queryCards(int cardType) {
        queryCardSubscription = cardRepository.cardList(cardType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<CardEntity>>(TAG) {
                    @Override
                    public void onNext(List<CardEntity> cardEntities) {
                        super.onNext(cardEntities);
                        Timber.tag(TAG).i("queryCards : %s", (cardEntities == null ? 0 : cardEntities.size()));
                        mICardView.updateData(cardEntities);
                    }
                });
    }

    @Override
    public void setCardDefault(final int cardType, int cardId) {
        cardRepository.topCard(cardType, cardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<Boolean>(TAG) {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        Log.i(TAG, "topCard boolean : " + aBoolean);
                        if (aBoolean) {
                            Timber.tag(TAG).i("refresh card sort");
                            queryCards(cardType);
                        }
                    }
                });
    }
}
