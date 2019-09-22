package com.vboss.okline.ui.user;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.cosw.sdkblecard.DeviceInfo;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.OKCard;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.customized.CustomDialog;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/3/30
 * Summary : 欧卡页面Presenter
 */

public class OCardAttachPresenter extends OCardAttachContract.Presenter {

    private final UserRepository userRepository;
    private OCardAttachContract.View view;
    private Subscription scanOCard;
    private Handler handler = new Handler();
    private BluetoothAdapter bluetoothAdapter;
    private Runnable runnable;
    private Context context;
    private Subscription detectOCard;
    private Subscription bindBTCard;
    private DeviceInfo deviceInfo;

    public OCardAttachPresenter(OCardAttachContract.View view) {
        this.view = view;
        context = view.getContext();
        userRepository = UserRepository.getInstance(context);
    }

    @Override
    public void onAttached() {
        view.executeAttaching(0,false);
    }

    @Override
    void detectNFC() {
        NfcAdapter defaultAdapter = NfcAdapter.getDefaultAdapter(context);
        if (defaultAdapter != null) {
            if (!defaultAdapter.isEnabled()) {
                view.showDialog("NFC功能尚未开启","点击确认按钮前往开启",true,new CustomDialog.DialogClickListener(){
                    @Override
                    public void onPositiveClick() {
                        view.enableNFC();
                    }

                    @Override
                    public void onNegtiveClick() {
                        view.executeAttaching(1,false);
                    }
                });
            } else {
                view.executeAttaching(1, true);
            }
        } else {
            // NFC not available
                view.executeAttaching(1,false);
        }
    }

    private static final String TAG = "OCardAttachPresenter";

    @Override
    void detectBT() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (bluetoothAdapter.isEnabled()) {
                        Utils.showLog(TAG,"蓝牙已开启，开始检测输入");
                        view.executeAttaching(2,true);
                    } else {
                        view.showDialog("蓝牙未开启", "点击确定按钮开启蓝牙", false, new CustomDialog.DialogClickListener() {
                            @Override
                            public void onPositiveClick() {
                                boolean enable = BluetoothAdapter.getDefaultAdapter().enable();
                                if (enable) {
                                    view.executeAttaching(2,true);
                                } else {
                                    view.executeAttaching(2,false);
                                }
                            }

                            @Override
                            public void onNegtiveClick() {
                                view.executeAttaching(2,false);
                            }
                        });
                    }
                }
            };
            handler.postDelayed(runnable,1000);
        } else {
            // TODO: 2017/3/30 手机无蓝牙设备
            view.executeAttaching(2,false);
        }
    }

    @Override
    void scanOCard(int timeout, String filter) {
        scanOCard = UserRepository.getInstance(context).scanOCard(timeout, filter)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<DeviceInfo>(TAG) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        Utils.showLog(TAG,"扫描欧卡出错：扫描超时");
                        view.executeAttaching(3,false);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Utils.showLog(TAG,"扫描欧卡出错："+throwable.getMessage());
                        view.executeAttaching(3,false);
                        Utils.unsubscribeRxJava(scanOCard);
                    }

                    @Override
                    public void onNext(DeviceInfo deviceInfo) {
                        super.onNext(deviceInfo);
                        if (deviceInfo != null) {
                            view.executeAttaching(3,true);
                            Utils.unsubscribeRxJava(scanOCard);
                        } else {
                            onError(new Throwable("onNext返回的DeviceInfo为null"));
                        }
                    }
                });
    }

    @Override
    void detectOCard(String number) {
        Utils.showLog(TAG,"检测欧卡：number = [" + number + "]");
        MainActivity.inOCardBinding = true;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            detectOCard = userRepository.detectOCard(number, 30 * 1000).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultSubscribe<DeviceInfo>(TAG) {
                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                            onError(new Exception("扫描超时"));
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            Utils.showLog(TAG, "检测失败：" + throwable.getMessage());
                            view.executeAttaching(4,false);
                        }

                        @Override
                        public void onNext(DeviceInfo deviceInfo) {
                            super.onNext(deviceInfo);
                            OCardAttachPresenter.this.deviceInfo = deviceInfo;
                            Utils.showLog(TAG, "检测结果：欧卡蓝牙地址" + deviceInfo.getDeviceMac());
                            view.executeAttaching(4,true);
                            Utils.unsubscribeRxJava(detectOCard);
                        }
                    });
        } else {
            Utils.showLog(TAG,"未能获取用户定位权限");
            view.executeAttaching(4,false);
        }
    }

    @Override
    void onViewDestroy() {
        Utils.unsubscribeRxJava(scanOCard);
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        Utils.unsubscribeRxJava(detectOCard);
        Utils.unsubscribeRxJava(bindBTCard);
    }

    @Override
    void matchOCardNo(String number) {
        Utils.showLog(TAG,"开始绑卡："+number);
        bindBTCard = userRepository.bindBTCard(
                context,
                User.DEV_OCARD,
                deviceInfo.getDeviceName(),
                deviceInfo.getDeviceMac(),
                number,
                30*1000)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<OKCard>(TAG) {

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Utils.showLog(TAG, "绑卡结果onError出错：" + throwable.getMessage());
                        view.executeAttaching(5,false);
                    }

                    @Override
                    public void onNext(OKCard okCard) {
                            super.onNext(okCard);
                            Utils.showLog(TAG, "绑卡结果onNext成功：" + okCard.toString());
                            view.saveCardInfo(okCard);
                            view.executeAttaching(5,true);
                        Utils.unsubscribeRxJava(bindBTCard);
                    }
                });
    }
}
