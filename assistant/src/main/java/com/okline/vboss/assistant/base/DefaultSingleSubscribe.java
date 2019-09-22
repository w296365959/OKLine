package com.okline.vboss.assistant.base;

import android.text.TextUtils;
import android.util.Log;

import rx.Subscriber;
import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/7/6 17:13 <br/>
 * Summary  : RxJava Single Subscribe only one execute onNext() or onError();
 */

public class DefaultSingleSubscribe<T> extends Subscriber<T> {

    private String TAG;

    public DefaultSingleSubscribe(String TAG) {
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
        if (!isUnsubscribed()) {
            unsubscribe();
            Timber.tag(TAG).i("onError unsubscribe");
        }
    }

    @Override
    public void onNext(T t) {
        if (!isUnsubscribed()) {
            unsubscribe();
            Timber.tag(TAG).i("onNext unsubscribe");
        }
    }

}
