package com.vboss.okline.base;

import android.support.annotation.CallSuper;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/28 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public abstract class BasePresenter<M,V> {
    protected M mModel;
    protected V mView;

    public void setVM(M model, V view){
        mModel = model;
        mView = view;
    }

    public abstract void onAttached();

    @CallSuper
    public void onDetached(){

    }
}
