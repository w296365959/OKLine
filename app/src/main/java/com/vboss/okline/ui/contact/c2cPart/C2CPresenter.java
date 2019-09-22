package com.vboss.okline.ui.contact.c2cPart;

import android.content.Context;

import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.CardRepository;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardType;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/4/10 20:48
 * Desc :
 */

public class C2CPresenter extends C2Contract.C2CPresenter {
    private static final String TAG = "C2CPresenter";

    C2Contract.View mView;
    C2Contract.Model mModel;
    Context context;
    private UserRepository repository;
    private String name;

    public C2CPresenter(C2Contract.View view,C2Contract.Model model,Context context){
        onAttached();
        mView = view;
        mModel = model;
        this.context = context;
    }

    @Override
    public void onAttached() {
        repository = UserRepository.getInstance(context);
    }

    @Override
    public void transfer(int amount, String destOlNo) {
        //lzb changed 2017/5/7 接口更改 此页面现在废弃,暂时tipscode传0
        repository.c2cCash(amount,destOlNo,0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<Boolean>(TAG){
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean){
                            mView.transfer();
                        }
                    }
                });
    }

    @Override
    public String getMyCount() {
        CardRepository.getInstance(context)
                .getTopCard(CardType.BANK_CARD)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<CardEntity>(TAG){
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }

                    @Override
                    public void onNext(CardEntity entity) {
                        String cardName = entity.cardName();
                        String cardNo = entity.cardNo();
                        if (null != cardNo){
                            name = cardName+"(尾号"+cardNo.substring(cardNo.length() - 4);
                        }
                    }
                });

        Timber.tag(TAG).i("getMyCount:"+name);
        return name == null ? "" : name;
    }
}
