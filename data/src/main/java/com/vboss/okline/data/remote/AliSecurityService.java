package com.vboss.okline.data.remote;

import com.okline.vboss.http.OKLineClient;

import rx.Observable;
import rx.functions.Func1;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/6/2 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class AliSecurityService {
    private static final String STATUS_REQUEST = "statusRequest";
    private static final String TOKEN_REQUEST = "tokenRequest";
    private static final String RESULT_REQUEST = "qryRetRequest";
    private static AliSecurityService instance;
    private final OKLineClient client;

    public static AliSecurityService getInstance() {
        if (instance == null) {
            instance = new AliSecurityService();
        }
        return instance;
    }

    private AliSecurityService() {
        client = OKLineClient.getInstance();
    }

    /**
     * 查询实人认证状态
     *
     * @param deviceNo 设备号（使用手机IMEI号）
     * @param imei     手机IMEI号
     * @return
     */
    public Observable<AliResult.Status> queryAuthStatus(String deviceNo, String imei) {
        return client.<AliResult.Status>aliSecurityRequest(deviceNo, imei, STATUS_REQUEST, AliResult.Status.class);
    }

    /**
     * 获取阿里实人认证Token
     *
     * @param deviceNo 设备号（使用手机IMEI号）
     * @param imei     手机IMEI号
     * @return 返回认证Token
     */
    public Observable<String> getVerifyToken(String deviceNo, String imei) {
        return client.<AliResult>aliSecurityRequest(deviceNo, imei, TOKEN_REQUEST, AliResult.class).map(new Func1<AliResult, String>() {
            @Override
            public String call(AliResult ret) {
                return ret.getToken();
            }
        });
    }

    /**
     * 获取认证结果
     *
     * @param deviceNo 设备号（使用手机IMEI号）
     * @param imei     手机IMEI号
     * @return 返回认证结果：1;初始 2;认证通过 3;认证失败 4;转人工 5;未认证用户取消
     */
    public Observable<Integer> getVerifyState(String deviceNo, String imei) {
        return client.<AliResult>aliSecurityRequest(deviceNo, imei, RESULT_REQUEST, AliResult.class).map(new Func1<AliResult, Integer>() {
            @Override
            public Integer call(AliResult ret) {
                return ret.getState();
            }
        });
    }
}
