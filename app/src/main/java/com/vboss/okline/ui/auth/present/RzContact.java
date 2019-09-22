package com.vboss.okline.ui.auth.present;

import android.hardware.Camera;
import com.vboss.okline.data.entities.AppVersion;
import com.vboss.okline.data.entities.User;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: Mengyupeng<br/>
 * Email:  mengyupeng@okline.cn</br>
 * Date : $(DATE) </br>
 * Summary:
 */

public interface RzContact {

    interface RPresenter{

        /**
         * 查询实人认证状态
         *
         * @param deviceNo 设备号（使用手机IMEI号）
         * @return
         */
        void queryAuthStatus(String deviceNo, String phoneImei);

        /**
         * 获取阿里实人认证Token
         *
         * @param deviceNo 设备号（使用手机IMEI号）
         * @return 返回认证Token
         */
        void getVerifyToken(String deviceNo, String phoneImei);

        /**
         * 获取认证结果
         *
         * @param deviceNo 设备号（使用手机IMEI号）
         * @return 返回认证结果：1;初始 2;认证通过 3;认证失败 4;转人工 5;未认证用户取消
         */
        void getVerifyState(String deviceNo, String phoneImei);
    }

    interface IPresenter{
        /**
         * 用户注册接口
         * @param idCardNo 证件号码
         * @param realName  姓名
         * @param creditType   证件类型（1身份证，  2护照）
         * @param imei   设备ID
         * @param imsi   imsi
         */
        void loginOrRegisterUser(int creditType,String idCardNo, String realName, String imei,  String imsi);

        /**
         * 用户登录接口
         * @param imei   设备ID
         * @param imsi   imsi
         */
        void login( String imei,  String imsi);

        void savePicDatat(byte[] data);
        Camera getCamera();
    }

    interface IResultView{
        /**
         * 用户数据信息
         * @param user
         */
        void setResult(User user);

        /**
         * 异常数据信息
         * @param error
         * @param code
         */
        void setError(String error,int code);
    }

    interface IResultUpdate{
        /**
         * 获取版本数据
         * @param appVersions
         */
        void getResultUpdate(AppVersion appVersions);
    }

}
