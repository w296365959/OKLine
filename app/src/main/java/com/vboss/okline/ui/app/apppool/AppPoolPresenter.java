package com.vboss.okline.ui.app.apppool;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.AppRepository;
import com.vboss.okline.data.entities.AppAds;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.view.widget.CommonDialog;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.vboss.okline.base.OKLineApp.context;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: yuan shaoyu  <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/3/29 <br/>
 * summary  :在这里描述Class的主要功能
 */

public class AppPoolPresenter extends AppPoolContract.Presenter{
    private static final String TAG = "AppPoolPresenter";
    AppPoolContract.View view;
    AppRepository appRepository;
    Subscription subscription;

    public AppPoolPresenter(Context context,AppPoolContract.View view){
        this.view = view;
        appRepository = AppRepository.getInstance(context);
    }

    @Override
    public void onAttached() {

    }

    @Override
    void getAdsImage() {
        super.getAdsImage();

        appRepository.getAppAdsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<AppAds>>(TAG){
                    @Override
                    public void onNext(List<AppAds> appAdses) {
                        super.onNext(appAdses);
                        view.showAdsImage(appAdses);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (throwable instanceof SocketTimeoutException){
                            CommonDialog timeOutDialog = new CommonDialog(context);
                            timeOutDialog.setTilte(context.getResources().getString(R.string.card_over_time));
                            timeOutDialog.setNegativeButton(context.getResources().getString(R.string.cancel));
                            timeOutDialog.setPositiveButton(context.getResources().getString(R.string.confirm));
                            timeOutDialog.setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
                                @Override
                                public void cancel(View view, CommonDialog dialog) {
                                    dialog.dismiss();
                                }

                                @Override
                                public void ensure(View view, CommonDialog dialog) {
                                    dialog.dismiss();
                                    getAdsImage();
                                }
                            });
                            if (timeOutDialog == null) {
                                Timber.tag(TAG).e("time out dialog is null, so don't deal SocketTimeOutException !");
                                return;
                            }
                            if (!timeOutDialog.isShowing()) {
                                timeOutDialog.show();
                            }
                        }

                    }
                });
    }


    @Override
    void getNewAppList() {
        super.getNewAppList();
        subscription =appRepository.getLatestPoolApp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<AppEntity>>(TAG){
                    @Override
                    public void onNext(List<AppEntity> appEntities) {
                        super.onNext(appEntities);
                        view.showNewApp(appEntities);
                    }
                });
    }

    @Override
    void deleteApp(String pkgName) {
        super.deleteApp(pkgName);
        Log.i(TAG,"delete app ："+pkgName);
        appRepository.onPoolAppUninstalled(pkgName);
    }


    @Override
    void addApp(Context context, String pkgName) {
        super.addApp(context, pkgName);
        Log.i(TAG,"add app ："+pkgName);
        appRepository.onPoolAppInstalled(context, pkgName);
    }

    @Override
    void poolAppDownload(String pkgName) {
        super.poolAppDownload(pkgName);
        appRepository.onPoolAppDownload(pkgName);
        Log.i(TAG,"下载应用  ："+pkgName);
    }

    //add by yuanshaoyu 2017-6-16 :增加取消订阅
    @Override
    public void unSubscribe() {
        unsubscribeAll();
    }


    /**
     * 取消订阅
     */
    private void unsubscribeAll() {

        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

    }
}
