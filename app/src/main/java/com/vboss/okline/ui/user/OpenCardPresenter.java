package com.vboss.okline.ui.user;

import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.user.callback.CardOpenCallback;
import com.vboss.okline.ui.user.callback.OpenCardConditionsListener;
import com.vboss.okline.ui.user.callback.UserFetchListener;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/4/12
 * Summary : 在这里描述Class的主要功能
 */

public class OpenCardPresenter extends OpenCardContract.Presenter {

    private final OpenCardContract.Model model;
    private final OpenCardContract.View view;

    public OpenCardPresenter(OpenCardContract.Model model, OpenCardContract.View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void onAttached() {

    }

    @Override
    void startCardOpening(int tsmCardType, String aid, String orderNo, CardOpenCallback cardOpenCallback) {
        model.startCardOpening(tsmCardType, aid, orderNo, cardOpenCallback);
    }


    @Override
    void getPersonalInfo() {
        model.getPersonalInfo(new UserFetchListener() {
            @Override
            public void onFetch(User user) {
                view.showPersonalInfo(user);
            }

            @Override
            public void onFail(String message) {
                view.showToast(message);
            }
        });
    }

    @Override
    void getOpenCardConditions(int cardMainType, String aid, OpenCardConditionsListener openCardConditionsListener) {
        model.getOpenCardConditions(cardMainType, aid, openCardConditionsListener);
    }

    @Override
    void onViewDestroy() {
        model.onViewDestroy();
    }
}
