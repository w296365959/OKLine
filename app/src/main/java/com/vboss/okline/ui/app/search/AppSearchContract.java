package com.vboss.okline.ui.app.search;

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
 * Date  : 2017/3/29 <br/>
 * summary  :在这里描述Class的主要功能
 */

public interface AppSearchContract {
    interface Model extends BaseModel{

    }
    interface View extends BaseView{
        void showSearchMyApp(List<AppEntity> list);
        void showSearchAppPool(List<AppEntity> list);
        void showSearchAppPoolCard(List<CardEntity> list);
        void showHotApp(List<AppEntity> list);
    }
    abstract class Presenter extends BasePresenter<Model,View>{
        void getSearchMyApp(int appType, String appName){}
        void getSearchAppPool(int appType, String appName, int pageIndex, int pageSize){}
        void getSearchAppPoolCard(int appType,String cardName, int pageIndex, int pageSize){}
        void getHotApp(){}
        void deleteApp(String pkgName){}
        void addApp(Context context, String pkgName){}
        void poolAppDownload(String pkgName){}
    }
}
