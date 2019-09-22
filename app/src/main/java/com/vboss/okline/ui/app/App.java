package com.vboss.okline.ui.app;

import android.graphics.drawable.Drawable;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: yuan shaoyu  <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/3/29 <br/>
 * summary  :在这里描述Class的主要功能
 */

public class App implements AppContract.Model{
    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    /**
     * 数据类型判断

     */
    int typeId;

    /**
     * app在服务器数据库中的Id
     */
    int appId;

    /**
     * app名字
     */
    String appName;

    /**
     * app图标url
     */
    String appIcon;


    /**
     * APP类型，如银行卡类的，便民卡类的、会员卡类的
     */
    private int appType;

    /**
     * 应用的下载地址
     */
    private String downloadUrl;
    /**
     * 应用版本号
     */
    private int version;

    /**
     * App的组件，用于启动App
     */
    private String component;

    private Drawable drawable;

    public int getMainType() {
        return mainType;
    }

    public void setMainType(int mainType) {
        this.mainType = mainType;
    }

    private int mainType;

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    /**
     * App是否安装
     */
    public boolean isInstalled() {
        return getComponent() != null;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }
}
