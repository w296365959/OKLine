package com.vboss.okline.tsm;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.annotation.NonNull;

import rx.Observable;
import rx.functions.Func1;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/5/9 <br/>
 * Summary : 卡片应用安装类
 */
public class CardInstaller {
    private static final String TAG = CardInstaller.class.getSimpleName();
    private final String btAddress;
    private final OCardService cardService;

    public CardInstaller(@NonNull Context context, @NonNull String btAddress) {
        if (!BluetoothAdapter.checkBluetoothAddress(btAddress)) {
            throw new IllegalArgumentException("Bluetooth address is not valid");
        }
        this.btAddress = btAddress;
        this.cardService = OCardService.getInstance(context);
    }

    /**
     * 监听蓝牙连接状态
     *
     * @return 返回蓝牙卡连接状态
     */
    public Observable<OCardService.Connection> getConnection() {
        return cardService.getConnection();
    }

    /**
     * 开卡应用安装
     *
     * @param context
     * @param tsmCardType TSM平台卡片类型
     * @param appAid      卡应用aid
     * @return 返回卡号
     */
    public Observable<String> cardAppInstall(Context context, final int tsmCardType, final String appAid) {
        return checkBTConnectionState(context).concatMap(new Func1<Boolean, Observable<? extends Boolean>>() {
            @Override
            public Observable<? extends Boolean> call(Boolean aBoolean) {
                return cardService.appDelete(appAid);
            }
        }).flatMap(new Func1<Boolean, Observable<String>>() {
            @Override
            public Observable<String> call(Boolean aBoolean) {
                return cardService.appInstall(tsmCardType, appAid);
            }
        });
    }

    /**
     * 检查蓝牙是否连接
     */
    private Observable<Boolean> checkBTConnectionState(Context context) {
        return cardService.connectAndInitialize(context, btAddress, 20 * 1000);
    }

    /**
     * 销毁OCard相关服务
     */
    public void destroy() {
        cardService.destroy();
    }

}
