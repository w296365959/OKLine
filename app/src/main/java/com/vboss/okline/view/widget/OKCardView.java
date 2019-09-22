package com.vboss.okline.view.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cosw.sdkblecard.DeviceInfo;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.TextUtils;

import java.util.ArrayList;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/6/20
 * Summary : 在这里描述Class的主要功能
 */

public class OKCardView extends RelativeLayout {

    public static final String COLOR_STRING = "#25A613";
    public static final String ACTION_ANIMATION_STOP = "com.vboss.okline.action.OCARD_ANIMATION_STOP";
    public static final int THRESHOLD_RAPID_ALARM = 10;
    public static final int THRESHOLD_SLOW_ALARM = 20;
    private final Context context;
    private final ArrayList<LinearLayout> linearLayouts;
    private final BroadcastReceiver broadcastReceiver;
    private final BroadcastReceiver stopAnimationReceiver;
    private Subscription requestOCardConnection;
    private long start2;
    public static boolean inShakingMode;
    LinearLayout llBatteryVolume0;
    LinearLayout llBatteryVolume1;
    LinearLayout llBatteryVolume2;
    LinearLayout llBatteryVolume3;
    LinearLayout llBatteryVolume4;
    LinearLayout llBatteryVolume5;
    LinearLayout llBatteryVolume6;
    LinearLayout llBatteryVolume7;
    LinearLayout llBatteryVolume8;
    LinearLayout llBatteryVolume9;
    RelativeLayout rlBatteryOutline;
    ImageView ivCharging;
    TextView tvBattery;
    private Handler handler;
    private Runnable runnable;
    private long end2;
    private ObjectAnimator anim;
    private final String fragmentName;

    public OKCardView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = View.inflate(context, R.layout.view_okcard, null);
        tvBattery = (TextView) view.findViewById(R.id.tv_battery);
        ivCharging = (ImageView) view.findViewById(R.id.iv_charging);
        rlBatteryOutline = (RelativeLayout) view.findViewById(R.id.rl_battery_outline);
        llBatteryVolume0 = (LinearLayout) view.findViewById(R.id.ll_battery_volume0);
        llBatteryVolume1 = (LinearLayout) view.findViewById(R.id.ll_battery_volume1);
        llBatteryVolume2 = (LinearLayout) view.findViewById(R.id.ll_battery_volume2);
        llBatteryVolume3 = (LinearLayout) view.findViewById(R.id.ll_battery_volume3);
        llBatteryVolume4 = (LinearLayout) view.findViewById(R.id.ll_battery_volume4);
        llBatteryVolume5 = (LinearLayout) view.findViewById(R.id.ll_battery_volume5);
        llBatteryVolume6 = (LinearLayout) view.findViewById(R.id.ll_battery_volume6);
        llBatteryVolume7 = (LinearLayout) view.findViewById(R.id.ll_battery_volume7);
        llBatteryVolume8 = (LinearLayout) view.findViewById(R.id.ll_battery_volume8);
        llBatteryVolume9 = (LinearLayout) view.findViewById(R.id.ll_battery_volume9);
        linearLayouts = new ArrayList<>();
        linearLayouts.add(llBatteryVolume0);
        linearLayouts.add(llBatteryVolume1);
        linearLayouts.add(llBatteryVolume2);
        linearLayouts.add(llBatteryVolume3);
        linearLayouts.add(llBatteryVolume4);
        linearLayouts.add(llBatteryVolume5);
        linearLayouts.add(llBatteryVolume6);
        linearLayouts.add(llBatteryVolume7);
        linearLayouts.add(llBatteryVolume8);
        linearLayouts.add(llBatteryVolume9);

        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.OKCardView);
        fragmentName = styledAttributes.getString(R.styleable.OKCardView_fragment_name);
        Utils.showLog(TAG,"所属Activity或Fragment:"+fragmentName);

        //注册欧卡状态广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BaseActivity.ACTION_OCARD_STATE_CHANGED);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onOcardStateChanged();
            }
        };
        context.registerReceiver(broadcastReceiver, intentFilter);

        //注册晃动动画停止广播
        stopAnimationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Utils.showLog(TAG, "收到广播停止动画");
                if (anim != null) {
                    anim.end();
                    clearAnimation();
                }
                setVisibility(GONE);
            }
        };
        context.registerReceiver(stopAnimationReceiver, new IntentFilter(ACTION_ANIMATION_STOP));

        String bhtAddress = UserRepository.getInstance(context).getUser().getBhtAddress();
        if (!TextUtils.isEmpty(bhtAddress) && !"OcardFragment".equals(fragmentName)) {
            start2 = System.currentTimeMillis();
            requestOCardConnection = UserRepository.getInstance(context).requestOCardConnection(context, bhtAddress, 5 * 1000)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultSubscribe<DeviceInfo>(TAG) {
                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                            Utils.showLog(TAG, "requestOCardConnection onCompleted");
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            Utils.showLog(TAG, "查询电量出错：" + throwable.getMessage());
                            Utils.unsubscribeRxJava(requestOCardConnection);
                            end2 = System.currentTimeMillis();
                            Utils.showLog(TAG, "requestOCardConnection接口耗时：" + (end2 - start2));
                        }

                        @Override
                        public void onNext(DeviceInfo deviceInfo) {
                            super.onNext(deviceInfo);
                            BaseActivity.setOcardBattery(deviceInfo.getDumpEnergy());
                            Utils.unsubscribeRxJava(requestOCardConnection);
                            end2 = System.currentTimeMillis();
                            Utils.showLog(TAG, "requestOCardConnection接口耗时：" + (end2 - start2));
                            Utils.showLog(TAG, "发送广播:[电量：" + BaseActivity.getOcardBattery() + "]  [欧卡状态：" + BaseActivity.getOcardState() + "]");
                            Intent intent = new Intent(BaseActivity.ACTION_OCARD_STATE_CHANGED);
                            intent.putExtra(BaseActivity.OCARD_STATE, BaseActivity.getOcardState());
                            context.sendBroadcast(intent);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        }
                    });
        }
        onOcardStateChanged();
        addView(view);
    }

    private void onOcardStateChanged() {
        Utils.showLog(TAG, "自定义控件接收到广播:[电量：" + BaseActivity.getOcardBattery() + "]  [欧卡状态：" + BaseActivity.getOcardState() + "]");
        switch (BaseActivity.getOcardState()) {
            case BaseActivity.OCARD_STATE_BOND:
                if (anim != null && anim.isRunning()) {
                    anim.end();
                    clearAnimation();
                }
                break;
            case BaseActivity.OCARD_STATE_NOT_BOND:
                setVisibility(View.GONE);
                break;
            case BaseActivity.OCARD_STATE_IPSS_INVALID:

                break;
            case BaseActivity.OCARD_STATE_NOT_CONNECTED:
                if (inShakingMode) {
                    setVisibility(VISIBLE);
                    startSharkAnimation();
                } else {
                    setVisibility(GONE);
                }
                break;
        }
        setBatteryVolume(BaseActivity.getOcardBattery(), false);
    }

    /**
     * 设置电量与充电状态
     *
     * @param batteryVolume 电量，0-100内整数
     * @param inCharging    是否处于充电状态
     */
    public void setBatteryVolume(int batteryVolume, boolean inCharging) {
        Utils.showLog(TAG, "设置电量：batteryVolume = [" + batteryVolume + "], inCharging = [" + inCharging + "]");
        if (tvBattery != null) {
            String text = batteryVolume != 0 ? (batteryVolume + "%") : "";
            tvBattery.setText(text);
            tvBattery.setTextColor((0 < batteryVolume && batteryVolume <= THRESHOLD_SLOW_ALARM) ? Color.RED : Color.WHITE);
            int i = batteryVolume / 10;
            for (int j = 0; j < linearLayouts.size(); j++) {
                if (j < i) {
                    linearLayouts.get(j).setBackgroundColor(batteryVolume <= THRESHOLD_SLOW_ALARM ? Color.RED : Color.parseColor(COLOR_STRING));
                } else {
                    linearLayouts.get(j).setBackgroundColor(Color.BLACK);
                }
            }
            ivCharging.setVisibility(inCharging ? VISIBLE : INVISIBLE);
            if (handler != null) {
                handler.removeCallbacks(runnable);
            }
            if (inCharging && batteryVolume == 100) {
                setVisibility(VISIBLE);
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        rlBatteryOutline.setBackgroundResource(System.currentTimeMillis() / 1000 % 2 == 1 ? R.mipmap.battery_outline_green : R.mipmap.battery_outline);
                        handler.postDelayed(runnable, 1000);
                    }
                };
                handler.postDelayed(runnable, 1000);
            } else if (THRESHOLD_RAPID_ALARM < batteryVolume && batteryVolume <= THRESHOLD_SLOW_ALARM) {
                setVisibility(VISIBLE);
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        rlBatteryOutline.setBackgroundResource(System.currentTimeMillis() / 1000 % 2 == 1 ? R.mipmap.battery_outline_red : R.mipmap.battery_outline);
                        handler.postDelayed(runnable, 1000);
                    }
                };
                handler.postDelayed(runnable, 1000);
            } else if (0 < batteryVolume && batteryVolume <= THRESHOLD_RAPID_ALARM) {
                setVisibility(VISIBLE);
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        rlBatteryOutline.setBackgroundResource(System.currentTimeMillis() / 500 % 2 == 1 ? R.mipmap.battery_outline_red : R.mipmap.battery_outline);
                        handler.postDelayed(runnable, 500);
                    }
                };
                handler.postDelayed(runnable, 500);
            } else {
                Utils.showLog(TAG, "设置电量其他情况：" + batteryVolume + "  inShakingMode:" + inShakingMode);
                if (!inShakingMode) {
                    rlBatteryOutline.setBackgroundResource(R.mipmap.battery_outline);
                    setVisibility(GONE);
                }
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseCallbacks();
    }

    /**
     * 释放相关回调
     */
    private void releaseCallbacks() {
        clearAnimation();
        context.unregisterReceiver(broadcastReceiver);
        context.unregisterReceiver(stopAnimationReceiver);
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    /**
     * 欧卡图标晃动动画并播放警报音频
     */
    public void startSharkAnimation() {
        Utils.showLog(TAG, "开始晃动动画");
        if (getVisibility() != VISIBLE) {
            setVisibility(View.VISIBLE);
        }
        anim = ObjectAnimator.ofFloat(this, "rotation",
                -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f
                , -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f
                , -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f
                , -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f
                , -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f
                , -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f
                , -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f, -20f, 20f,0);
        anim.setDuration(BaseActivity.STOP_ANIMATION_DELAY_MILLIS);
        anim.setInterpolator(new LinearInterpolator());
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Utils.showLog(TAG, "动画开始");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Utils.showLog(TAG, "动画结束");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Utils.showLog(TAG, "动画取消");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

    private static final String TAG = "OKCardView";
}
