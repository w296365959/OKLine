package com.vboss.okline.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vboss.okline.data.entities.AppAds;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.AppModel;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.data.local.AppLocalDataSource;
import com.vboss.okline.data.remote.AppRemoteDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import timber.log.Timber;


/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/31 <br/>
 * Summary : App数据操作入口
 */
public class AppRepository implements AppDataSource {
    private static final String TAG = AppRepository.class.getSimpleName();

    private AppLocalDataSource local;
    private AppRemoteDataSource remote;
    private static AppRepository instance;

    public static AppRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AppRepository(context);
        }
        return instance;
    }

    private AppRepository(Context context) {
        local = AppLocalDataSource.getInstance(context);
        remote = AppRemoteDataSource.getInstance();
    }

    @Override
    public Observable<AppEntity> getAppDetail(int appId) {
        return local.getAppDetail(appId);
    }


    /**
     * 获取或搜索我的App
     *
     * @param appType app类型
     * @param appName 搜索关键字，仅支持名称搜索
     * @return
     */
    @Override
    public Observable<List<AppEntity>> getOrSearchMyApp(int appType, String appName) {
        return local.getOrSearchMyApp(appType, appName);
    }

    /**
     * 获取应用池的最新上架应用
     */
    @Override
    public Observable<List<AppEntity>> getLatestPoolApp() {
        return local.getLatestPoolApp();
    }

    /**
     * 准备PoolApp的数据，将PoolApp的数据全部保存到本地。<br/>
     * <p>
     * 已在OKLineApp中调用。
     *
     * @param context
     */
    public void preparePoolApp(final Context context) {
        WorkerThreadScheduler.getInstance().execute(new Runnable() {
            @Override
            public void run() {


                //Added by shihaijun: 重新进入OKLine时，不重新拉取服务器App列表
                if (local.isAppExisting()) return;
                //End for modification.

                //Added by shihaijun on 2017-06-16: Add OuleAssistant to database start
                addAssistantApp(context);
                //Added by shihaijun on 2017-06-16: Add OuleAssistant to database end

                try {
                    List<AppEntity> appEntities = remote.getOrSearchPoolApp(CardType.ALL, null, 1, Integer.MAX_VALUE).toBlocking().single();
                    if (!appEntities.isEmpty()) {
                        local.saveAll(fillAppEntities(context, appEntities));
                    }
                } catch (Exception e) {
                    Timber.tag(TAG).e(e, "Fail to prepare app data");
                }
            }
        });
    }

    /**
     * 添加卡助理到数据库。如果不存在，则插入卡助理数据。
     *
     * @param context 上下文
     */
    private void addAssistantApp(Context context) {
        String appName = "卡助理";
        if (!local.isAppExisting(appName)) {
            //Modified by shihaijun on 2017-06-19 : 解决覆盖完整OKLine时卡助理不显示的问题 start
            String pkgName = "com.okline.vboss.assistant"/*context.getPackageName()*/;
            ComponentName cn = new ComponentName(pkgName, "com.okline.vboss.assistant.ui.MainActivity");
            //Modified by shihaijun on 2017-06-19 : 解决覆盖完整OKLine时卡助理不显示的问题 end
            ContentValues assistantValues = AppEntity.buildMarshal(null)
                    .appId(50)
                    .appType(600)
                    .shortShow(50)
                    //add by yuanshaoyu 2017-6-16 :默认加星标
                    .isFollow(1)
                    .openUrl(pkgName)
                    .appName(appName)
                    .appIcon("http://down.okline.com.cn/appicon/kzl.png")
                    .componentName(cn.flattenToString())
                    .isDownload((short) 1)
                    .asContentValues();
            local.save(assistantValues);
        }
    }

    /**
     * 检测PoolApp中是否有app已经安装，如果已经安装，则更新Entity中的componentName字段。<br/>
     * <p>
     * componentName可用于启动应用。
     *
     * @param src 从服务器获取的Entity列表
     * @return 返回可直接插入数据库的ContentValues列表
     */
    private List<ContentValues> fillAppEntities(Context context, List<AppEntity> src) {
        Map<String, String> map = RepoUtils.getMainIntentActivities(context);
        List<ContentValues> valuesList = new ArrayList<>();
        for (AppEntity entity : src) {
            AppModel.Marshal marshal = AppEntity.buildMarshal(entity);
            String pkgName = entity.openUrl();
            if (map.containsKey(pkgName)) {
//                String componentName = new ComponentName(pkgName, map.get(pkgName)).flattenToString();
                if (BuildConfig.DEBUG) {
                    Timber.tag(TAG).i("==>%s had been installed , ComponentName : %s ", pkgName, map.get(pkgName));
                }
                marshal.componentName(map.get(pkgName));
                try {
                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
                    marshal.localVersionCode(packageInfo.versionCode);
                } catch (PackageManager.NameNotFoundException e) {
                    Timber.tag(TAG).e(e);
                }
            }
            valuesList.add(marshal.asContentValues());
        }
        return valuesList;
    }

    @Override
    public Observable<List<AppEntity>> getOrSearchPoolApp(int appType, String appName, int pageIndex, int pageSize) {
        return local.getOrSearchPoolApp(appType, appName, pageIndex, pageSize);
    }

    @Override
    public Observable<List<AppAds>> getAppAdsList() {
        return remote.getAppAdsList();
    }

    @Override
    public void onPoolAppUninstalled(String pkgName) {
        local.onPoolAppUninstalled(pkgName);
    }

    /**
     * 当PoolApp下载完成时
     *
     * @param pkgName 包名
     */
    @Override
    public void onPoolAppDownload(String pkgName) {
        local.onPoolAppDownload(pkgName);
    }

    /**
     * 添加／删除星标
     *
     * @param appId
     * @param isFollow 0：无星标，1：有星标
     * @return
     */
    @Override
    public Observable<Boolean> starApp(int appId, @StatusFlavor int isFollow) {
        return remote.starApp(appId, isFollow).zipWith(local.starApp(appId, isFollow), new Func2<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean remote, Boolean local) {
                return remote && local;
            }
        });
    }

    /**
     * 添加app， 当安装了PoolApp中的App时，更新对应记录的componentName字段，表示该app已安装；
     */
    @Override
    public void onPoolAppInstalled(Context context, @NonNull String pkgName) {
        local.onPoolAppInstalled(context, pkgName);
    }

    @Override
    public Observable<List<CardEntity>> getOrSearchPoolCard(int appType, @Nullable String cardName, int pageIndex, int pageSize) {
        return remote.getOrSearchPoolCard(appType, cardName, pageIndex, pageSize);
    }

    /**
     * 获取热门应用。可通过versionCode和localVersionCode判断是否有新版本；
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public Observable<List<AppEntity>> getHotApp(int pageIndex, int pageSize) {

        return remote.getHotApp(pageIndex, pageSize).map(new Func1<List<AppEntity>, List<AppEntity>>() {
            @Override
            public List<AppEntity> call(List<AppEntity> appEntities) {
                StringBuilder sb = new StringBuilder();
                AppEntity entity = null;
                for (int i = appEntities.size() - 1; i >= 0; i--) {
                    entity = appEntities.get(i);
                    sb.append(AppEntity.APPID + "=")
                            .append(entity.appId());
                    if (i != 0) {
                        sb.append(" OR ");
                    }
                }
                Timber.tag(TAG).i("Build hot AppId whereClause : %s", sb.toString());
                return local.selectApp(sb.toString());
            }
        });
    }

    /**
     * 获取待更新应用列表
     *
     * @param context
     */
    @Override
    public Observable<List<AppEntity>> getPendingUpdateApp(Context context) {
        return local.getPendingUpdateApp(context);
    }
}
