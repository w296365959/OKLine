package com.vboss.okline.ui.card.notice;

import android.content.Context;

import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.CardRepository;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/4/11 9:54 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

class CardNoticePresenter implements CardNoticeContact.ICardNoticePresenter, CardNoticeContact.IQuickPassPresenter {
    private static final String TAG = CardNoticePresenter.class.getSimpleName();
    private CardNoticeContact.ICardNoticeView cardPayView;
    private CardNoticeContact.IQuickPassView quickPassView;
    private CardRepository cardRepository;
    private Context mContext;

    CardNoticePresenter(Context context, CardNoticeContact.ICardNoticeView payView) {
        cardRepository = CardRepository.getInstance(context);
        this.cardPayView = payView;
        this.mContext = context;
    }

    CardNoticePresenter(Context context, CardNoticeContact.ICardNoticeView payView, CardNoticeContact.IQuickPassView quickPassView) {
        cardRepository = CardRepository.getInstance(context);
        this.quickPassView = quickPassView;
        this.cardPayView = payView;
        this.mContext = context;
    }


    @Override
    public void recharge(String merOrderNo, String merNo, long amount, CardEntity cardEntity) {
        // recharge parameter
        String cardId = String.valueOf(cardEntity.cardId());
        String orderAmount = String.valueOf(amount * 100);
        java.text.DateFormat format = new SimpleDateFormat("yyyyMMddmmssSSS", Locale.CHINA);
        String orderNo = format.format(new Date());
        String tn = "";
        String orderDesc = cardEntity.cardName() + String.format(mContext.getResources().getString(R.string.card_orderDesc),
                TextUtils.formatMoney(amount * 100));
        User user = UserRepository.getInstance(mContext).getUser();
        String olNo;
        if (user != null) {
            olNo = user.getOlNo();
        } else {
            olNo = "OLHZ310571000000000436";
        }
        String name = "郑军";
        String mobile = "17681821398";
        String payFee = String.valueOf(amount * 100);
        int cardType = cardEntity.cardMainType();
        //system out print recharge parameter
        Timber.tag(TAG).i("card no balance recharge parameter ：" +
                        "\ncardId %s \norderAmount %s \norderNo %s \ntn %s \norderDesc %s " +
                        "\nolNo %s \nname %s \nmobile %s \npayFee %s \ncardMainType %s ", cardId,
                orderAmount, orderNo, tn, orderDesc, olNo, name, mobile, payFee, cardType);

        /* ** card no enough balance and recharge ** */
        Utils.startPayment(mContext, cardId, orderNo, tn, orderAmount, merNo, orderDesc, true,
                CardNoticeFragment.RECHAREGE_REQUEST_CODE, cardType, olNo, name, mobile, payFee);
    }

    @Override
    public void queryCardDetail(int cardType, int cardId) {
        cardRepository.cardDetail(cardType, cardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<CardEntity>(TAG) {
                    @Override
                    public void onNext(CardEntity cardEntity) {
                        super.onNext(cardEntity);
                        Timber.tag(TAG).i("cardEntity %s", cardEntity);
                        cardPayView.updateCardDetail(cardEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Timber.tag(TAG).e("error %s", throwable.toString());
                    }
                });
    }

    @Override
    public void queryOrderInfo(int cardType, int cardId, String orderNo, String date, String tag) {
        cardRepository.queryCardLogByOrderNo(cardType, cardId, orderNo, date, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<CardLog>(TAG) {
                    @Override
                    public void onNext(CardLog cardLog) {
                        super.onNext(cardLog);
                        Timber.tag(TAG).i("cardLog %s", cardLog);
                        cardPayView.updateTradeInfo(cardLog);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Timber.tag(TAG).e("error %s", throwable.toString());
                    }
                });
    }

    @Override
    public void queryQuickPassCards() {
        cardRepository.cardList(CardType.BANK_CARD)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<CardEntity>>(TAG) {
                    @Override
                    public void onNext(List<CardEntity> cardEntities) {
                        super.onNext(cardEntities);
                        Timber.tag(TAG).i("cardEntities size %s", cardEntities.size());
                        if (quickPassView != null) {
                            ArrayList<CardEntity> quickPassEntities = new ArrayList<>();
                            for (int i = 0; i < cardEntities.size(); i++) {
                                if (2 == cardEntities.get(i).isQuickPass()) {
                                    quickPassEntities.add(cardEntities.get(i));
                                }
                            }
                            quickPassView.showQuickPassCard(quickPassEntities);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Timber.tag(TAG).e("error %s", throwable.toString());
                    }
                });
    }
}
