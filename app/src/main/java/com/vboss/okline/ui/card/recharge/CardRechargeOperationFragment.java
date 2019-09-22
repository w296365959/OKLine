package com.vboss.okline.ui.card.recharge;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.ui.card.CardConstant;
import com.vboss.okline.ui.card.RechargeSynchronousHelper;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.ui.user.customized.LoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.vboss.okline.ui.user.CardOpenFragment.cardTyperSwitch;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardRechargeOperationFragment extends BaseFragment {


    @BindView(R.id.sdv_card)
    SimpleDraweeView sdvCard;
    @BindView(R.id.lv_operation)
    LoadingView lvOperation;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.ll_operation)
    LinearLayout llOperation;
    @BindView(R.id.tv_open_card_name)
    TextView tvOpenCardName;
    @BindView(R.id.tv_open_card_number)
    TextView tvOpenCardNumber;
    @BindView(R.id.tv_open_card_balance)
    TextView tvOpenCardBalance;
    @BindView(R.id.tv_card_look)
    TextView tvCardLook;
    @BindView(R.id.tv_card_auto)
    TextView tvCardAuto;
    @BindView(R.id.ll_success)
    LinearLayout llSuccess;
    @BindView(R.id.tv_card_open_fail)
    TextView tvCardOpenFail;
    @BindView(R.id.ll_exception)
    LinearLayout llException;
    @BindView(R.id.tv_card_retry)
    TextView tvCardRetry;
    Unbinder unbinder;
    @BindView(R.id.tv_card_recharge_amout)
    TextView tvCardRechargeAmout;
    @BindView(R.id.action_back)
    ImageButton actionBack;
    @BindView(R.id.action_back_layout)
    RelativeLayout actionBackLayout;
    @BindView(R.id.iv_ocard_state)
    ImageView ivOcardState;
    @BindView(R.id.action_title)
    TextView actionTitle;
    @BindView(R.id.action_menu_button)
    ImageButton actionMenuButton;
    @BindView(R.id.action_menu_layout)
    RelativeLayout actionMenuLayout;
    private CardRechargeActivity activity;
    private Runnable runnable;
    private Handler handler;
    private Subscription rechargeCard;

    public CardRechargeOperationFragment() {
        // Required empty public constructor
        System.out.println("CardRechargeOperationFragment.CardRechargeOperationFragment");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Utils.showLog(TAG, "CardRechargeOperationFragment.onCreateView");
        activity = (CardRechargeActivity) getActivity();
        handler = new Handler();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_recharge_operation, container, false);
        unbinder = ButterKnife.bind(this, view);
        actionBack.setImageResource(R.drawable.background_000);
        actionMenuButton.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.showLog(TAG, "CardRechargeOperationFragment.onViewCreated");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
        unbinder.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Utils.showLog(TAG, "CardRechargeOperationFragment.setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            startRecharge();
        }
    }

    public void startRecharge() {
        sdvCard.setImageURI(activity.cardEntity.imgUrl());
        lvOperation.startLoading();
        actionTitle.setText("正在充值");
        // TODO: 2017/5/6 设置自动圈存
        runnable = new Runnable() {
            @Override
            public void run() {
                startCardSync(new CardRestorListener() {
                    @Override
                    public void onSuccess(String amout) {
                        if (isAdded() && actionTitle != null) {
                            actionTitle.setText("充值成功");
                            lvOperation.endLoading();
                            llLoading.setVisibility(View.GONE);
                            llSuccess.setVisibility(View.VISIBLE);
                            tvOpenCardName.setText(getString(R.string.card_open_name) + activity.cardEntity.cardName());
                            tvOpenCardNumber.setText(getString(R.string.card_open_number) + activity.cardEntity.cardNo());
                            tvOpenCardBalance.setText(getString(R.string.card_open_balance) + "¥ " + amout);
                            String amount = String.valueOf(activity.cardRechargeInfo.getAmount());
                            tvCardRechargeAmout.setText("本次充值：¥ " + amount.substring(0, amount.length() - 2) + "." + amount.substring(amount.length() - 2));
                            tvCardLook.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent data = new Intent();
                                    data.putExtra(CardConstant.CARD_INSTANCE, activity.cardEntity);
                                    activity.setResult(Activity.RESULT_OK, data);
                                    activity.finish();
                                }
                            });
                            tvCardAuto.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // TODO: 2017/5/6 设置自动圈存
                                }
                            });
                        }
                    }

                    @Override
                    public void onFail(String msg) {
                        if (isAdded() && actionTitle != null) {
                            actionTitle.setText("圈存失败");
                            lvOperation.endLoading();
                            llSuccess.setVisibility(View.GONE);
                            llOperation.setVisibility(View.GONE);
                            llException.setVisibility(View.VISIBLE);
                            tvCardOpenFail.setText("失败原因：" + msg);
                            tvCardRetry.setVisibility(View.VISIBLE);
                            tvCardRetry.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    tvCardRetry.setVisibility(View.GONE);
                                    llException.setVisibility(View.GONE);
                                    llOperation.setVisibility(View.VISIBLE);
                                    startRecharge();
                                }
                            });
                        }
                    }
                });
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    private static final String TAG = "CardRechargeOperation";

    @SuppressWarnings("WrongConstant")
    private void startCardSync(final CardRestorListener listener) {
        String aid = activity.cardEntity.aid();
        int tsmCardType = cardTyperSwitch(aid);
        int amount = activity.cardRechargeInfo.getAmount();
        Utils.showLog(TAG, "发起充值：tsmCardType = [" + tsmCardType + "], appAid = [" + aid + "], amount = [" + amount + "]");
        rechargeCard = UserRepository.getInstance(getContext()).rechargeCard(activity, tsmCardType, aid, amount)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<String>(TAG) {
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Utils.showLog(TAG, "充值失败：" + throwable.getMessage());
                        listener.onFail(throwable.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        super.onNext(s);
                        Utils.showLog(TAG, "充值成功：余额：" + s);
                        //充值成功之后解除订阅
                        Utils.unsubscribeRxJava(rechargeCard);
                                    /* ***
                                    *  ------------------------------
                                    * modify by wangshuai 2017-04-28
                                    * synchronous recharge card log
                                    * ------------------------
                                    * ** */
                        CardLog.Builder builder = CardLog.newBuilder()
                                .amount(Integer.parseInt(String.valueOf(activity.cardRechargeInfo.getAmount())))
                                .cardMainType(activity.cardEntity.cardMainType())
                                .cardId(activity.cardEntity.cardId())
                                .cardName(activity.cardEntity.cardName())
                                .cardNo(activity.cardEntity.cardNo())
                                .integral(activity.cardEntity.integral())
                                .merName(activity.cardEntity.merName())
                                .orderNo(activity.bankDebitInfo.getTn())
                                .remark(activity.bankDebitInfo.getOrderDesc())
                                .transTime(activity.bankDebitInfo.getOrderDate());
                        RechargeSynchronousHelper.getInstance().rechargeSynchronousToServer(builder.build());
                                    /* **************      end      **************  */
                        listener.onSuccess(s);
                    }
                });
    }

    interface CardRestorListener {
        void onSuccess(String amout);

        void onFail(String msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.unsubscribeRxJava(rechargeCard);
    }
}
