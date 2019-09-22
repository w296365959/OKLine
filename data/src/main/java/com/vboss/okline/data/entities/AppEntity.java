package com.vboss.okline.data.entities;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.Serializable;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/30 <br/>
 * Summary : App应用实体类
 */
@AutoValue
public abstract class AppEntity implements AppModel, Serializable {

    public static final AppEntity.Factory<AppEntity> FACTORY = new Factory<>(new Creator<AppEntity>() {
        @Override
        public AppEntity create(long appId, @Nullable String appName, int appType, @Nullable String appIcon, @Nullable String openUrl, @Nullable String apkUrl, int isFollow, int shortShow, @Nullable String componentName, short isDownload, int localVersionCode, int versionCode, @Nullable String versionName, @Nullable String versionLog) {
            return new AutoValue_AppEntity(appId, appName, appType, appIcon, openUrl, apkUrl, isFollow, shortShow, componentName, isDownload, localVersionCode, versionCode, versionName, versionLog);
        }
    });

    public static TypeAdapter<AppEntity> typeAdapter(Gson gson) {
        return new AutoValue_AppEntity.GsonTypeAdapter(gson);
    }

    public static final Mapper<AppEntity> MAPPER = new Mapper<>(FACTORY);

    public static Marshal buildMarshal(AppModel copy) {
        return new Marshal(copy);
    }


}
