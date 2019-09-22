package com.vboss.okline.ui.user;

import com.vboss.okline.base.BasePresenter;
import com.vboss.okline.base.BaseView;
import com.vboss.okline.ui.user.callback.OCadAbsenceDeclarationListener;
import com.vboss.okline.ui.user.callback.OCardApplyNewListener;
import com.vboss.okline.ui.user.callback.OCardInfoListener;
import com.vboss.okline.ui.user.callback.OCardResumeListener;
import com.vboss.okline.ui.user.callback.OKCardListener;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/3/30
 * Summary : 欧卡页面Contract
 */

public interface OCardContract {
    interface View extends BaseView{
        void initUI();
    }

    abstract class Presenter extends BasePresenter{
        abstract void getOCardInfo(OCardInfoListener okCardListener);
        abstract void declareAbsence(int deviceType, String deviceNo,OCadAbsenceDeclarationListener listener);
        abstract void resume(int deviceType, String deviceNo,OCardResumeListener listener);
        abstract void onViewDestroy();
    }
}
