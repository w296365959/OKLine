package com.vboss.okline.ui.opay;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.CardRepository;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.ui.user.customized.CustomDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_CANCELED;

/**
 * OKLine(Hangzhou) Co.,ltd.
 * Author:Zheng Jun
 * Email:zhengjun@okline.cn
 * Date: 2016-6-16 11:28:11
 * Desc:
 */
public class OPayFragment extends BaseFragment {
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_paypopcancel)
    TextView tvPaypopcancel;
    @BindView(R.id.tv_selectedAccountName)
    TextView tvSelectedAccountName;
    @BindView(R.id.rl_payableaccount)
    RelativeLayout rlPayableaccount;
    @BindView(R.id.tv_amout)
    TextView tvAmout;
    @BindView(R.id.bt_payconfirm)
    Button btPayconfirm;
    @BindView(R.id.tv_payabledesc)
    TextView tvPayabledesc;
    @BindView(R.id.rl_payabledesc)
    RelativeLayout rlPayabledesc;
    private View view;
    private OPaySDKActivity activity;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_opaysdk1, null);
            unbinder = ButterKnife.bind(this, view);
            activity = (OPaySDKActivity) getActivity();

            //显示整合的支付金额
            tvPayabledesc.setText(activity.orderDesc);
            String orderAmount = activity.orderAmount;
            Utils.showLog(TAG,"activity.orderAmount:"+activity.orderAmount);
            String substring = orderAmount.substring(0, orderAmount.length() - 2);
            String substring1 = orderAmount.substring(orderAmount.length() - 2);
            String text = "¥ " + substring + "." + substring1;
            Utils.showLog(TAG,""+text);
            tvAmout.setText(text);


            //choose available payment method
            rlPayableaccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!activity.isEmptyCards) {
                        activity.viewPager.setCurrentItem(3, false);
                    }
                }
            });

            setAccountName();


            tvPaypopcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CustomDialog(getContext(), null, getString(R.string.opaysdk_quitpayment), null, getString(R.string.settings_no), getString(R.string.settings_yes), new CustomDialog.DialogClickListener() {
                        @Override
                        public void onPositiveClick() {
                            activity.setResult(RESULT_CANCELED);
                            activity.finish();
                        }

                        @Override
                        public void onNegtiveClick() {
                        }
                    }, 0).show();
                }
            });
        }
        ButterKnife.bind(this, view);
        return view;
    }

    public void setAccountName() {
        if (activity.isEmptyCards) {
//            tvSelectedAccountName.setText(getString(R.string.opaysdk_empty_cardlist_prompt));
//            btPayconfirm.setText(getString(R.string.opaysdk_add_bank_card));
//            btPayconfirm.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    Intent intent;
//                    if (activity.user.getOlNo() == null) {
//                        intent = new Intent(getContext(), RealNameAuthenticationActivity.class);
//                    } else {
//                        intent = new Intent();
//                        intent.putExtra(CommonConstant.VALUE.IS_FROM_PAY_INTERFACE, true);
//                        intent.setClass(getContext(), BankCardInfo.class);
//                    }
//                    startActivityForResult(intent, 817);
//                }
//            });
            Utils.customToast(getContext(), "没有可以使用的卡片", Toast.LENGTH_SHORT).show();
        } else {
            String cardNo = activity.selectedCard.cardNo();
            String cardNoSubstring = cardNo.substring(cardNo.length() - 4);
            String text1 = activity.selectedCard.cardName() + "(" + cardNoSubstring + ") >";
            tvSelectedAccountName.setText(text1);
            btPayconfirm.setText(getString(R.string.payment_confirm));
            btPayconfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    activity.userManager.isPasswordSet(UserManager.PASSWORD_TYPE_OPAY)
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new DefaultSubscriber<Boolean>() {
//                                @Override
//                                public void onNext(Boolean aBoolean) {
//                                    super.onNext(aBoolean);
//                                    if (aBoolean) {
//                                        SharedPreferences sharedPreferences = activity.getSharedPreferences(MySecurityActivity.SHAREDPREFERENCE_NAME_CONFIG, Context.MODE_PRIVATE);
//                                        boolean flag = sharedPreferences.getBoolean(MySecurityActivity.OPAY_ONLINE_PAYMENT_CONFIRMATION, true);
//                                        if (aBoolean) {
                                            activity.viewPager.setCurrentItem(1, true);
//                                        } else {
//                                            activity.viewPager.setCurrentItem(2, false);
//                                        }
//                                    } else {
//                                        activity.viewPager.setCurrentItem(2, false);
//                                    }
//                                }
//                            });
                }
            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            setAccountName();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Activity.RESULT_OK:
                Utils.showLog(TAG,"★OPayFragment被返回后调用查询银行卡列表");
                CardRepository.getInstance(getContext()).cardList(CardType.BANK_CARD)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultSubscribe<List<CardEntity>>(TAG) {
                            @Override
                            public void onCompleted() {
                                super.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                Utils.customToast(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                activity.finish();
                            }

                            @Override
                            public void onNext(List<CardEntity> cards) {
                                if (cards.size() != 0) {
                                    for (CardEntity card : cards) {
                                        if (card.isQuickPass() == 2) {
                                            activity.cardEntities.add(card);
                                        }
                                    }
                                    if (activity.cardEntities.isEmpty()) {
                                        activity.isEmptyCards = true;
                                    } else {
                                        //打印可以支付的卡片列表
                                        activity.isEmptyCards = false;
                                        activity.selectedCard = activity.cardEntities.get(0);

                                    }
                                } else {
                                    activity.isEmptyCards = true;
                                }
                                setAccountName();
                            }
                        });
                break;
            case Activity.RESULT_CANCELED:
                break;
        }
    }

    private static final String TAG = "OPayFragment";

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Utils.showLog(TAG,"onDestroyView");
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.showLog(TAG,"onDestroy");
    }
}
