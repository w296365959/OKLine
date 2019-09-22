package com.hyphenate.easeui.widget;

import android.text.TextUtils;
import android.util.Log;

import rx.Subscriber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/4/8 9:33 <br/>
 * Summary  : RxJava 事件回调基类
 */

public class DefaultSubscribe<T> extends Subscriber<T> {
    private String TAG;

    public DefaultSubscribe(String TAG) {
        if (TextUtils.isEmpty(TAG)) {
            TAG = DefaultSubscribe.class.getSimpleName();
        }
        this.TAG = TAG;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        Log.e(TAG, throwable.getMessage(), throwable);
    }

    @Override
    public void onNext(T t) {

    }
}
