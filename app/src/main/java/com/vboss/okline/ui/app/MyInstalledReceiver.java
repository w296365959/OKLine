package com.vboss.okline.ui.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hwangjr.rxbus.RxBus;
import com.vboss.okline.data.AppRepository;

import timber.log.Timber;

import static com.hyphenate.easeui.ui.CallActivity.TAG;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: yuan shaoyu  <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/6/20 <br/>
 * summary  :在这里描述Class的主要功能
 */

public class MyInstalledReceiver extends BroadcastReceiver {
    private static final String TAG = MyInstalledReceiver.class.getSimpleName();

    AppRepository appRepository;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        appRepository = AppRepository.getInstance(context);

        String intentAction = intent.getAction();
        String packageName = intent.getDataString();//获取应用包名
        Log.i(TAG, "packageName:" + packageName);
        String intentPackageName = packageName.substring(packageName.indexOf(":") + 1);//截取包名用于比对
        if (intentAction.equals("android.intent.action.PACKAGE_ADDED")) {
            Timber.tag(TAG).i("installer application %s",packageName);
            // add by yuanshaoyu 2017-5-24 :判断自己应用下载的app
            appRepository.onPoolAppDownload(intentPackageName);
            appRepository.onPoolAppInstalled(context, intentPackageName);
        } else if (intentAction.equals("android.intent.action.PACKAGE_REMOVED")) {
            Timber.tag(TAG).i("uninstaller application %s",packageName);
            appRepository.onPoolAppUninstalled(intentPackageName);
        }

        //add by yuanshaoyu 2017-6-20 :event post message
        RxBus.get().post(AppHelper.EVENT_APP_REFRESH,true);
        Timber.tag(TAG).i("RxBus post message refresh app data ");
    }
}
