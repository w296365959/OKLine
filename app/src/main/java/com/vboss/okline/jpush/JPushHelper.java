package com.vboss.okline.jpush;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.hwangjr.rxbus.RxBus;
import com.vboss.okline.R;
import com.vboss.okline.data.CardRepository;
import com.vboss.okline.data.GsonUtils;

import java.lang.reflect.Field;
import java.util.List;

import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/13 <br/>
 * Summary : 解析JPush的数据，并做相应处理
 */
public class JPushHelper {
    private static final String TAG = JPushHelper.class.getSimpleName();
    private static final int CARD_NO_BALANCE = 3078;    //余额不足;
    public static final String KEY_JPUSH_DATA = "data";
    public static final String EVENT_JPUSH = "EVENT_JPUSH";
    private Context mContext;
    //Added by wangshuai 2017-05-15
    private CardRepository cardRepository;

    public JPushHelper(Context context) {
        mContext = context;
        //Added by wangshuai 2017-05-15
        cardRepository = CardRepository.getInstance(context);
    }

    /**
     * 分发JPush数据，是Notificatin还是PopWindow。
     *
     * @param title   push标题，
     * @param message push描述
     * @param data    push数据，json格式。
     */
    public void dispatchJPush(String title, String message, String data) {
        JPushEntity entity = null;
        try {
            entity = GsonUtils.fromJson(data, JPushEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (entity == null) {
            Timber.tag("JIGUANG").i("Pushed entity is null");
            return;
        }
        Timber.tag("JIGUANG").i("Pushed entity : %s", entity);

        //Added by wangshuai 2017-06-13 receiver jpush massage and post to message
        postJpushMessage(entity);

        if (isOKLineShown()) {
            showPushWindow(entity);
        } else {
            showNotification(title, message, entity);
        }
    }

    /**
     * 显示弹窗
     *
     * @param entity JPushEntity
     */
    private void showPushWindow(JPushEntity entity) {
        //modify by wangshuai open card success notice don't push widow
        if (OperatorType.OPEN == entity.getOperatorType()) {
            Timber.tag(TAG).i("open card notice don't push window,but we need refresh card list ");
            //Added by wangshuai refresh all card list
            cardRepository.prepareCardData();
            Timber.tag(TAG).i("refresh all card list");
            return;
        }

        Intent intent = new Intent(mContext, JPushActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_JPUSH_DATA, entity);
        mContext.startActivity(intent);
    }

    /**
     * post unread message
     *
     * @param entity JPushEntity
     */
    private void postJpushMessage(JPushEntity entity) {
        if (OperatorType.OPEN == entity.getOperatorType()) {
            Timber.tag(TAG).i("card open");
            return;
        }
        if (CARD_NO_BALANCE == entity.getErrorCode()) {
            Timber.tag(TAG).i("card no enough balance ");
            return;
        }
        RxBus.get().post(EVENT_JPUSH, entity);
    }

    /**
     * 显示通知
     *
     * @param title
     * @param message
     * @param entity
     */
    private void showNotification(String title, String message, JPushEntity entity) {
        //Added by wangshuai 2017-05-15
        if (OperatorType.OPEN == entity.getOperatorType()) {
            Timber.tag(TAG).i("open card notice don't push window,but we need refresh card list ");
            //Added by wangshuai refresh all card list
            cardRepository.prepareCardData();
            Timber.tag(TAG).i("refresh all card list");
        }

        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(mContext, JPushActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_JPUSH_DATA, entity);
        PendingIntent pendingIntent = PendingIntent.getActivities(mContext, 0,
                new Intent[]{new Intent(mContext, JPushActivity.class)}, 0);
        Notification notification = new Notification.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        nm.notify(1, notification);

    }

    /**
     * 当前app是否为OKLine
     *
     * @return
     */
    private boolean isOKLineShown() {
        final int PROCESS_STATE_TOP = 2;
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        //获取正在运行的进程
        boolean isShown = false;
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process : processInfos) {
            if (process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                try {
                    //判断OKLine进程是否为前台
                    Field processState = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
                    if (PROCESS_STATE_TOP == processState.getInt(process)) {
                        String[] pkgName = process.pkgList;
                        isShown = mContext.getPackageName().equals(pkgName[0]);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Timber.tag("JIGUANG").i("%s is shown", mContext.getPackageName());
        return isShown;
    }


}
