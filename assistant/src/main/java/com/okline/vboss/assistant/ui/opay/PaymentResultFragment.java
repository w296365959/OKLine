package com.okline.vboss.assistant.ui.opay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.okline.vboss.assistant.R;
import com.okline.vboss.assistant.R2;
import com.okline.vboss.assistant.base.Config;
import com.okline.vboss.assistant.utils.CustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * OKLine(Hangzhou) Co.,ltd.
 * Author:Zheng Jun
 * Email:zhengjun@okline.cn
 * Date: 2016-6-16 11:28:11
 * Desc:
 */
public class PaymentResultFragment extends Fragment {

    @BindView(R2.id.iv_loading)
    ImageView ivLoading;
    @BindView(R2.id.tv_loading)
    TextView tvLoading;
    @BindView(R2.id.tv_opaysdk_result_cancel)
    TextView tvOpaysdkResultCancel;
    @BindView(R2.id.tv_opaysdk_result_retry)
    TextView tvOpaysdkResultRetry;
    private View view;
    private OPaySDKActivity activity;
    private String resultMessage;
    String bankDebitResult;
    private String resultCode;
    String tn;
    String bindId;
    private String orderNumber;
    private String orderAmount;
    private String merNo;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (OPaySDKActivity) getActivity();
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_opaysdk5_assistant, null);
            unbinder = ButterKnife.bind(this, view);

            setPaymentExecutingAnimation();

            tn = activity.orderTn;
            bindId = activity.selectedCard.getBindId();
            orderNumber = activity.orderNumber;
            orderAmount = activity.orderAmount;
            merNo = activity.merNo;
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (ivLoading != null) {
                ivLoading.clearAnimation();
                ivLoading.setVisibility(View.INVISIBLE);
                tvLoading.setText("支付失败:"+activity.paymentNote);
                tvOpaysdkResultCancel.setVisibility(View.VISIBLE);
                tvOpaysdkResultCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new CustomDialog(getContext(), null, getString(R.string.opaysdk_quitpayment), null, getString(R.string.settings_no), getString(R.string.settings_yes), new CustomDialog.DialogClickListener() {
                            @Override
                            public void onNegtiveClick() {

                            }

                            @Override
                            public void onPositiveClick() {
                                //返回的信息为用户中途取消!
                                getActivity().setResult(Activity.RESULT_CANCELED);
                                getActivity().finish();
                            }
                        }, 0).show();
                    }
                });
                tvOpaysdkResultRetry.setVisibility(View.VISIBLE);
                tvOpaysdkResultRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.viewPager.setCurrentItem(0, true);
                    }
                });
            }
        }
    }

    public void setPaymentExecutingAnimation() {
        RotateAnimation animation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatCount(RotateAnimation.INFINITE);
        animation.setRepeatMode(RotateAnimation.RESTART);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        ivLoading.startAnimation(animation);
        tvLoading.setText(R.string.payment_process_loading);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 251 && resultCode == Activity.RESULT_OK) {
            activity.setResult(Activity.RESULT_OK);
            activity.finish();
        }
    }

}
