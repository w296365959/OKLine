package com.vboss.okline.ui.app.search;

import android.content.Context;
import android.view.View;

import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.AppRepository;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.view.widget.CommonDialog;

import java.net.SocketTimeoutException;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: yuan shaoyu  <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/3/29 <br/>
 * summary  :在这里描述Class的主要功能
 */

public class AppSearchPresenter extends AppSearchContract.Presenter{
    private static final String TAG = "AppSearchPresenter";
    AppSearchContract.View view;
    AppRepository appRepository;

    //网络请求超时处理
    CommonDialog timeOutDialog;

    public AppSearchPresenter(Context context, AppSearchContract.View view){
        this.view = view;
        appRepository = AppRepository.getInstance(context);
        timeOutDialog = new CommonDialog(context);
        timeOutDialog.setTilte(context.getResources().getString(R.string.card_over_time));
        timeOutDialog.setNegativeButton(context.getResources().getString(R.string.cancel));
        timeOutDialog.setPositiveButton(context.getResources().getString(R.string.confirm));
    }

    @Override
    public void onAttached() {

    }

    @Override
    void getSearchMyApp(int appType, String appName) {
        super.getSearchMyApp(appType,appName);
        appRepository.getOrSearchMyApp(appType,appName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<AppEntity>>(TAG){
                    @Override
                    public void onNext(List<AppEntity> list) {
                        super.onNext(list);
                        view.showSearchMyApp(list);
                    }
                });
    }

    @Override
    void getSearchAppPool(int appType, String appName, int pageIndex, int pageSize) {
        super.getSearchAppPool(appType,appName,pageIndex,pageSize);
        appRepository.getOrSearchPoolApp(appType,appName,pageIndex,pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<AppEntity>>(TAG){
                    @Override
                    public void onNext(List<AppEntity> list) {
                        super.onNext(list);
                        view.showSearchAppPool(list);
                    }
                });
    }

    @Override
    void getHotApp() {
        super.getHotApp();
        appRepository.getHotApp(1,50)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<AppEntity>>(TAG){
                    @Override
                    public void onNext(List<AppEntity> list) {
                        super.onNext(list);
                        view.showHotApp(list);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (throwable instanceof SocketTimeoutException ){
                            timeOutDialog.setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
                                @Override
                                public void cancel(View view, CommonDialog dialog) {
                                    dialog.dismiss();
                                }

                                @Override
                                public void ensure(View view, CommonDialog dialog) {
                                    dialog.dismiss();
                                    getHotApp();
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
    void getSearchAppPoolCard(final int appType, final String cardName, final int pageIndex, final int pageSize) {
        super.getSearchAppPoolCard(appType, cardName, pageIndex, pageSize);
        appRepository.getOrSearchPoolCard(appType, cardName, pageIndex, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<CardEntity>>(TAG){
                    @Override
                    public void onNext(List<CardEntity> list) {
                        super.onNext(list);
                        view.showSearchAppPoolCard(list);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (throwable instanceof SocketTimeoutException ){
                            timeOutDialog.setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
                                @Override
                                public void cancel(View view, CommonDialog dialog) {
                                    dialog.dismiss();
                                }

                                @Override
                                public void ensure(View view, CommonDialog dialog) {
                                    dialog.dismiss();
                                    getSearchAppPoolCard(appType, cardName, pageIndex, pageSize);
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
    void deleteApp(String pkgName) {
        super.deleteApp(pkgName);
        appRepository.onPoolAppUninstalled(pkgName);
    }


    @Override
    void addApp(Context context, String pkgName) {
        super.addApp(context, pkgName);
        appRepository.onPoolAppInstalled(context, pkgName);
    }

    @Override
    void poolAppDownload(String pkgName) {
        super.poolAppDownload(pkgName);
        appRepository.onPoolAppDownload(pkgName);
    }
}
