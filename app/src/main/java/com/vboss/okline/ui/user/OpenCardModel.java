package com.vboss.okline.ui.user;

import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.CardRepository;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.CardCondition;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.user.callback.CardOpenCallback;
import com.vboss.okline.ui.user.callback.OpenCardConditionsListener;
import com.vboss.okline.ui.user.callback.UserFetchListener;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/4/12
 * Summary : 在这里描述Class的主要功能
 */

public class OpenCardModel implements OpenCardContract.Model {

    private final OpenCardActivity openCardActivity;
    private final CardRepository cardRepository;
    private static final String TAG = "OpenCardModel";
    private final UserRepository userRepository;
    private Subscription installCardApp;
    private Subscription getCardOpenCondition;

    public OpenCardModel(OpenCardActivity openCardActivity) {
        this.openCardActivity = openCardActivity;
        cardRepository = CardRepository.getInstance(this.openCardActivity);
        userRepository = UserRepository.getInstance(openCardActivity);
    }

    @Override
    public void startCardOpening(int tsmCardType, String aid, String orderNo, final CardOpenCallback cardOpenCallback) {
        Utils.showLog(TAG,"发起开卡：tsmCardType = [" + tsmCardType + "], aid = [" + aid + "], orderNo = [" + orderNo + "], cardOpenCallback = [" + cardOpenCallback + "]");

        installCardApp = userRepository.installCardApp(openCardActivity, tsmCardType, aid, orderNo)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<CardEntity>(TAG) {
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        String message = throwable.getMessage();
                        if (throwable instanceof NullPointerException) {
                            message = "蓝牙连接异常";
                        }
                        cardOpenCallback.onExceptionCaught(message);
                    }

                    @Override
                    public void onNext(CardEntity cardEntity) {
                        super.onNext(cardEntity);
                        Utils.showLog(TAG, "开卡成功OpenCardModel.onNext:" + cardEntity.toString());
                        Utils.showLog(TAG,"开卡成功之后解除订阅！");
                        Utils.unsubscribeRxJava(installCardApp);
                        cardOpenCallback.onProcedureFinished(cardEntity);
                    }
                });
    }

    @Override
    public void getPersonalInfo(UserFetchListener listener) {
        User user = null;
        try {
            user = userRepository.getUser();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (user != null) {
            listener.onFetch(user);
        } else {
            Utils.showLog(TAG,"userRepository.getUser()   ：  null");
//            listener.onFail("无法获取用户信息，请稍后再试");
            User user1 = new User("17681821398", "张三", "", "421098198808028852", null, null, null);
            user1.setIdcardNo("421098198808028852");
            listener.onFetch(user1);
        }
    }

    @Override
    public void getOpenCardConditions(int cardMainType, String aid, final OpenCardConditionsListener openCardConditionsListener) {
        getCardOpenCondition = userRepository.getCardOpenCondition(cardMainType, aid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<CardCondition>(TAG) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        openCardConditionsListener.onFail(throwable.getMessage());
                    }

                    @Override
                    public void onNext(CardCondition cardCondition) {
                        super.onNext(cardCondition);
                        if (cardCondition != null) {
                            Utils.showLog(TAG, "通过服务器获取到的开卡条件：" + cardCondition.toString());
                            openCardConditionsListener.onFetch(cardCondition);
                        } else {
                            String message = "通过服务器获取到的开卡条件对象为空";
                            Utils.showLog(TAG, message);
                            openCardConditionsListener.onFail(message);
                        }
                    }
                });
    }

    @Override
    public void onViewDestroy() {
        Utils.unsubscribeRxJava(installCardApp);
        Utils.unsubscribeRxJava(getCardOpenCondition);
    }
}
