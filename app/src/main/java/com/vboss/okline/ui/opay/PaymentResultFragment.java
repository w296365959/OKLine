package com.vboss.okline.ui.opay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.user.CardOpenRechargeFragment;
import com.vboss.okline.ui.user.customized.CustomDialog;

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
public class PaymentResultFragment extends BaseFragment {

    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.tv_loading)
    TextView tvLoading;
    @BindView(R.id.tv_opaysdk_result_cancel)
    TextView tvOpaysdkResultCancel;
    @BindView(R.id.tv_opaysdk_result_retry)
    TextView tvOpaysdkResultRetry;
    private View view;
    private OPaySDKActivity activity;
    private String resultMessage;
    String bankDebitResult;
    private String resultCode;
    String olNo;
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
            view = inflater.inflate(R.layout.fragment_opaysdk5, null);
            unbinder = ButterKnife.bind(this, view);

            setPaymentExecutingAnimation();

            olNo = activity.user!=null?activity.user.getOlNo():CardOpenRechargeFragment.OL_NO;
            tn = activity.orderTn;
            bindId = activity.selectedCard.bindId();
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
