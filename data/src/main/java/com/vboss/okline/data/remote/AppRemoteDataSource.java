package com.vboss.okline.data.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.reflect.TypeToken;
import com.okline.vboss.http.OKLineClient;
import com.vboss.okline.data.AppDataSource;
import com.vboss.okline.data.CardFlavor;
import com.vboss.okline.data.RepoUtils;
import com.vboss.okline.data.entities.AppAds;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.AppVersion;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.data.entities.CheckInfo;

import java.util.List;

import rx.Observable;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/7 <br/>
 * Summary : App服务器数据操作入口
 */
public class AppRemoteDataSource implements AppDataSource {
    private static final String POOL_APP_REQUEST = "qryAppListRequest";
    private static final String POOL_CARD_REQUEST = "qryAppListByCardRequest";
    private static final String POOL_ADS_LIST = "qryAppAdListRequest";

    private static final String HOT_APP_LIST = "qryHotAppListRequest";
    private static final String STAR_APP = "setFollowAppRequest";
    private static final String CHECK_VERSION_REQUEST = "checkVersionRequest";
    private final OKLineClient client;
    private static AppRemoteDataSource instance;

    public static AppRemoteDataSource getInstance() {
        if (instance == null) {
            instance = new AppRemoteDataSource();
        }
        return instance;
    }


    private AppRemoteDataSource() {
        client = OKLineClient.getInstance();
    }

    @Override
    public Observable<AppEntity> getAppDetail(int appId) {
        return null;
    }

    @Override
    public Observable<List<AppEntity>> getOrSearchMyApp(int appType, String appName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<List<AppEntity>> getLatestPoolApp() {
        return getOrSearchPoolApp(CardType.ALL, null, 1, 8);
    }

    @Override
    public Observable<List<AppEntity>> getOrSearchPoolApp(@CardFlavor int appType, @Nullable String appName, int pageIndex, int pageSize) {
        RepoUtils.checkPageIndexAndSize(pageIndex, pageSize);
        RequestData.Builder builder = RequestData.newBuilder()
                .sysBelong(1)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .appName(appName);
        if (appType != CardType.ALL) {
            builder.cardMainType(appType);
        }

        return client.requestAsyncForData(POOL_APP_REQUEST, builder.build(), new TypeToken<List<AppEntity>>() {
        }.getType());
    }

    @Override
    public Observable<List<AppAds>> getAppAdsList() {
        return client.requestAsyncForData(POOL_ADS_LIST, RequestData.newBuilder()
                .sysBelong(1)
                .pageIndex(1)
                .pageSize(6)
                .build(), new TypeToken<List<AppAds>>() {
        }.getType());
    }

    @Override
    public void onPoolAppUninstalled(String pkgName) {

    }

    /**
     * 当PoolApp下载完成时
     *
     * @param pkgName 包名
     */
    @Override
    public void onPoolAppDownload(String pkgName) {

    }

    @Override
    public Observable<Boolean> starApp(int appId, int isFollow) {
        return client.requestAsyncForBoolean(STAR_APP, RequestData.newBuilder()
                .appId(appId)
                .isFollow(isFollow)
                .build());
    }

    @Override
    public void onPoolAppInstalled(Context context, @NonNull String pkgName) {

    }

    @Override
    public Observable<List<CardEntity>> getOrSearchPoolCard(int appType, @Nullable String cardName, int pageIndex, int pageSize) {
        RepoUtils.checkPageIndexAndSize(pageIndex, pageSize);

        RequestData data = RequestData.newBuilder().sysBelong(1)
                .cardMainType(appType)
                .cardName(cardName)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();

        return client.requestAsyncForData(POOL_CARD_REQUEST, data, new TypeToken<List<CardEntity>>() {
        }.getType());
    }

    @Override
    public Observable<List<AppEntity>> getHotApp(int pageIndex, int pageSize) {
        RepoUtils.checkPageIndexAndSize(pageIndex, pageSize);


        return client.requestAsyncForData(HOT_APP_LIST, RequestData.newBuilder()
                .sysBelong(1)
                .pageIndex(pageIndex)
                .pageSize(pageSize).build(), new TypeToken<List<AppEntity>>() {
        }.getType());
    }

    @Override
    public Observable<List<AppEntity>> getPendingUpdateApp(Context context) {
        return null;
    }

    /**
     * @param baseVersion
     * @param checkInfos
     * @return
     */
    public Observable<List<AppVersion>> checkVersionRequest(String baseVersion, CheckInfo[] checkInfos) {
        return checkVersionRequest(baseVersion, checkInfos, AppVersion.PHASE_DEV);
    }


    /**
     * @param baseVersion
     * @param checkInfo
     * @param phase
     * @return
     */
    public Observable<List<AppVersion>> checkVersionRequest(String baseVersion, CheckInfo[] checkInfo, int phase) {
        //Modified by shihaijun on 2017-07-03 : 添加参数 start
        /*return client.requestAsyncForData(CHECK_VERSION_REQUEST, RequestData.newBuilder()
                .baseVersion(baseVersion)
                .checkInfo(checkInfo)
                .build(), new TypeToken<List<AppVersion>>() {
        }.getType());*/
        return checkVersionRequest(baseVersion, checkInfo, AppVersion.TYPE_OL_APP, null, 1, phase);
        //Modified by shihaijun on 2017-07-03 : 添加参数 end
    }

    /**
     * App版本信息查询
     *
     * @param baseVersion 基础版本
     * @param checkInfos  检查的对象
     * @param updateType  更新类型
     * @param versionName 版本号
     * @param platform    软件平台
     * @param phase       版本用途
     * @return
     */
    private Observable<List<AppVersion>> checkVersionRequest(String baseVersion, CheckInfo[] checkInfos, int updateType,
                                                             String versionName, int platform, int phase) {
        return client.requestAsyncForData(CHECK_VERSION_REQUEST, RequestData.newBuilder()
                .baseVersion(baseVersion)
                .checkInfo(checkInfos)
                .updateType(updateType)
                .versionName(versionName)
                .platform(platform)
                .phase(phase).build(), new TypeToken<List<AppVersion>>() {
        }.getType());
    }
}
