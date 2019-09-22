package com.vboss.okline.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.GsonBuilder;
import com.hyphenate.easeui.ChatContentUtil;
import com.hyphenate.easeui.EaseHelper;
import com.okline.vboss.http.GsonUtils;
import com.vboss.okline.BuildConfig;
import com.vboss.okline.data.adapter.AutoValueGsonTypeAdapterFactory;
import com.vboss.okline.data.local.SPUtils;
import com.vboss.okline.ui.home.download.DownLoadService;

import cn.jpush.android.api.JPushInterface;
import timber.log.Timber;


/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/3/29 14:26 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

public class OKLineApp extends Application {
    public static OKLineApp context;

    public static String getStringResource(int resId) {
        return context.getResources().getString(resId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //add by yuanshaoyu 2017-6-27 :下载器服务
        this.startService(new Intent(this, DownLoadService.class));

        // easeui init
        Fresco.initialize(this);
        // chat content init
        ChatContentUtil.getInstance().setContext(getApplicationContext());
        EaseHelper.getInstance().init(this);

        setupTimber();
        context = this;
        //Init JPush
        initJPush();
        SPUtils.init(this);
        //Added by shihaijun on 2017-06-14 to set up gson start
        GsonUtils.initialize(new GsonBuilder()
                .registerTypeAdapterFactory(AutoValueGsonTypeAdapterFactory.create())
                .create());
        //Added by shihaijun on 2017-06-14 to set up gson end


        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);*/
        enableStrictMode();
    }

    /**
     * 启动严苛调试模式
     */
    private void enableStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .detectAll().build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedClosableObjects()
                    .detectLeakedRegistrationObjects()
                    .detectLeakedSqlLiteObjects()
                    .detectActivityLeaks()
                    .penaltyLog()
                    .detectAll().build());
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 配置日志打印组件
     */
    private void setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void initJPush() {
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
        JPushInterface.initCrashHandler(this);
        Timber.tag("JIGUANG").i("JIGUANG ID = %s", JPushInterface.getRegistrationID(this));
    }
}
