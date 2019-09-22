package com.vboss.okline.ui.app;

import android.content.Context;

import com.vboss.okline.base.BaseModel;
import com.vboss.okline.base.BasePresenter;
import com.vboss.okline.base.BaseView;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.OKCard;

import java.util.List;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: yuan shaoyu  <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/3/29 <br/>
 * summary  :在这里描述Class的主要功能
 */
 interface AppContract {
    interface Model extends BaseModel{

    }

    interface View extends BaseView{
        void showJiugonggeDialogData(List<AppEntity> list);
        void showCommonData(List<AppEntity> list);
        void showVipData(List<AppEntity> list);
        void showBankData(List<AppEntity> list);
        void showTransData(List<AppEntity> list);
        void showDoorData(List<AppEntity> list);
        void showCredentialsData(List<AppEntity> list);
        void showWorkData(List<AppEntity> list);
        void showUpdateApp(List<AppEntity> list);
        void showStarApp(Boolean aBoolean);
    }

    abstract class Presenter extends BasePresenter<Model,View>{
        void getJiugonggeDialogData(int appType, String appName){}
        void deleteApp(String pkgName){}
        void addApp(Context context, String pkgName){}
        void StarApp(int appId, int star){}
        void getUpdateApp(Context context){}
        void unSubscribe(){}
    }

}
