package com.vboss.okline.ui.contact.c2cPart;

import com.vboss.okline.base.BaseModel;
import com.vboss.okline.base.BasePresenter;
import com.vboss.okline.base.BaseView;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/4/10 21:37
 * Desc :
 */

public interface C2Contract {
    interface Model extends BaseModel {

    }
    interface View extends BaseView {
        void transfer();
    }

    abstract class C2CPresenter extends BasePresenter<C2Contract.Model,C2Contract.View> {
        public abstract void transfer(int amount, String destOlNo);
        public abstract String getMyCount();
    }
}
