package com.vboss.okline.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.CardRepository;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.tsm.DefaultConfig;
import com.vboss.okline.tsm.TsmCardType;
import com.vboss.okline.ui.card.CardConstant;
import com.vboss.okline.ui.card.recharge.CardRechargeActivity;
import com.vboss.okline.ui.user.callback.CardOpenCallback;
import com.vboss.okline.ui.user.customized.LoadingView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/4/12
 * Summary : 在这里描述Class的主要功能
 */

public class CardOpenFragment extends BaseFragment {

    @BindView(R.id.sdv_card_open)
    SimpleDraweeView sdvCardOpen;
    @BindView(R.id.tv_open_card_name)
    TextView tvOpenCardName;
    @BindView(R.id.tv_open_card_number)
    TextView tvOpenCardNumber;
    @BindView(R.id.ll_card_open_detail)
    LinearLayout llCardOpenDetail;
    @BindView(R.id.tv_card_opening_success)
    TextView tvCardOpeningSuccess;
    @BindView(R.id.tv_card_opening)
    TextView tvCardOpening;
    @BindView(R.id.lv_open_card)
    LoadingView lvOpenCard;
    @BindView(R.id.tv_card_look)
    TextView tvCardLook;
    Unbinder unbinder;
    private static final String TAG = "CardOpenFragment";
    @BindView(R.id.tv_card_opening_alert)
    TextView tvCardOpeningAlert;
    @BindView(R.id.tv_card_recharge)
    TextView tvCardRecharge;
    @BindView(R.id.ll_card_opening)
    LinearLayout llCardOpening;
    @BindView(R.id.ll_card_open_main)
    LinearLayout llCardOpenMain;
    @BindView(R.id.tv_card_open_fail)
    TextView tvCardOpenFail;
    @BindView(R.id.ll_card_open_fail)
    LinearLayout llCardOpenFail;
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
    private OpenCardActivity activity;
    private long currentTimeMillis;
    private boolean isLauchingRecharge;
    private boolean isCardOpening;
    private Runnable runnable;
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Utils.showLog(TAG, "CardOpenFragment.onCreateView");
        activity = (OpenCardActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_card_open_success, container, false);
        unbinder = ButterKnife.bind(this, view);
        handler = new Handler();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Utils.showLog(TAG, "CardOpenFragment.onViewCreated");
        activity = (OpenCardActivity) getActivity();
//        状态栏设置
        actionBack.setImageResource(R.drawable.background_000);
        actionMenuButton.setVisibility(View.GONE);
        if (activity.initialCardEntity != null && activity.cardCondition.needToAuth() == 0 && activity.cardCondition.needFeeToOpen() == 0) {
            initUI();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startOpening() {
        currentTimeMillis = System.currentTimeMillis();
        String s = String.valueOf(currentTimeMillis);
        activity.cardNumber = s.substring(s.length() - 10);
        activity.openCardPresenter.startCardOpening(cardTyperSwitch(activity.initialCardEntity.aid()), activity.initialCardEntity.aid(), activity.orderNo, new CardOpenCallback() {
            @Override
            public void onProcedureFinished(final CardEntity cardEntity) {
                if (cardEntity != null) {
                    //清除加载画面
                    llCardOpening.setVisibility(View.GONE);
                    lvOpenCard.endLoading();
                    tvCardOpeningAlert.setVisibility(View.GONE);

                    //显示开卡成功界面
                    llCardOpenDetail.setVisibility(View.VISIBLE);
                    tvCardOpeningSuccess.setVisibility(View.VISIBLE);
                    actionTitle.setText(R.string.card_open_ok);
                    tvOpenCardName.setText(getString(R.string.card_open_name) + activity.initialCardEntity.cardName());
                    tvOpenCardNumber.setText(getString(R.string.card_open_number) + cardEntity.cardNo());
                    if (cardEntity.cardMainType() == CardType.COMMON_CARD || cardEntity.cardMainType() == CardType.VIP_CARD) {
                        tvCardRecharge.setVisibility(View.VISIBLE);
                        tvCardRecharge.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!isLauchingRecharge) {
                                    isLauchingRecharge = true;
                                    if (activity.bankCards == null) {
                                        CardRepository.getInstance(getContext()).cardList(CardType.BANK_CARD)
                                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new DefaultSubscribe<List<CardEntity>>(TAG) {
                                                    @Override
                                                    public void onError(Throwable throwable) {
                                                        super.onError(throwable);
                                                        Utils.showLog(TAG, "获取银行卡列表出错：" + throwable.getMessage());
                                                        Utils.customToast(getContext(), getString(R.string.card_open_no_card_alert), Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onNext(List<CardEntity> cardEntities) {
                                                        super.onNext(cardEntities);
                                                        activity.bankCards = new ArrayList<>();
                                                        for (CardEntity entity : cardEntities) {
                                                            if (entity.isQuickPass() == 2) {
                                                                activity.bankCards.add(entity);
                                                            }
                                                        }
                                                        if (!activity.bankCards.isEmpty()) {
                                                            Intent intent = new Intent(getContext(), CardRechargeActivity.class);
                                                            intent.putExtra(CardConstant.CARD_INSTANCE, cardEntity);
                                                            intent.putExtra(CardConstant.BANK_CARDS, activity.bankCards);
                                                            intent.putExtra(CardConstant.CARD_CONDITION, activity.cardCondition);
                                                            activity.startActivityForResult(intent, OpenCardActivity.REQUEST_CODE_OPEN_RECHARGE);
                                                        } else {
                                                            onError(new Exception("没有绑定为POS通的银行卡"));
                                                        }
                                                    }
                                                });
                                    } else {
                                        Intent intent = new Intent(getContext(), CardRechargeActivity.class);
                                        intent.putExtra(CardConstant.CARD_INSTANCE, cardEntity);
                                        intent.putExtra(CardConstant.BANK_CARDS, activity.bankCards);
                                        intent.putExtra(CardConstant.CARD_CONDITION, activity.cardCondition);
                                        activity.startActivityForResult(intent, OpenCardActivity.REQUEST_CODE_OPEN_RECHARGE);
                                    }
                                }
                            }
                        });
                    }
                    tvCardLook.setVisibility(View.VISIBLE);
                    tvCardLook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent data = new Intent();
                            data.putExtra(CardConstant.CARD_INSTANCE, cardEntity);
                            Utils.showLog(TAG, "开卡成功后跳转查看卡片：" + cardEntity.toString());
                            activity.setResult(Activity.RESULT_OK, data);
                            activity.finish();
                        }
                    });
                    isCardOpening = false;
                } else {
                    onExceptionCaught(getString(R.string.card_open_fail));
                }
            }

            @Override
            public void onExceptionCaught(String message) {
                //清除加载画面
                lvOpenCard.endLoading();
                llCardOpening.setVisibility(View.GONE);
                llCardOpenMain.setVisibility(View.GONE);
                tvCardOpeningAlert.setVisibility(View.GONE);

                llCardOpenFail.setVisibility(View.VISIBLE);
                tvCardOpenFail.setText("失败原因：" + message + (!TextUtils.isEmpty(activity.orderNo) ? " 开卡费将在7个工作日内返还至您的支付卡中" : ""));
                tvCardLook.setVisibility(View.GONE);
                tvCardRecharge.setVisibility(View.VISIBLE);
                tvCardRecharge.setText("知道了");
                tvCardRecharge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                });
                isCardOpening = false;
            }
        });
    }

    public static int cardTyperSwitch(String i) {
        int result = 0;
        if (i.equals(DefaultConfig.PBOC_AID)) {
            result = TsmCardType.CARD_TYPE_PBOC;
        }
        if (i.equals(DefaultConfig.VIP_AID)) {
            result = TsmCardType.CARD_TYPE_VIP;
        }
        if (i.equals(DefaultConfig.TRANSPORT_AID)) {
            result = TsmCardType.CARD_TYPE_TRANSPORT;
        }
        return result;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Utils.showLog(TAG, "CardOpenFragment.setUserVisibleHint:" + isVisibleToUser + "      getUserVisibleHint:" + getUserVisibleHint());
        if (isVisibleToUser && lvOpenCard != null) {
            initUI();
        }
    }

    public void initUI() {
        if (!isCardOpening) {
            isCardOpening = true;
            if (activity.initialCardEntity.imgUrl() != null) {
                actionBackLayout.setClickable(false);
                Utils.showLog(TAG, "显示卡片图片");
                sdvCardOpen.setImageURI(activity.initialCardEntity.imgUrl());
                runnable = new Runnable() {
                    public void run() {
                        startOpening();
                    }
                };
                handler.postDelayed(runnable, 6000);
            }
            actionTitle.setText(R.string.card_opening_title);
            lvOpenCard.startLoading();
        }
    }
}
