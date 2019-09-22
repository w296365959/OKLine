package com.vboss.okline.ui.card.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.OCardAttachFragment;
import com.vboss.okline.ui.user.OcardFragment;

import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/6/20 14:22 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

public class LogoView extends FrameLayout implements View.OnClickListener {
    private static final String TAG = LogoView.class.getSimpleName();
    //欧卡状态广播发送action
    public static final String ACTION_OCARD_STATE_CHANGED = "com.vboss.okline.action.OCARD_STATE_CHANGED";
    public static final String KEY_OCARD_STATE = "ocard_state";
    //欧卡绑定
    public static final int OCARD_BIND = 0x001;
    //欧卡未绑定
    public static final int OCARD_NO_BIND = 0x002;
    //欧卡是否断开
    public boolean isConnected = false;
    //Animation
    private ObjectAnimator animator;
    //欧卡状态
    private int state;
    private Context mContext;
    //欧卡状态
    public static final int OCARD_STATE_NOT_BIND = 0;   // 欧卡未绑定
    public static final int OCARD_STATE_BIND = 1;     //欧卡绑定并连接
    public static final int OCARD_STATE_NOT_CONNECTED = 2;   //欧卡未连接
    public static final int OCARD_STATE_IPSS_INVALID = 3;    //IPSS 未连接

    private MainActivity mMainActivity;

    ImageView iv_logo;
    ImageView iv_animation;

    public LogoView(@NonNull Context context) {
        this(context, null);
    }

    public LogoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LogoView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View convertView = LayoutInflater.from(context).inflate(R.layout.layout_app_logo, this, false);
        iv_logo = (ImageView) convertView.findViewById(R.id.iv_logo);
        iv_animation = (ImageView) convertView.findViewById(R.id.logo_animation);
        iv_animation.setVisibility(GONE);

        animator = ObjectAnimator.ofFloat(iv_animation, "rotation", 0f, 360f);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
        addView(convertView);
        //Added by wangshuai 2017-06-22
        this.mContext = context;
        IntentFilter filter = new IntentFilter(ACTION_OCARD_STATE_CHANGED);
        context.registerReceiver(receiver, filter);
        showOCardState(BaseActivity.getOcardState());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    /**
     * setting logo
     *
     * @param resId int resource
     */
    public void setActionbarLogo(@DrawableRes int resId) {
        if (resId == 0) {
            Timber.tag(TAG).w("drawable id is not 0");
            return;
        }
        iv_logo.setImageResource(resId);
    }

    /**
     * setting OCard state
     *
     * @param state int
     *              {@link LogoView#OCARD_BIND}
     *              {@link LogoView#OCARD_NO_BIND}
     * @deprecated Use BroadcastReceiver {@link LogoView#receiver}
     */
    @Deprecated
    public void setOCardState(int state) {
        this.state = state;
        if (state == OCARD_BIND) {
            iv_animation.setImageResource(R.drawable.shape_progress_orange);
            setActionbarLogo(R.mipmap.logo2);
        } else if (state == OCARD_NO_BIND) {
            iv_animation.setImageResource(R.drawable.shape_progress_green);
            iv_animation.setVisibility(GONE);
            setActionbarLogo(R.mipmap.logo);
        }
    }

    /**
     * @deprecated Use BroadcastReceiver {@link LogoView#receiver}
     */
    @Deprecated
    public void oCardContacted() {
        iv_animation.setVisibility(GONE);
        if (animator != null) {
            animator.cancel();
        }
        setOCardState(state);
    }

    /**
     * @deprecated Use BroadcastReceiver {@link LogoView#receiver}
     */
    @Deprecated
    public void oCardOutContacted() {
        if (state == OCARD_NO_BIND) {
            iv_animation.setVisibility(GONE);
        } else {
            iv_animation.setVisibility(VISIBLE);
        }
        if (animator != null) {
            animator.start();
        }
        setOCardState(state);
    }

    public void onResume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (animator != null) {
                animator.resume();
            }
        }
    }

    public void onPause() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (animator != null) {
                animator.pause();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //Added by wangshuai 2017-06-22
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
        }
        if (animator != null) {
            animator = null;
        }
    }

    /***
     * author wangshuai
     * date   2017/6/22 15:28
     * Ocard state receiver
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //modify by wangshuai 2017-06-23 avoid NullPointException
            if (intent != null) {
                Timber.tag(TAG).i("intent action %s", intent.getAction());
                Bundle args = intent.getExtras();
                if (args != null) {
                    Timber.tag(TAG).i("bundle %s", args.toString());
                    int state = intent.getExtras().getInt(KEY_OCARD_STATE);
                    Timber.tag(TAG).i("state %s", state);
                    showOCardState(state);
                } else {
                    Timber.tag(TAG).w(" bundle is null");
                }
            } else {
                Timber.tag(TAG).w("intent is null ");
            }
        }
    };

    private void showOCardState(int state) {
        switch (state) {
            case OCARD_STATE_BIND:
                //欧卡绑定并连接
                bindOCard();
                break;
            case OCARD_STATE_NOT_BIND:
                //欧卡未绑定
                unbindOCard();
                break;
            case OCARD_STATE_NOT_CONNECTED:
                //欧卡未连接
                unConnectOCard();
                break;
            case OCARD_STATE_IPSS_INVALID:
                //IPSS 未连接
                break;

            default:
                break;
        }
    }

    /*** 
     * author wangshuai
     * date   2017/6/22 15:47
     * desc ocard bind but not connected to bluetooth
     */
    private void unConnectOCard() {
        iv_logo.setImageResource(R.mipmap.logo2);
        iv_animation.setImageResource(R.drawable.shape_progress_orange);
        iv_animation.setVisibility(VISIBLE);
        if (animator != null) {
            animator.start();
        }
    }

    /*** 
     * author wangshuai
     * date   2017/6/22 15:47
     * desc ocard unbind and not connected to bluetooth
     */
    private void unbindOCard() {
        iv_logo.setImageResource(R.mipmap.logo);
        iv_animation.setVisibility(GONE);
        if (animator != null && animator.isStarted()) {
            animator.cancel();
        }
    }

    /*** 
     * author wangshuai
     * date   2017/6/22 15:47
     * desc ocard bind and connected to bluetooth
     */
    private void bindOCard() {
        iv_logo.setImageResource(R.mipmap.logo2);
        iv_animation.setVisibility(GONE);
        if (animator != null && animator.isStarted()) {
            animator.cancel();
        }
    }

    public void setOnClickListener(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (BaseActivity.getOcardState() == BaseActivity.OCARD_STATE_BOND) {
            mMainActivity.addSecondFragment(OcardFragment.newInstance());
        }
        if (BaseActivity.getOcardState() == BaseActivity.OCARD_STATE_NOT_BOND) {
            mMainActivity.addSecondFragment(new OCardAttachFragment());
        }
        if (BaseActivity.getOcardState() == BaseActivity.OCARD_STATE_NOT_CONNECTED) {
            mMainActivity.addSecondFragment(OcardFragment.newInstance());
        }
    }
}
