package com.okline.vboss.assistant.ui.opay;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.okline.vboss.assistant.R;
import com.okline.vboss.assistant.R2;
import com.okline.vboss.assistant.ui.recharge.Utils;

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
public class OPayFragment extends Fragment {
    @BindView(R2.id.iv_icon)
    ImageView ivIcon;
    @BindView(R2.id.tv_paypopcancel)
    TextView tvPaypopcancel;
    @BindView(R2.id.tv_selectedAccountName)
    TextView tvSelectedAccountName;
    @BindView(R2.id.rl_payableaccount)
    RelativeLayout rlPayableaccount;
    @BindView(R2.id.tv_amout)
    TextView tvAmout;
    @BindView(R2.id.bt_payconfirm)
    Button btPayconfirm;
    @BindView(R2.id.tv_payabledesc)
    TextView tvPayabledesc;
    @BindView(R2.id.rl_payabledesc)
    RelativeLayout rlPayabledesc;
    @BindView(R2.id.iv_connecting)
    ImageView ivConnecting;
    @BindView(R2.id.tv_connecting)
    TextView tvConnecting;
    private View view;
    private OPaySDKActivity activity;
    private Unbinder unbinder;
    private Handler handler;
    private Runnable runnable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_opaysdk1_assistant, null);
            unbinder = ButterKnife.bind(this, view);
            activity = (OPaySDKActivity) getActivity();
            tvConnecting.setText("正在调用"+activity.selectedCard.getMerName()+"插件");
            startImageAnimation(true);
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    startImageAnimation(false);
                    activity.viewPager.setCurrentItem(1);
                }
            };
            handler.postDelayed(runnable,3000);
//            //显示整合的支付金额
//            tvPayabledesc.setText(activity.orderDesc);
//            String orderAmount = activity.orderAmount;
//            Utils.showLog(TAG, "activity.orderAmount:" + activity.orderAmount);
//            String substring = orderAmount.substring(0, orderAmount.length() - 2);
//            String substring1 = orderAmount.substring(orderAmount.length() - 2);
//            String text = "¥ " + substring + "." + substring1;
//            Utils.showLog(TAG, "" + text);
//            tvAmout.setText(text);
//            //choose available payment method
//            rlPayableaccount.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!activity.isEmptyCards) {
//                        activity.viewPager.setCurrentItem(3, false);
//                    }
//                }
//            });
//            setAccountName();
//            tvPaypopcancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    new CustomDialog(getContext(), null, getString(R.string.opaysdk_quitpayment), null, getString(R.string.settings_no), getString(R.string.settings_yes), new CustomDialog.DialogClickListener() {
//                        @Override
//                        public void onPositiveClick() {
//                            activity.setResult(RESULT_CANCELED);
//                            activity.finish();
//                        }
//
//                        @Override
//                        public void onNegtiveClick() {
//                        }
//                    }, 0).show();
//                }
//            });
        }
        return view;
    }

    private void startImageAnimation(boolean b) {
        if (b) {
            ivConnecting.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotation_clockwise);
            animation.setDuration(2000);
            animation.setRepeatCount(Animation.INFINITE);
            animation.setInterpolator(new LinearInterpolator());
            ivConnecting.startAnimation(animation);
        } else {
            ivConnecting.clearAnimation();
        }
    }

    public void setAccountName() {
        if (activity.isEmptyCards) {
            Utils.customToast(getContext(), "没有可以使用的卡片", Toast.LENGTH_SHORT).show();
        } else {
            String cardNo = activity.selectedCard.getCardNo();
            String cardNoSubstring = cardNo.substring(cardNo.length() - 4);
            String text1 = activity.selectedCard.getCardName() + "(" + cardNoSubstring + ") >";
            tvSelectedAccountName.setText(text1);
            btPayconfirm.setText(getString(R.string.payment_confirm));
            btPayconfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.viewPager.setCurrentItem(1, true);
                }
            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        try {
//            setAccountName();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        switch (resultCode) {
//            case Activity.RESULT_OK:
//                Utils.showLog(TAG, "★OPayFragment被返回后调用查询银行卡列表");
//                OLApiService.getInstance().getDownloadedCardList(CardType.BANK_CARD)
//                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new DefaultSubscribe<List<CardEntity>>(TAG) {
//                            @Override
//                            public void onCompleted() {
//                                super.onCompleted();
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                super.onError(e);
//                                Utils.customToast(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                                activity.finish();
//                            }
//
//                            @Override
//                            public void onNext(List<CardEntity> cards) {
//                                if (cards.size() != 0) {
//                                    for (CardEntity card : cards) {
//                                        if (card.isQuiclPass()) {
//                                            activity.cardEntities.add(card);
//                                        }
//                                    }
//                                    if (activity.cardEntities.isEmpty()) {
//                                        activity.isEmptyCards = true;
//                                    } else {
//                                        //打印可以支付的卡片列表
//                                        activity.isEmptyCards = false;
//                                        activity.selectedCard = activity.cardEntities.get(0);
//
//                                    }
//                                } else {
//                                    activity.isEmptyCards = true;
//                                }
//                                setAccountName();
//                            }
//                        });
//                break;
//            case Activity.RESULT_CANCELED:
//                break;
//        }
    }

    private static final String TAG = "OPayFragment";

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
