package com.vboss.okline.ui.app.apppool.apppooltype;

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

import static com.vboss.okline.base.OKLineApp.context;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: yuan shaoyu  <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/3/30 <br/>
 * summary  :在这里描述Class的主要功能
 */

public class AppPoolTypePresenter extends AppPoolTypeContract.Presenter{
    private static final String TAG = "AppPoolTypePresenter";
    AppPoolTypeContract.View view;
    AppRepository appRepository;
    public AppPoolTypePresenter(Context context,AppPoolTypeContract.View view){
        this.view = view;
        appRepository = AppRepository.getInstance(context);
    }
    @Override
    public void onAttached() {

    }

    @Override
    void getAppPoolTypeList(int appType, String appName, int pageIndex, int pageSize) {
        super.getAppPoolTypeList(appType, appName, pageIndex, pageSize);
        appRepository.getOrSearchPoolApp(appType,appName,pageIndex,pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<AppEntity>>(TAG){
                    @Override
                    public void onNext(List<AppEntity> appEntities) {
                        super.onNext(appEntities);
                        view.showAppPoolTypeList(appEntities);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        view.requestAppFailed();
                    }
                });


    }

    @Override
    void getAppPoolCardList(final int appType, final String cardName, final int pageIndex, final int pageSize) {
        super.getAppPoolCardList(appType,cardName,pageIndex,pageSize);
        appRepository.getOrSearchPoolCard(appType,cardName,pageIndex,pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<CardEntity>>(TAG){
                    @Override
                    public void onNext(List<CardEntity> cardEntities) {
                        super.onNext(cardEntities);
                        view.showAppPoolCardList(cardEntities);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        view.requestCardFailed();
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
                                    getAppPoolCardList(appType,cardName,pageIndex,pageSize);
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
    void addApp(Context context , String pkgName) {
        super.addApp(context, pkgName);
        appRepository.onPoolAppInstalled(context, pkgName);
    }

    @Override
    void deleteApp(String pkgName) {
        super.deleteApp(pkgName);
        appRepository.onPoolAppUninstalled(pkgName);
    }

    @Override
    void poolAppDownload(String pkgName) {
        super.poolAppDownload(pkgName);
        appRepository.onPoolAppDownload(pkgName);
    }
}
