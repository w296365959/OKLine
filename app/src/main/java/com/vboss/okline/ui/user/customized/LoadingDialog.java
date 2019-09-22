package com.vboss.okline.ui.user.customized;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.ui.user.Utils;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/4/19
 * Summary : 在这里描述Class的主要功能
 */

public class LoadingDialog extends DialogFragment {

    private ImageView iv_loading;
    private TextView tv_finished;

    public void setLoadingDialogListener(LoadingDialogListener loadingDialogListener) {
        Utils.showLog(TAG,"★设置监听");
        this.loadingDialogListener = loadingDialogListener;
    }

    private LoadingDialogListener loadingDialogListener;

    public LoadingDialog() {

    }

    public static LoadingDialog getInstance(){
        return new LoadingDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.view_loading_dialog, container, false);
        iv_loading = (ImageView) view.findViewById(R.id.iv_loading);
        tv_finished = (TextView) view.findViewById(R.id.tv_finished);
        tv_finished.setVisibility(View.INVISIBLE);
        startImageAnimation(true);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
        getDialog().setCanceledOnTouchOutside(false);
        return view;
    }
    private void startImageAnimation(boolean b) {
        if (b) {
            iv_loading.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotation);
            animation.setDuration(2000);
            animation.setRepeatCount(Animation.INFINITE);
            animation.setInterpolator(new LinearInterpolator());
            iv_loading.startAnimation(animation);
        } else {
            iv_loading.clearAnimation();
            iv_loading.setVisibility(View.GONE);
        }
    }

    public void onFinished(String string, int delayMillis){
        startImageAnimation(false);
        tv_finished.setText(string);
        tv_finished.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, delayMillis);
    }

    private static final String TAG = "LoadingDialog";

    @Override
    public void show(FragmentManager manager, String tag) {
//        super.show(manager, LoadingDialog.class.getCardName());
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(this,getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
        if (loadingDialogListener != null) {
            loadingDialogListener.onShow();
            Utils.showLog(TAG,"★LoadingDialog.onShow");
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        System.out.println("");
        Utils.showLog(TAG,"LoadingDialog.onDismiss");
        if (loadingDialogListener != null) {
            loadingDialogListener.onDismiss();
        }
        loadingDialogListener = null;
    }

    public interface LoadingDialogListener {
        void onDismiss();
        void onShow();
    }
}
