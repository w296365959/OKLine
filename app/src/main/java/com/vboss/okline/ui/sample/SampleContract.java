package com.vboss.okline.ui.sample;

import com.vboss.okline.base.BaseModel;
import com.vboss.okline.base.BasePresenter;
import com.vboss.okline.base.BaseView;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/28 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public interface SampleContract {

    interface Model extends BaseModel {

    }

    interface View extends BaseView {

    }

    abstract class Presenter extends BasePresenter<Model, View> {

    }
}
