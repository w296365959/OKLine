package com.vboss.okline.ui.app.update;

import android.content.Context;

import com.vboss.okline.base.BaseModel;
import com.vboss.okline.base.BasePresenter;
import com.vboss.okline.base.BaseView;
import com.vboss.okline.data.entities.AppEntity;

import java.util.List;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: yuan shaoyu  <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/3/29 <br/>
 * summary  :在这里描述Class的主要功能
 */
 interface UpdateAppContract {
    interface Model extends BaseModel{

    }

    interface View extends BaseView{
        void showUpdateApp(List<AppEntity> list);
    }

    abstract class Presenter extends BasePresenter<Model,View>{
        void getUpdateApp(Context context){}
    }

}
