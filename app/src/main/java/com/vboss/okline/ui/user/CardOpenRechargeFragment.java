package com.vboss.okline.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.okline.ocp.CreateOrder;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.GsonUtils;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.data.local.DataConfig;
import com.vboss.okline.ui.opay.BankDebitInfo;
import com.vboss.okline.ui.opay.OLResult;
import com.vboss.okline.ui.opay.OPayPasswordFragment;
import com.vboss.okline.ui.opay.OPaySDKActivity;
import com.vboss.okline.ui.user.customized.LoadingDialog;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.view.widget.PaymentLauchLayout;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/4/12
 * Summary : 在这里描述Class的主要功能
 */

public class CardOpenRechargeFragment extends BaseFragment {
    public static final int REQUEST_CODE = 218;
    public static final String OL_NO = DataConfig.DEFAULT_OLNO;

    Unbinder unbinder;

    @BindView(R.id.sdv_card)
    SimpleDraweeView sdvCard;
    @BindView(R.id.tv_card_name)
    TextView tvCardName;
    @BindView(R.id.tv_card_number)
    TextView tvCardNumber;
    @BindView(R.id.tv_card_open_cost)
    TextView tvCardOpenCost;
    @BindView(R.id.pll_bankcards)
    PaymentLauchLayout pllBankcards;
    @BindView(R.id.tv_card_pay_open)
    TextView tvCardPayOpen;
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
    User user;
    private boolean isLoadingPaymentInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Utils.showLog(TAG, "CardOpenRechargeFragment.onCreateView");
        activity = (OpenCardActivity) getActivity();
        user = UserRepository.getInstance(activity).getUser();
        View view = inflater.inflate(R.layout.fragment_card_recharge, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvCardPayOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoadingPaymentInfo) {
                    isLoadingPaymentInfo = true;
                    final String s1 =activity.initialCardEntity.cardName() + "开卡支付" + activity.cardCondition.cardFeeList().get(0).getMemo() + "元";
                    Utils.showLog(TAG, "支付信息：" + s1);
                    final LoadingDialog loadingDialog = LoadingDialog.getInstance();
                    loadingDialog.setLoadingDialogListener(new LoadingDialog.LoadingDialogListener() {
                        @Override
                        public void onDismiss() {

                        }

                        @Override
                        public void onShow() {
                            activity.orderNum = String.valueOf(System.currentTimeMillis());
                            Observable.create(new Observable.OnSubscribe<String>() {
                                @Override
                                public void call(Subscriber<? super String> subscriber) {
                                    try {
                                        String resultJson = new CreateOrder().createOrder(
                                                user != null ? user.getOlNo() : OL_NO,
                                                activity.orderNum,
                                                "1",
                                                user != null ? user.getRealName() : "张三",
                                                user != null ? user.getPhone() : "17681821398",
                                                String.valueOf(activity.cardCondition.cardFeeList().get(0).getAmount()),
                                                "0",//充值金额
                                                "6",
                                                OPayPasswordFragment.P_ID,
                                                OPayPasswordFragment.P_CODE);
                                        Utils.showLog(TAG, "返回的JSON语句：" + resultJson);
                                        OLResult<Map<String, String>> result = GsonUtils.fromJson(resultJson,
                                                new TypeToken<OLResult<Map<String, String>>>() {
                                                }.getType());
                                        if (result.isSuccess()) {
                                            subscriber.onNext(result.getData().get("tn"));
                                            subscriber.onCompleted();
                                        } else {
                                            subscriber.onError(new Throwable(result.getMessage()));
                                            subscriber.onCompleted();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        subscriber.onError(e);
                                        subscriber.onCompleted();
                                    }
                                }
                            }).timeout(5, TimeUnit.SECONDS)
                                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new DefaultSubscribe<String>(TAG) {
                                        @Override
                                        public void onCompleted() {
                                            super.onCompleted();
                                        }

                                        @Override
                                        public void onError(Throwable throwable) {
                                            super.onError(throwable);
                                            Utils.showLog(TAG, "关闭加载框：支付出错：" + throwable.getMessage());
                                            loadingDialog.onFinished(null, 0);
                                            isLoadingPaymentInfo = false;
                                            Utils.customToast(getContext(), "流水号生成出错，请稍候重试！", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onNext(final String s) {
                                            super.onNext(s);
                                            Intent intent = new Intent(getContext(), OPaySDKActivity.class);
                                            CardEntity selectedCard = pllBankcards.getSelectedCard();
                                            if (selectedCard != null) {
                                                activity.bankCards.add(activity.bankCards.remove(activity.bankCards.indexOf(selectedCard)));
                                            }
                                            intent.putExtra(OPaySDKActivity.PAYMENT_ARGUMENT_CARDLIST, activity.bankCards);
                                            String orderDesc = s1.replace("¥ ", "");
                                            BankDebitInfo bankDebitInfo = new BankDebitInfo(
                                                    String.valueOf(activity.cardCondition.cardFeeList().get(0).getAmount()),//支付金额
                                                    activity.initialCardEntity.cardMainType(),//卡片类型
                                                    String.valueOf(activity.initialCardEntity.cardId()),//卡片id
                                                    activity.initialCardEntity.cardName(),//卡片名称
                                                    activity.initialCardEntity.cardNo(),//卡号
                                                    activity.orderNum,//商户订单号
                                                    "6",//商户号
                                                    s,//流水号
                                                    orderDesc,//订单描述
                                                    true, //已选中银行卡
                                                    false//显示结果页
                                            );
                                            intent.putExtra(BankDebitInfo.BANK_DEBIT_INFO, bankDebitInfo);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            Utils.showLog(TAG, "关闭加载框");
                                            loadingDialog.onFinished(null, 0);
                                            isLoadingPaymentInfo = false;
                                            activity.startActivityForResult(intent, REQUEST_CODE);
                                            onCompleted();
                                        }
                                    });
                        }
                    });
                    loadingDialog.show(getFragmentManager(), LoadingDialog.class.getName());
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    void initUI() {
//        初始化状态栏
        actionTitle.setText(R.string.card_open_info);
        actionBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.cardCondition.needToAuth() == 1) {
                    activity.vpOpenCard.setCurrentItem(0, false);
                } else {
                    activity.setResult(Activity.RESULT_CANCELED);
                    activity.finish();
                }
            }
        });
        if (!TextUtils.isEmpty(activity.initialCardEntity.imgUrl())) {
            Utils.showThumb(sdvCard, activity.initialCardEntity.imgUrl(), 110 * 2, 70 * 2, 15f);
        }
        tvCardName.setText(activity.initialCardEntity.cardName());
        tvCardNumber.setText(activity.initialCardEntity.merName());

        if (activity.cardCondition != null && tvCardOpenCost != null) {
            tvCardOpenCost.setText(Utils.getAmountText(activity.cardCondition.cardFeeList().get(0).getAmount()));
        }

        pllBankcards.setBankCards(activity.bankCards);
        pllBankcards.adjustSize();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private static final String TAG = "CardOpenRechargeFragment";
}
