package com.vboss.okline.ui.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hwangjr.rxbus.RxBus;
import com.vboss.okline.data.AppRepository;

import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: yuan shaoyu  <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/4/21 <br/>
 * summary  : 广播监听APP的安装和卸载
 */

public class InstalledReceiver extends BroadcastReceiver{
    private static final String TAG = InstalledReceiver.class.getSimpleName();

    AppRepository appRepository;
    @Override
    public void onReceive(Context context, Intent intent) {
        appRepository = AppRepository.getInstance(context);
        String intentAction = intent.getAction();
        String packageName = intent.getDataString();//获取应用包名
        //截取包名用于比对
        String intentPackageName = packageName.substring(packageName.indexOf(":") + 1, packageName.length());
        if (intentAction.equals("android.intent.action.PACKAGE_REMOVED")) {
            Timber.tag(TAG).i("uninstaller application %s",packageName);
            appRepository.onPoolAppUninstalled(intentPackageName);
            Log.i("Receiver" , "removed");
        }else if (intentAction.equals("android.intent.action.PACKAGE_ADDED")){
            Timber.tag(TAG).i("installer application %s",packageName);
            appRepository.onPoolAppDownload(intentPackageName);
            appRepository.onPoolAppInstalled(context, intentPackageName);
            Log.i("Receiver" , "added");

        }
        //add by yuanshaoyu 2017-6-19 :event post message
        RxBus.get().post(AppHelper.EVENT_APP_REFRESH,true);
        Timber.tag(TAG).i("RxBus post message refresh app data ");

        //else if(intentAction.equals("android.intent.action.PACKAGE_REPLACED")){}
    }
}
