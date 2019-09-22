package com.vboss.okline.tsm;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cosw.tsm.trans.protocol.vo.AppInfo;

import rx.Observable;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/5/4 <br/>
 * Summary : 在这里描述Class的主要功能
 */
interface TsmInterface {
    /**
     * 下载卡应用前需先像TSM平台注册。
     *
     * @param appAid   卡应用aid
     * @param name     姓名
     * @param phone    手机号码
     * @param idCardNo 身份证号
     * @param address  联系地址
     * @return
     */
    Observable<Boolean> appRegister(@NonNull String appAid, @NonNull String name, @Nullable String phone, @Nullable String idCardNo, @Nullable String address);

    /**
     * 安装卡应用
     *
     *
     * @param tsmCardType
     * @param appAid 卡应用Aid
     * @return
     */
    Observable<String> appInstall(int tsmCardType, @NonNull String appAid);

    /**
     * 删除卡应用
     *
     * @param appAid 卡应用aid
     * @return
     */
    Observable<Boolean> appDelete(@NonNull String appAid);

    /**
     * 根据aid获取卡应用信息
     *
     * @param appAid 卡应用aid
     * @return
     */
    Observable<AppInfo> getAppInfo(@NonNull String appAid);

    /**
     * 卡应用是否已安装
     *
     * @param appAid 卡应用aid
     * @return
     */
    boolean isAppInstall(@NonNull String appAid);

    /**
     * 安装安全域
     *
     * @param sdAid 安全域aid
     * @return
     */
    Observable<Boolean> sdInstall(@NonNull String sdAid);

    /**
     * 删除安全域
     *
     * @param sdAid 安全域aid
     * @return
     */
    Observable<Boolean> sdDelete(@NonNull String sdAid);

    /**
     * 查询余额
     *
     * @param tsmCardType tsm平台的卡应用类型
     * @param appAid      卡应用aid
     * @return
     */
    Observable<String> getBalance(@TsmCardFlavor int tsmCardType, @NonNull String appAid);

    /**
     * 卡片圈存
     *
     * @param tsmCardType tsm平台的卡应用类型
     * @param appAid      卡应用aid
     * @param amount      充值金额
     * @return
     */
    Observable<Boolean> recharge(@TsmCardFlavor int tsmCardType, String appAid, int amount);

    /**
     * 释放资源
     */
    void destroy();
}
