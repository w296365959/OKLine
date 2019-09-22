package com.vboss.okline.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.cosw.sdkblecard.DeviceInfo;
import com.hwangjr.rxbus.RxBus;
import com.vboss.okline.R;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.OKCard;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.home.IFragmentSwitcher;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.view.widget.OKCardView;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/28 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public abstract class BaseActivity<P extends BasePresenter, M extends BaseModel> extends AppCompatActivity implements IFragmentSwitcher {
    private static final String TAG = "BaseActivity";
    /**
     * 没有连接网络
     */
    public static final int NETWORK_NONE = -1;
    public static final int OCARD_STATE_NOT_BOND = 0;
    public static final int OCARD_STATE_BOND = 1;
    public static final int OCARD_STATE_NOT_CONNECTED = 2;
    public static final int OCARD_STATE_IPSS_INVALID = 3;
    public static final String ACTION_OCARD_STATE_CHANGED = "com.vboss.okline.action.OCARD_STATE_CHANGED";
    private static final int REQUEST_CODE_PERMISSION = 597;
    public static final String OCARD_STATE = "ocard_state";
    public static final int STOP_ANIMATION_DELAY_MILLIS = 15 * 1000;
    public static int lastState = 0;
    private static int activityCount = 0;
    private static boolean isBatteryMonitoring;
    private User user;
    public static OKCard okCard;
    private static int ocardConnectionState;
    private UserRepository userRepository;
    private MediaPlayer mediaPlayer;
    private long start1;
    private long end1;
    private Subscription requestOCardConnection;
    private long start2;
    private long end2;
    public static String okCardBhtAddress;
    private boolean aBoolean;
    public static DeviceInfo deviceInfo;
    private static Handler stopAnimationHandler;
    private static Runnable stopAnimationRunnable;
    private Subscription getOCard;
    private static Subscription monitorOCardBattery;

    public static int getOcardBattery() {
        return ocardBattery;
    }

    public static void setOcardBattery(int ocardBattery) {
        BaseActivity.ocardBattery = ocardBattery;
    }

    private static int ocardBattery;

    public static int getOcardState() {
        return ocardState;
    }

    public static void setOcardState(int ocardState) {
        BaseActivity.ocardState = ocardState;
    }

    private static int ocardState = OCARD_STATE_NOT_BOND;
    private Subscription monitorOcard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = UserRepository.getInstance(this);
        user = userRepository.getUser();
        activityCount++;
        Utils.showLog(TAG, "activityCount = " + activityCount);
        RxBus.get().register(this);
        if (activityCount == 1) {
            monitorOcard = userRepository.monitorOCardConnection()
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultSubscribe<Boolean>(TAG) {
                        @Override
                        public void onStart() {
                            super.onStart();
                            Utils.showLog(TAG, "开始监测欧卡状态");
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            throwable.printStackTrace();
                            String message = throwable.getMessage();
                            Utils.showLog(TAG, "欧卡状态监测出错：" + message);
                            if (TextUtils.isEmpty(message)) {
                                initialOcardState();
                            }
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            super.onNext(aBoolean);
                            Utils.showLog(TAG,"aBoolean = [" + aBoolean + "]");
                            Utils.showLog(TAG,"BaseActivity.this.aBoolean："+BaseActivity.this.aBoolean);
                            if (BaseActivity.this.aBoolean != aBoolean) {
                                BaseActivity.this.aBoolean = aBoolean;
                                Utils.showLog(TAG, "★ 欧卡状态变化：" + (aBoolean ? "已连接★" : "已掉线★  当前欧卡状态："+ocardState));
                                ocardConnectionState = aBoolean ? 1 : 2;
                                if (ocardState == OCARD_STATE_BOND && !aBoolean) {
                                    if (isBatteryMonitoring) {
                                        pauseBatteryMonitor();
                                    }
                                    deviceInfo = null;
                                    ocardBattery = 0;
                                    OKCardView.inShakingMode = true;
                                    stopAnimationHandler = new Handler();
                                    stopAnimationRunnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            Utils.showLog(TAG, "15秒截止，发送广播停止动画！");
                                            sendBroadcast(new Intent(OKCardView.ACTION_ANIMATION_STOP));
                                            OKCardView.inShakingMode = false;
                                            if (mediaPlayer.isPlaying() || mediaPlayer.isPlaying()) {
                                                mediaPlayer.stop();
                                                mediaPlayer.release();
                                            }
                                        }
                                    };
                                    stopAnimationHandler.postDelayed(stopAnimationRunnable, STOP_ANIMATION_DELAY_MILLIS);

                                    try {
                                        Utils.showLog(TAG,"播放报警音频");
                                        mediaPlayer = MediaPlayer.create(BaseActivity.this, R.raw.emergency_alarm);
                                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                        mediaPlayer.setVolume(0.3f, 0.3f);
                                        mediaPlayer.setLooping(true);
                                        mediaPlayer.start();
                                    } catch (Exception e) {
                                        Utils.showLog(TAG,"音频播放失败："+e.getMessage());
                                    }
                                }
                                initialOcardState();
                            }
                        }
                    });
        }
        initialOcardState();
    }

    private static void startBatteryMonitor(final Context context) {
        monitorOCardBattery = UserRepository.getInstance(context).monitorOCardBattery(30 * 1000)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<Integer>(TAG) {
                    @Override
                    public void onStart() {
                        super.onStart();
                        isBatteryMonitoring = true;
                        Utils.showLog(TAG,"○开始监测欧卡电量monitorOCardBattery");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Utils.showLog(TAG,"○查询电量出错："+throwable.getMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        super.onNext(integer);
                        Utils.showLog(TAG,"○查询欧卡电量："+integer);
                        if (ocardBattery != integer) {
                            ocardBattery = integer;
                            Utils.showLog(TAG, "○发送广播:[电量：" + BaseActivity.getOcardBattery() + "]  [欧卡状态：" + BaseActivity.getOcardState() + "]");
                            Intent intent = new Intent(BaseActivity.ACTION_OCARD_STATE_CHANGED);
                            intent.putExtra(OCARD_STATE, ocardState);
                            context.sendBroadcast(intent);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        }
                    }
                });
    }

    private static void pauseBatteryMonitor(){
        if (monitorOCardBattery != null) {
            Utils.showLog(TAG,"中止电量查询");
            isBatteryMonitoring= false;
            Utils.unsubscribeRxJava(monitorOCardBattery);
        }
    }

    /**
     * 进入下一级
     *
     * @param fClass  目标Fragment的class
     * @param args    要携带的参数
     * @param current
     */
    @Override
    public void goNext(Class<? extends Fragment> fClass, Bundle args, Fragment current) {

    }

    /**
     * 返回上一级
     */
    @Override
    public void goBack() {

    }

    /**
     * 跳到纵深的顶部
     */
    @Override
    public void goTop() {

    }

    private void initialOcardState() {
        start1 = System.currentTimeMillis();
        getOCard = UserRepository.getInstance(this).getOCard()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<OKCard>(TAG) {
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        com.okline.vboss.assistant.ui.recharge.Utils.unsubscribeRxJava(getOCard);
                        end1 = System.currentTimeMillis();
                        Utils.showLog(TAG, "UserRepository.getInstance(this).getOCard()接口耗时：" + (end1 - start1));
                        Utils.showLog(TAG, "获取欧卡信息出错：" + throwable.getMessage());
                        ocardState = ocardConnectionState;
                        sendBroadcast();
                    }

                    @Override
                    public void onNext(OKCard okCard) {
                        super.onNext(okCard);
                        com.okline.vboss.assistant.ui.recharge.Utils.unsubscribeRxJava(getOCard);
                        okCardBhtAddress = okCard.getBhtAddress();
                        end1 = System.currentTimeMillis();
                        Utils.showLog(TAG, "UserRepository.getInstance(this).getOCard()接口耗时：" + (end1 - start1));
                        BaseActivity.okCard = okCard;
                        Utils.showLog(TAG, "获取欧卡信息：" + okCard);
                        processOcardState();
                    }
                });
    }

    private void processOcardState() {
        Utils.showLog(TAG, "BaseActivity.processOcardState");
        if (okCard.getIsBind() != 1) {
            ocardState = OCARD_STATE_NOT_BOND;
            sendBroadcast();
        } else {
            if (ocardConnectionState % 2 == 0) {
                ocardState = OCARD_STATE_NOT_CONNECTED;
                sendBroadcast();
            } else {
                ocardState = OCARD_STATE_BOND;
                //欧卡已连接
                if (!isBatteryMonitoring) {
                    startBatteryMonitor(BaseActivity.this);
                }

                if (stopAnimationHandler != null) {
                    stopAnimationHandler.removeCallbacks(stopAnimationRunnable);
                    stopAnimationHandler = null;
                    stopAnimationRunnable = null;
                }
                if (mediaPlayer != null) {
                    try {
                        if (mediaPlayer.isLooping() || mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
                OKCardView.inShakingMode = false;
                start2 = System.currentTimeMillis();
                String bhtAddress = UserRepository.getInstance(BaseActivity.this).getUser().getBhtAddress();
                requestOCardConnection = UserRepository.getInstance(BaseActivity.this).requestOCardConnection(BaseActivity.this, bhtAddress, 5 * 1000)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultSubscribe<DeviceInfo>(TAG) {
                            @Override
                            public void onError(Throwable throwable) {
                                super.onError(throwable);
                                Utils.showLog(TAG, "查询电量出错：" + throwable.getMessage());
                                Utils.unsubscribeRxJava(requestOCardConnection);
                                end2 = System.currentTimeMillis();
                                Utils.showLog(TAG, "requestOCardConnection接口耗时：" + (end2 - start2));
                                sendBroadcast();
                            }

                            @Override
                            public void onNext(DeviceInfo deviceInfo) {
                                super.onNext(deviceInfo);
                                BaseActivity.deviceInfo = deviceInfo;
                                ocardBattery = deviceInfo.getDumpEnergy();
                                Utils.unsubscribeRxJava(requestOCardConnection);
                                end2 = System.currentTimeMillis();
                                Utils.showLog(TAG, "requestOCardConnection接口耗时：" + (end2 - start2));
                                sendBroadcast();
                            }
                        });
            }
        }
    }

    public void sendBroadcast() {
        Utils.showLog(TAG, "发送广播:[电量：" + BaseActivity.getOcardBattery() + "]  [欧卡状态：" + BaseActivity.getOcardState() + "]");
        Intent intent = new Intent(BaseActivity.ACTION_OCARD_STATE_CHANGED);
        intent.putExtra(OCARD_STATE, ocardState);
        sendBroadcast(intent);
        LocalBroadcastManager.getInstance(BaseActivity.this).sendBroadcast(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            UserRepository.getInstance(this).detectOCard(user.getDeviceNo().substring(user.getDeviceNo().length() - 6), 10 * 1000)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultSubscribe<DeviceInfo>(TAG) {
                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                        }

                        @Override
                        public void onNext(DeviceInfo deviceInfo) {
                            super.onNext(deviceInfo);
                            ocardState = deviceInfo != null ? OCARD_STATE_BOND : OCARD_STATE_NOT_CONNECTED;
                            Intent intent = new Intent(BaseActivity.ACTION_OCARD_STATE_CHANGED);
                            sendBroadcast(intent);
                            LocalBroadcastManager.getInstance(BaseActivity.this).sendBroadcast(intent);
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
        activityCount--;
        Utils.showLog(TAG, "activityCount = " + activityCount);
        if (activityCount == 0) {
            Utils.showLog(TAG, "取消订阅欧卡状态");
            Utils.unsubscribeRxJava(monitorOcard);
            if (stopAnimationHandler != null) {
                stopAnimationHandler.removeCallbacks(stopAnimationRunnable);
            }
            userRepository.releaseOCard();
        }
    }

    protected int getNetWorkState() {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return activeNetworkInfo.getType();
        } else {
            return NETWORK_NONE;
        }
    }
}
