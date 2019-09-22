package com.vboss.okline.ui.app;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.AppRepository;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.CardType;
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

public  class AppPresenter extends AppContract.Presenter{
    private static final String TAG = "AppPresenter";
    AppContract.View view;
    AppRepository appRepository;
    private List<Subscription> subscriberList;

    public AppPresenter(Context context, AppContract.View view){
        this.view = view;
        appRepository = AppRepository.getInstance(context);
    }
    @Override
    public void onAttached() {

    }

    @Override
    void getJiugonggeDialogData(final int appType, final String appName) {
        super.getJiugonggeDialogData(appType,appName);
        Subscription sbpMyApp = appRepository.getOrSearchMyApp(appType,appName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<AppEntity>>(TAG){
                    @Override
                    public void onNext(List<AppEntity> appEntities) {
                        super.onNext(appEntities);
                        if (appType == CardType.COMMON_CARD) {
                            view.showCommonData(appEntities);
                        }else if(appType == CardType.VIP_CARD){
                            view.showVipData(appEntities);
                        }else if(appType == CardType.BANK_CARD){
                            view.showBankData(appEntities);
                        }else if(appType == CardType.CREDENTIALS){
                            view.showCredentialsData(appEntities);
                        }else if(appType == CardType.DOOR_CARD){
                            view.showDoorData(appEntities);
                            //modified by yuanshaoyu 2017-5-26 :把交通类改为票务类
                        }else if(appType == CardType.TICKET){
                            view.showTransData(appEntities);
                        }else if(appType == CardType.EMPLOYEE_CARD){
                            view.showWorkData(appEntities);
                        }else {
                            Log.i(TAG,"已安装 ： "+appEntities);
                            view.showJiugonggeDialogData(appEntities);
                        }
                    }
                });
        addSubscription(sbpMyApp);
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
    void StarApp(final int appId, final int star) {
        super.StarApp(appId, star);
        Subscription sbpStarApp = appRepository.starApp(appId,star)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<Boolean>(TAG){
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        view.showStarApp(aBoolean);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (throwable instanceof SocketTimeoutException) {
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
                                    StarApp(appId, star);
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
        addSubscription(sbpStarApp);
    }

    @Override
    void getUpdateApp(Context context) {
        super.getUpdateApp(context);

        appRepository.getPendingUpdateApp(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<AppEntity>>(TAG){
                    @Override
                    public void onNext(List<AppEntity> list) {
                        super.onNext(list);
                        view.showUpdateApp(list);
                    }
                });

    }
    /**
     * 添加订阅
     */
    private void addSubscription(Subscription subscription) {
        if (subscriberList == null) {
            subscriberList = new ArrayList<>();
        }
        if (!subscriberList.contains(subscription)) {
            subscriberList.add(subscription);
        }
    }

    @Override
    public void unSubscribe() {
        unsubscribeAll();
    }

    /**
     * 取消订阅
     */
    private void unsubscribeAll() {
        if (subscriberList != null && subscriberList.size() != 0) {
            for (Subscription subscription : subscriberList) {
                if (!subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }
            }
            subscriberList.clear();
        }
    }
}
