package com.vboss.okline.ui.contact.callPhone;

import com.vboss.okline.base.BaseModel;
import com.vboss.okline.base.BasePresenter;
import com.vboss.okline.base.BaseView;
import com.vboss.okline.ui.contact.c2cPart.C2Contract;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/4 19:53
 * Desc :
 */

public interface DialContract  {
    interface Model extends BaseModel {

    }
    interface View extends BaseView {

    }

    abstract class DialPresenter extends BasePresenter<DialContract.Model,DialContract.View> {


    }
}
