package com.vboss.okline.data.entities;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/12 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class AppVersion {

    public static final int FLAG_MUST = 1;   // 强制更新
    public static final int FLAG_CHOOSE = 2; // 可选
    public static final int FLAG_NULL = 3;   // 无需

    public static final int TYPE_OL_APP = 10;   // 欧乐APP
    public static final int TYPE_OPOS = 11; // OPOS
    public static final int TYPE_FIRMWARE = 20; // 固件更新
    public static final int FLAG_WORD_FILTER = 30;   // 敏感词更新

    /**
     * 开发版本
     */
    public static final int PHASE_DEV = 1;
    /**
     * 测试版本
     */
    public static final int PHASE_TEST = 2;
    /**
     * 生产版本
     */
    public static final int PHASE_PRODUCT = 3;


    int versionType;
    String versionName;
    int updateFlag;
    String versionUrl;
    String versionMemo;

    public int getVersionType() {
        return versionType;
    }

    public void setVersionType(int versionType) {
        this.versionType = versionType;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(int updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public String getVersionMemo() {
        return versionMemo;
    }

    public void setVersionMemo(String versionMemo) {
        this.versionMemo = versionMemo;
    }
}
