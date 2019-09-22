package com.vboss.okline.ui.app.update;

import android.content.Context;
import android.util.Log;

import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.AppRepository;
import com.vboss.okline.data.entities.AppEntity;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: yuan shaoyu  <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/4/20 <br/>
 * summary  :在这里描述Class的主要功能
 */

public class UpdateAppPresenter extends UpdateAppContract.Presenter{

    private static final String TAG = "AppPresenter";
    UpdateAppContract.View view;
    AppRepository appRepository;

    public UpdateAppPresenter(Context context, UpdateAppContract.View view){
        this.view = view;
        appRepository = AppRepository.getInstance(context);
    }
    @Override
    public void onAttached() {

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
                        Log.e(TAG,"updateApp.size : " + list.toString());
                        view.showUpdateApp(list);
                    }
                });
    }
}
