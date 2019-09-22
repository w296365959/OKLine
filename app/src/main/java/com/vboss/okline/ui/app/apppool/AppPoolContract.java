package com.vboss.okline.ui.app.apppool;

import android.content.Context;

import com.vboss.okline.base.BaseModel;
import com.vboss.okline.base.BasePresenter;
import com.vboss.okline.base.BaseView;
import com.vboss.okline.data.entities.AppAds;
import com.vboss.okline.data.entities.AppEntity;

import java.util.List;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: yuan shaoyu  <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/3/29 <br/>
 * summary  :在这里描述Class的主要功能
 */
 interface AppPoolContract {
    interface Model extends BaseModel{

    }
    interface View extends BaseView{
        void showNewApp(List<AppEntity> list);
        void showAdsImage(List<AppAds> list);
    }
    abstract class Presenter extends BasePresenter<Model,View>{
        void getNewAppList(){}
        void getAdsImage(){}
        void deleteApp(String pkgName){}
        void addApp(Context context, String pkgName){}
        void poolAppDownload(String pkgName){}
        /**
         * 取消订阅
         */
        public abstract void unSubscribe();
    }
}
