package com.vboss.okline.ui.user;

import com.vboss.okline.base.BaseModel;
import com.vboss.okline.base.BasePresenter;
import com.vboss.okline.base.BaseView;
import com.vboss.okline.data.CardFlavor;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.user.callback.CardEntityFetchListener;
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

public interface OpenCardContract {
    interface View extends BaseView {

        /**
         * 显示开卡用身份信息
         * @param user    用户实例
         */
        void showPersonalInfo(User user);

        /**
         * 在界面上Toast一下
         * @param message 消息内容
         */
        void showToast(String message);
    }

    abstract class Presenter extends BasePresenter {
        abstract void startCardOpening(int tsmCardType, String aid, String orderNo, CardOpenCallback cardOpenCallback);

        abstract void getPersonalInfo();

        abstract void getOpenCardConditions(int cardMainType, String aid, OpenCardConditionsListener openCardConditionsListener);
        abstract void onViewDestroy();
    }

    interface Model extends BaseModel {
        void startCardOpening(int tsmCardType, String aid, String orderNo, CardOpenCallback cardOpenCallback);

        void getPersonalInfo(UserFetchListener listener);
        void getOpenCardConditions(int cardMainType, String aid, OpenCardConditionsListener openCardConditionsListener);
        void onViewDestroy();
    }
}
