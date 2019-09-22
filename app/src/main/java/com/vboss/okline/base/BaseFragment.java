package com.vboss.okline.base;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MotionEvent;
import android.view.View;

import com.hwangjr.rxbus.RxBus;
import com.vboss.okline.ui.home.IFragmentSwitcher;
import com.vboss.okline.ui.home.MainActivity;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/28 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public abstract class BaseFragment extends Fragment implements View.OnTouchListener, IFragmentSwitcher {

    private static final String TAG = "BaseFragment";
    private IFragmentSwitcher fragmentSwitcher;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            fragmentSwitcher = (IFragmentSwitcher) activity;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 拦截触摸事件，防止泄露下去
        view.setOnTouchListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }

    /**
     * 进入下一级
     *
     * @param fClass
     * @param args
     * @param current
     */
    @Override
    public void goNext(Class<? extends Fragment> fClass, Bundle args, Fragment current) {
        if (fragmentSwitcher != null) {
            fragmentSwitcher.goNext(fClass, args, current);
        }
    }


    /**
     * 进入下一级，不带数据。
     *
     * @param fClass
     * @param current
     */
    protected void goNextWithoutArgs(Class<BaseFragment> fClass, BaseFragment current) {
        this.goNext(fClass, null, current);
    }

    /**
     * 返回上一级
     */
    @Override
    public void goBack() {
        if (fragmentSwitcher != null) {
            fragmentSwitcher.goBack();
        }
    }

    /**
     * 跳到纵深的顶部
     */
    @Override
    public void goTop() {
        if (fragmentSwitcher != null) {
            fragmentSwitcher.goTop();
        }
    }
}
