package com.vboss.okline.data.remote;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/9 <br/>
 * Summary : 在这里描述Class的主要功能
 */
class DefaultSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(T t) {
        if (t != null && t.getClass().isAssignableFrom(ArrayList.class)){
            int size = ((List)t).size();
            System.out.println("Data size = " + size);
            if (size != 0){

                System.out.print("First data info : " + ((List) t).get(0));
            }
        }else {
            System.out.println("Data info : " + t);
        }

    }


}
