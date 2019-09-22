package com.vboss.okline.ui.user;

import android.content.Context;

import com.vboss.okline.base.BasePresenter;
import com.vboss.okline.base.BaseView;
import com.vboss.okline.data.entities.OKCard;
import com.vboss.okline.ui.user.customized.CustomDialog;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/3/30
 * Summary : 欧卡添加模块Contract
 */

public interface OCardAttachContract {
    interface View extends BaseView {
        void executeAttaching(int i, boolean b);
        void enableNFC();
        void showDialog(String message, String subMessage, boolean cancelEnabled,CustomDialog.DialogClickListener listener);

        Context getContext();

        void saveCardInfo(OKCard okCard);
    }

    abstract class Presenter<View> extends BasePresenter{
        abstract void detectNFC();
        abstract void detectBT();
        abstract void detectOCard(String number);
        abstract void onViewDestroy();
        abstract void matchOCardNo(String number);
        void scanOCard(int timeout,String filter){};
    }
}
