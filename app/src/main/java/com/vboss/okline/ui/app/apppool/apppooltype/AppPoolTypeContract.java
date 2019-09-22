package com.vboss.okline.ui.app.apppool.apppooltype;

import android.content.Context;

import com.vboss.okline.base.BaseModel;
import com.vboss.okline.base.BasePresenter;
import com.vboss.okline.base.BaseView;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.CardEntity;

import java.util.List;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: yuan shaoyu  <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/3/30 <br/>
 * summary  :在这里描述Class的主要功能
 */
 interface AppPoolTypeContract {
    interface Model extends BaseModel{

    }

    interface View extends BaseView{
        void showAppPoolTypeList(List<AppEntity> list);
        void showAppPoolCardList(List<CardEntity> cardList);
        void requestAppFailed();
        void requestCardFailed();
    }

    abstract class Presenter extends BasePresenter<Model,View>{
        void getAppPoolTypeList(int appType, String appName, int pageIndex, int pageSize){}
        void getAppPoolCardList(int appType,String cardName, int pageIndex, int pageSize){}
        void addApp(Context context , String pkgName){}
        void deleteApp(String pkgName){}
        void poolAppDownload(String pkgName){}
    }
}

