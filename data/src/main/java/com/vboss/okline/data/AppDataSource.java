package com.vboss.okline.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vboss.okline.data.entities.AppAds;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.CardEntity;

import java.util.List;

import rx.Observable;


/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/31 <br/>
 * Summary : App数据访问入口
 */
public interface AppDataSource {

    /**
     * 获取App详情
     *
     * @param appId
     * @return
     */
    Observable<AppEntity> getAppDetail(int appId);

    /**
     * 获取或搜索我的App。 {@code appName}为空时搜索app。
     *
     * @param appType
     * @param appName
     * @return
     */
    Observable<List<AppEntity>> getOrSearchMyApp(@CardFlavor int appType, @Nullable String appName);

    /**
     * 获取最新上架的应用。返回结果不大于8个。
     *
     * @return
     */
    Observable<List<AppEntity>> getLatestPoolApp();


    /**
     * 获取或搜索PoolApp。{@code appName}为空时搜索app。
     *
     * @param appType   应用类别
     * @param appName   搜索PoolApp是的关键字
     * @param pageIndex 查询结果的页码
     * @param pageSize  每页的数据条数
     * @return
     */
    Observable<List<AppEntity>> getOrSearchPoolApp(@CardFlavor int appType, @Nullable String appName, int pageIndex, int pageSize);

    /**
     * 获取广告位App信息列表。 最多获取六个。
     *
     * @return
     */
    Observable<List<AppAds>> getAppAdsList();

    /**
     * 删除我的App
     *
     * @param pkgName
     */
    void onPoolAppUninstalled(String pkgName);

    /**
     * 当PoolApp下载完成时
     *
     * @param pkgName 包名
     */
    void onPoolAppDownload(String pkgName);

    /**
     * 给我的App添加／移除星标
     *
     * @param appId
     * @param isFollow 0：无星标，1：有星标
     */
    Observable<Boolean> starApp(int appId, @StatusFlavor int isFollow);

    /**
     * 将PoolApp添加到MyApp
     */
    void onPoolAppInstalled(Context context, @NonNull String pkgName);

    /**
     * 获取或搜索Pool中可开卡的卡片列表。{@code cardName}为空时搜索app。
     *
     * @param appType
     * @param cardName  搜索时，用于匹配的卡片名称
     * @param pageIndex 返回查询结果的第几页
     * @param pageSize  返回查询结果每页的数据条数
     * @return
     */
    Observable<List<CardEntity>> getOrSearchPoolCard(@CardFlavor int appType, @Nullable String cardName, int pageIndex, int pageSize);

    Observable<List<AppEntity>> getHotApp(int pageIndex, int pageSize);

    /**
     * 获取待更新应用
     *
     * @param context
     */
    Observable<List<AppEntity>> getPendingUpdateApp(Context context);
}
