package com.vboss.okline.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.vboss.okline.data.AppDataSource;
import com.vboss.okline.data.CardFlavor;
import com.vboss.okline.data.IDataSource;
import com.vboss.okline.data.RepoUtils;
import com.vboss.okline.data.StatusFlavor;
import com.vboss.okline.data.WorkerThreadScheduler;
import com.vboss.okline.data.entities.AppAds;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.AppModel;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardType;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/31 <br/>
 * Summary : App本地数据入口
 */
public class AppLocalDataSource implements AppDataSource, IDataSource<ContentValues> {
    private static final String TAG = AppLocalDataSource.class.getSimpleName();
    private DBService dbService;
    private final Func1<Cursor, AppEntity> APP_MAPPER;

    private static AppLocalDataSource instance;

    public static AppLocalDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new AppLocalDataSource(context);
        }
        return instance;
    }

    private AppLocalDataSource(Context context) {
        dbService = DBService.getInstance(context);
        APP_MAPPER = new Func1<Cursor, AppEntity>() {
            @Override
            public AppEntity call(Cursor cursor) {
                return AppEntity.MAPPER.map(cursor);
            }
        };
    }

    /**
     * 判断App缓存是否存在
     */
    public boolean isAppExisting() {
        return dbService.isDataExisting(AppEntity.TABLE_NAME, null);
    }

    /**
     * 根据{@link AppEntity#APPNAME} 判断应用是否存在
     *
     * @param appName app名称
     * @return 若名称为{@code appName}的应用存在，则返回true，否则返回false。
     */
    public boolean isAppExisting(@NonNull String appName) {
        return dbService.isDataExisting(AppEntity.TABLE_NAME, AppEntity.APPNAME + " = ?", appName);
    }

    @Override
    public Observable<AppEntity> getAppDetail(int appId) {
        return dbService.queryOneQuietly(AppEntity.TABLE_NAME, APP_MAPPER, AppEntity.APPID + " = ?", String.valueOf(appId));
    }

    @Override
    public Observable<List<AppEntity>> getOrSearchMyApp(int appType, String appName) {
        StringBuilder sb = new StringBuilder(AppEntity.COMPONENTNAME + " IS NOT NULL ")
                .append(" AND ")
                .append(AppEntity.ISDOWNLOAD + "=1");
        if (CardType.ALL != appType) {
            sb.append(" AND ")
                    .append(AppEntity.APPTYPE + "=")
                    .append(appType);
        }
        if (!TextUtils.isEmpty(appName)) {
            sb.append(" AND ")
                    .append(RepoUtils.buildLikeStatement(AppEntity.APPNAME, appName));
        }

        return dbService.queryListWithWhere(AppEntity.TABLE_NAME, APP_MAPPER, sb.toString());
    }

    @Override
    public Observable<List<AppEntity>> getLatestPoolApp() {
        String sql = SQLiteQueryBuilder.buildQueryString(false, AppEntity.TABLE_NAME, null, null, null, null, AppEntity.SHORTSHOW + " DESC", "8");
        return dbService.queryListQuietly(sql, APP_MAPPER);
    }

    @Override
    public Observable<List<AppEntity>> getOrSearchPoolApp(@CardFlavor int appType, String appName, int pageIndex, int pageSize) {
        RepoUtils.checkPageIndexAndSize(pageIndex, pageSize);
        StringBuilder whereClause = new StringBuilder();

        if (!TextUtils.isEmpty(appName)) {
            whereClause.append(RepoUtils.buildLikeStatement(AppEntity.APPNAME, appName));
        }

        if (appType != CardType.ALL) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append(AppEntity.APPTYPE + "=").append(appType);
        }

        String limit = null;
        if (pageIndex == 1) {
            limit = String.valueOf(pageSize);
        } else if (pageIndex > 1) {
            //modified by yuanshaoyu 2017-5-22 : 解决分页数据错误
            limit = (pageIndex - 1) * pageSize + ", " + pageSize;
        }


        String sql = SQLiteQueryBuilder.buildQueryString(false, AppEntity.TABLE_NAME, null,
                whereClause.toString(), null, null, AppEntity.SHORTSHOW + " DESC", limit);

        //Timber.tag(TAG).i("getOrSearchPoolApp sql : %s", whereClause.toString());
        Timber.tag(TAG).i("getOrSearchPoolApp sql : %s", sql);
        return dbService.queryListQuietly(sql, APP_MAPPER);
    }

    @Override
    public Observable<List<AppAds>> getAppAdsList() {
        return null;
    }

    @Override
    public void onPoolAppUninstalled(String pkgName) {
        dbService.update(AppEntity.TABLE_NAME, AppEntity.buildMarshal(null).componentName(null)
                        .versionCode(0)
                        .isDownload((short) 0)
                        .versionName(null)
                        .versionLog(null).asContentValues(),
                AppEntity.OPENURL + "=?", pkgName);
    }

    /**
     * 当PoolApp下载完成时,将该app设置为已下载
     *
     * @param pkgName 包名
     */
    @Override
    public void onPoolAppDownload(String pkgName) {
        dbService.update(AppEntity.TABLE_NAME, AppEntity.buildMarshal(null).isDownload((short) 1).asContentValues(),
                AppEntity.OPENURL + "=?", pkgName);
    }

    @Override
    public Observable<Boolean> starApp(final int appId, @StatusFlavor final int isFollow) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return dbService.update(AppEntity.TABLE_NAME, AppEntity.buildMarshal(null).isFollow(isFollow).asContentValues(),
                        AppEntity.APPID + "=?", String.valueOf(appId)) != 0;
            }
        });
    }

    @Override
    public void onPoolAppInstalled(final Context context, @NonNull final String pkgName) {
        WorkerThreadScheduler.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                if (!isAppPoolApp(pkgName)) {
                    Timber.tag(TAG).w("======%s is not poolApp========");
                    return;
                }
                String componentName = getComponentName(context, pkgName);
                if (componentName != null) {
                    AppModel.Marshal marshal = AppEntity.buildMarshal(null).componentName(componentName);
                    try {
                        PackageInfo info = context.getPackageManager().getPackageInfo(pkgName, 0);
                        marshal.localVersionCode(info.versionCode);
                    } catch (PackageManager.NameNotFoundException e) {
                        Timber.tag(TAG).e(e);
                    }
                    int count = dbService.update(AppEntity.TABLE_NAME, marshal.asContentValues(),
                            AppEntity.OPENURL + "=?", pkgName);

                    Timber.tag(TAG).w("Add app[%d], componentName = %s", count, componentName);
                } else {
                    Timber.tag(TAG).e("Fail to get ComponentName of %s", pkgName);
                }
            }
        });
    }

    /**
     * 判断包名为pkgName的app是否为PoolApp。
     *
     * @param pkgName app包名
     * @return 若是PoolApp，则返回true，否则返回false。
     */
    private boolean isAppPoolApp(@NonNull String pkgName) {
        return dbService.isDataExisting(AppEntity.TABLE_NAME, AppEntity.OPENURL + "=? LIMIT 1", pkgName);
    }

    /**
     * 拼装应用的ComponentName，用于启动。
     *
     * @param context
     * @param pkgName
     * @return
     */
    private String getComponentName(Context context, String pkgName) {
        return RepoUtils.getMainIntentActivities(context).get(pkgName);
        /*List<ResolveInfo> resolveInfos = RepoUtils.queryIntentActivities(context);
        for (ResolveInfo info : resolveInfos) {
            if (BuildConfig.DEBUG) {
                Timber.tag(TAG).i("PackageName = %s, mainActivity = %s", info.activityInfo.packageName, info.activityInfo.name);
            }
            if (info.activityInfo.packageName.equals(pkgName)) {
                return new ComponentName(pkgName, info.activityInfo.name).flattenToString();
            }
        }
        return null;*/
    }


    @Override
    public Observable<List<CardEntity>> getOrSearchPoolCard(int appType, @Nullable String cardName, int pageIndex, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<AppEntity>> getHotApp(int pageIndex, int pageSize) {
        throw new UnsupportedOperationException("bu zhun yong");
    }

    @Override
    public Observable<List<AppEntity>> getPendingUpdateApp(final Context context) {
        String whereClause = AppEntity.COMPONENTNAME + " IS NOT NULL"
                + " AND "
                + AppEntity.LOCALVERSIONCODE + "<" + AppEntity.VERSIONCODE;

        String sql = SQLiteQueryBuilder.buildQueryString(true, AppEntity.TABLE_NAME,
                null, whereClause, null, null, AppEntity.SHORTSHOW + " DESC ", null);
        return dbService.queryListQuietly(sql, APP_MAPPER);
    }

    @Override
    public Boolean save(ContentValues values) {
        return dbService.insert(AppEntity.TABLE_NAME, values) != -1;
    }

    @Override
    public boolean saveAll(List<ContentValues> list) {
        return dbService.bulkInsert(AppEntity.TABLE_NAME, list) != 0;
    }

    /**
     * 查询app
     *
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public List<AppEntity> selectApp(String whereClause, String... whereArgs) {
        return dbService.select(AppEntity.TABLE_NAME, AppEntity.MAPPER, whereClause, whereArgs);
    }

    public Observable<List<AppEntity>> getApp(String whereClause, String... whereArgs) {
        return dbService.queryListQuietly(AppEntity.TABLE_NAME, APP_MAPPER, whereClause, whereArgs);
    }

}
