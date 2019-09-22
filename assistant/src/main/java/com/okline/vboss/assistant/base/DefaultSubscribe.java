package com.okline.vboss.assistant.base;

import rx.Subscriber;
import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/6/8 15:10 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

public class DefaultSubscribe<T> extends Subscriber<T> {
    private String TAG = "";

    public DefaultSubscribe(String tag) {
        this.TAG = tag;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Timber.tag(TAG).e(e.toString());
    }

    @Override
    public void onNext(T t) {

    }
}
