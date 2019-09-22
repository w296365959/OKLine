package com.vboss.okline.ui.contact.callPhone;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.LinearLayout;

import com.vboss.okline.R;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.utils.ToastUtil;

import butterknife.BindView;
import timber.log.Timber;

import static com.vboss.okline.R.id.chronometerView;
import static com.vboss.okline.ui.contact.group.CreateGroupPresenter.TAG;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/23 17:02
 * Desc :
 */

public class CallingService extends Service {

    private WindowManager wm;
    private View view;
    private WindowManager.LayoutParams params;
    private int mEndX;
    private int mEndY;
    private int mStartY;
    private int mStartX;
    private LinearLayout llMinimizeContent;
    private Chronometer chronometer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.minimize_window, null);
        llMinimizeContent = (LinearLayout) view.findViewById(R.id.ll_minimize_content);
        chronometer = (Chronometer) view.findViewById(chronometerView);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        int screenW = getResources().getDisplayMetrics().widthPixels;
        int screenH = getResources().getDisplayMetrics().heightPixels;


        // TYPE为Toast类型，Toast无需权限
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(screenW / 10, screenW / 10,
//                WindowManager.LayoutParams.TYPE_TOAST, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.RGBA_8888);
//        params.x = screenW / 2 - screenW / 20;
//        params.y = screenH / 2 - screenW / 20;
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_TOAST);
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wm.addView(view, params);
        initClick();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initClick() {
        llMinimizeContent.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mStartX = (int) event.getRawX();
                        mStartY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mEndX = (int) event.getRawX();
                        mEndY = (int) event.getRawY();
                        Timber.tag(TAG).i("onTouch: RawX: %s  RawY: %s",mEndX,mEndY);
                        if (needIntercept()) {
                            //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                            params.x = (int) event.getRawX() - view.getMeasuredWidth() / 2;
                            params.y = (int) event.getRawY() - view.getMeasuredHeight() / 2;
                            wm.updateViewLayout(view, params);
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (needIntercept()) {
                            return true;
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        //TODO 此处点击事件不灵敏 应该是下面拦截的问题
        llMinimizeContent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CallingService.this, CallingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                wm.removeView(view);
                ToastUtil.show(CallingService.this,"我被点击了");
            }
        });
    }


    /**
     * 是否拦截
     * @return true:拦截;false:不拦截.
     */
    private boolean needIntercept() {
        if (Math.abs(mStartX - mEndX) > 30 || Math.abs(mStartY - mEndY) > 30) {
            return true;
        }
        return false;
    }


        @Override
        public void onDestroy () {
            chronometer.stop();
            wm.removeView(view);
            Timber.tag(TAG).i("onDestroy");
            super.onDestroy();
        }
    }
