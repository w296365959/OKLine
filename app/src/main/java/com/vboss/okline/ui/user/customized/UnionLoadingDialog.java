package com.vboss.okline.ui.user.customized;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.vboss.okline.R;
import com.vboss.okline.ui.user.Utils;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/6/13
 * Summary : 在这里描述Class的主要功能
 */

public class UnionLoadingDialog extends DialogFragment {

    private View ivLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.view_union_dialog, container, false);
        ivLoading = view.findViewById(R.id.iv_loading_ocard);
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
            ivLoading.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotation_clockwise);
            animation.setDuration(2000);
            animation.setRepeatCount(Animation.INFINITE);
            animation.setInterpolator(new LinearInterpolator());
            ivLoading.startAnimation(animation);
        } else {
            ivLoading.clearAnimation();
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
//        super.show(manager, LoadingDialog.class.getCardName());
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(this,tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        startImageAnimation(false);
        super.onDismiss(dialog);
    }
}
