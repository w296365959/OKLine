package com.vboss.okline.ui.user;


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
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.okline.ocp.CreateOrder;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.GsonUtils;
import com.vboss.okline.ui.opay.BankDebitInfo;
import com.vboss.okline.ui.opay.OLResult;
import com.vboss.okline.ui.opay.OPayPasswordFragment;
import com.vboss.okline.ui.opay.OPaySDKActivity;
import com.vboss.okline.ui.user.customized.LoadingDialog;

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
 * A simple {@link Fragment} subclass.
 */
public class CardOpenPaymentFragment extends BaseFragment {


    public static final int REQUEST_CODE = 189;
    @BindView(R.id.iv_open_card0)
    SimpleDraweeView ivOpenCard0;
    @BindView(R.id.tv_card_name0)
    TextView tvCardName0;
    @BindView(R.id.tv_card_info0)
    TextView tvCardInfo0;
    @BindView(R.id.iv_open_card1)
    SimpleDraweeView ivOpenCard1;
    @BindView(R.id.tv_card_name1)
    TextView tvCardName1;
    @BindView(R.id.tv_card_info1)
    TextView tvCardInfo1;
    @BindView(R.id.ll_success)
    LinearLayout llSuccess;
    @BindView(R.id.tv_fail_message)
    TextView tvFailMessage;
    @BindView(R.id.btn_retry)
    TextView btnRetry;
    @BindView(R.id.btn_quit)
    TextView btnQuit;
    @BindView(R.id.ll_fail)
    LinearLayout llFail;
    Unbinder unbinder;
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
    private boolean isLoadingPaymentInfo;

    public CardOpenPaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Utils.showLog(TAG, "CardOpenPaymentFragment.onCreateView");
        activity = (OpenCardActivity) getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_open_payment, container, false);
        unbinder = ButterKnife.bind(this, view);
        actionBack.setImageResource(R.drawable.background_000);
        actionBackLayout.setOnClickListener(null);
        actionTitle.setCompoundDrawables(getResources().getDrawable(R.drawable.logo8), null, null, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.showLog(TAG, "CardOpenPaymentFragment.onViewCreated");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        System.out.println("");
        Utils.showLog(TAG, "CardOpenPaymentFragment.setUserVisibleHint:" + isVisibleToUser);
        if (isVisibleToUser) {
            initUI(activity.paymentResult == 1, activity.failMessage);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        System.out.println("CardOpenPaymentFragment.onHiddenChanged");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    void initUI(boolean successful, String message) {
        Utils.showLog(TAG, "★支付结果界面：successful = [" + successful + "], message = [" + message + "]");
        actionTitle.setText(successful ? "支付成功" : "支付失败");
        if (successful) {
            llSuccess.setVisibility(View.VISIBLE);
            llFail.setVisibility(View.GONE);
            Utils.showThumb(ivOpenCard0, activity.initialCardEntity.imgUrl(), 110, 72, 15f);
            Utils.showThumb(ivOpenCard1, activity.bankCards.get(0).imgUrl(), 110, 72, 15f);
            tvCardName0.setText(activity.initialCardEntity.cardName());
            tvCardName1.setText(activity.bankCards.get(0).cardName());
            String memo = String.valueOf(activity.cardCondition.cardFeeList().get(0).getAmount());
            memo = "¥ " + memo.substring(0, memo.length() - 2) + "." + memo.substring(memo.length() - 2);
            tvCardInfo0.setText("开卡费：" + memo);
            tvCardInfo1.setText("支付：" + memo);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    activity.vpOpenCard.setCurrentItem(1, false);
                }
            }, 3000);
        } else {
            llSuccess.setVisibility(View.GONE);
            llFail.setVisibility(View.VISIBLE);
            tvFailMessage.setText(message);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isLoadingPaymentInfo) {
                        isLoadingPaymentInfo = true;
                        final String s1 =  activity.initialCardEntity.cardName() + "开卡支付" + activity.cardCondition.cardFeeList().get(0).getMemo();
                        Utils.showLog(TAG, "支付信息：" + s1);
                        final LoadingDialog loadingDialog = LoadingDialog.getInstance();
                        loadingDialog.setLoadingDialogListener(new LoadingDialog.LoadingDialogListener() {
                            @Override
                            public void onDismiss() {

                            }

                            @Override
                            public void onShow() {
                                Observable.create(new Observable.OnSubscribe<String>() {
                                    @Override
                                    public void call(Subscriber<? super String> subscriber) {
                                        try {
                                            String resultJson = new CreateOrder().createOrder(
                                                    activity.user != null ? activity.user.getOlNo() : CardOpenRechargeFragment.OL_NO,
                                                    activity.orderNum,
                                                    "1",
                                                    activity.user != null ? activity.user.getRealName() : "张三",
                                                    activity.user != null ? activity.user.getPhone() : "17681821398",
                                                    String.valueOf(activity.cardCondition.cardFeeList().get(0).getAmount()),
                                                    "0",
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
            btnQuit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.showLog(TAG, "requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");

    }

    private static final String TAG = "CardOpenPaymentFragment";
}
