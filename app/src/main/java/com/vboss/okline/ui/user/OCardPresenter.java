package com.vboss.okline.ui.user;

import android.content.SharedPreferences;

import com.cosw.sdkblecard.DeviceInfo;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.OKCard;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.callback.OCadAbsenceDeclarationListener;
import com.vboss.okline.ui.user.callback.OCardInfoListener;
import com.vboss.okline.ui.user.callback.OCardResumeListener;

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

public class OCardPresenter extends OCardContract.Presenter {

    private final MainActivity context;
    private final UserRepository userRepository;
    private  OCardContract.View view;
    private static final String TAG = "OCardPresenter";
    private Subscription getOCardInfo;
    private Subscription resumeOCard;
    private Subscription lossOCard;

    public OCardPresenter(OCardContract.View view, MainActivity activity) {
        this.view = view;
        this.context = activity;
        userRepository = UserRepository.getInstance(activity);
    }

    @Override
    public void onAttached() {
//        getOCardInfo();
    }

    @Override
    void getOCardInfo(final OCardInfoListener listener) {
        listener.onStart();
        if (BaseActivity.deviceInfo == null) {
            UserRepository userRepository = UserRepository.getInstance(context);
            User user = userRepository.getUser();
            String bhtAddress = user.getBhtAddress();
            Utils.showLog(TAG,"待检测蓝牙地址："+bhtAddress);
            getOCardInfo = userRepository.requestOCardConnection(context, bhtAddress, 10 * 1000)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultSubscribe<DeviceInfo>(TAG) {
                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            Utils.showLog(TAG, "检测蓝牙状态出错：" + throwable.getMessage());
                            listener.onErr(throwable.getMessage());
                        }

                        @Override
                        public void onNext(DeviceInfo deviceInfo) {
                            super.onNext(deviceInfo);
                            Utils.showLog(TAG, "欧卡蓝牙连接状态：" + deviceInfo);
                            com.okline.vboss.assistant.ui.recharge.Utils.unsubscribeRxJava(getOCardInfo);
                            if (deviceInfo != null) {
                                SharedPreferences.Editor editor = context.sharedPreferences.edit();
                                editor.putString(UserFragment.AVAILABLE_VOLUME, "93KB");
                                editor.putString(UserFragment.TOTAL_VOLUME, "180KB");
                                editor.putString(UserFragment.BATTERY_VOLUME, deviceInfo.getDumpEnergy() + "%");
                                editor.putString(UserFragment.VERSION_INFO, deviceInfo.getDeviceCosVersion());
                                editor.apply();
                            }
                            listener.onFetch(deviceInfo);
                        }
                    });
        } else {
            SharedPreferences.Editor editor = context.sharedPreferences.edit();
            editor.putString(UserFragment.AVAILABLE_VOLUME, "93KB");
            editor.putString(UserFragment.TOTAL_VOLUME, "180KB");
            editor.putString(UserFragment.BATTERY_VOLUME, BaseActivity.deviceInfo.getDumpEnergy() + "%");
            editor.putString(UserFragment.VERSION_INFO, BaseActivity.deviceInfo.getDeviceCosVersion());
            editor.apply();
            listener.onFetch(BaseActivity.deviceInfo);
        }
    }

    @Override
    void declareAbsence(int deviceType, String deviceNo, final OCadAbsenceDeclarationListener listener) {
        Utils.showLog(TAG,"欧卡挂失开始：deviceType = [" + deviceType + "], deviceNo = [" + deviceNo + "], listener = [" + listener + "]");
        lossOCard = userRepository.lossOCard(deviceType, deviceNo)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<OKCard>(TAG) {
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Utils.showLog(TAG, "挂失出错：" + throwable.getMessage());
                        listener.onFail(throwable.getMessage(), null);
                    }

                    @Override
                    public void onNext(OKCard okCard) {
                        super.onNext(okCard);
                        Utils.showLog(TAG, "挂失成功：" + okCard.toString());
                        listener.onSuccess(okCard);
                    }
                });
    }

    @Override
    void resume(int deviceType, String deviceNo, final OCardResumeListener listener) {
        Utils.showLog(TAG,"欧卡重启：deviceType = [" + deviceType + "], deviceNo = [" + deviceNo + "]");
        resumeOCard = userRepository.resumeOCard(deviceType, deviceNo).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<OKCard>(TAG) {
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Utils.showLog(TAG, "操作出错：" + throwable.getMessage());
                        listener.onFail();
                    }

                    @Override
                    public void onNext(OKCard okCard) {
                        super.onNext(okCard);
                        Utils.showLog(TAG, "操作成功：" + okCard.toString());
                        listener.onSuccess(okCard);
                    }
                });

    }

    @Override
    void onViewDestroy() {
        Utils.unsubscribeRxJava(getOCardInfo);
        Utils.unsubscribeRxJava(resumeOCard);
        Utils.unsubscribeRxJava(lossOCard);
    }
}
