package com.vboss.okline.ui.card.recharge;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.ui.opay.BankDebitInfo;
import com.vboss.okline.ui.opay.OLResult;
import com.vboss.okline.ui.opay.OPayPasswordFragment;
import com.vboss.okline.ui.opay.OPaySDKActivity;
import com.vboss.okline.ui.opay.PaymentRunningDialog;
import com.vboss.okline.ui.user.CardOpenRechargeFragment;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.ui.user.customized.LoadingDialog;
import com.vboss.okline.ui.user.entity.CardRechargeInfo;
import com.vboss.okline.view.widget.PaymentLauchLayout;

import java.util.ArrayList;
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
public class CardRechargeMainFragment extends BaseFragment {

    public static final int REQUEST_CODE_RECHARGE = 297;
    public static final String INTENT_TAG = "tag";
    public static final String INTENT_OPEN_BANK_CARD = "intent_open_bank_card";
    @BindView(R.id.sdv_card)
    SimpleDraweeView sdvCard;
    @BindView(R.id.tv_card_name)
    TextView tvCardName;
    @BindView(R.id.tv_card_number)
    TextView tvCardNumber;
    @BindView(R.id.tv_card_balance)
    TextView tvCardBalance;
    @BindView(R.id.tv_recharge_amount)
    TextView tvRechargeAmount;
    @BindView(R.id.text_recharge_amount)
    TextView textRechargeAmount;
    @BindView(R.id.gv_recharge)
    GridView gvRecharge;
    @BindView(R.id.pll_bankcards)
    PaymentLauchLayout pllBankcards;
    @BindView(R.id.tv_need_pay)
    TextView tvNeedPay;
    @BindView(R.id.text_need_pay)
    TextView textNeedPay;
    @BindView(R.id.tv_price_cut)
    TextView tvPriceCut;
    @BindView(R.id.text_price_cut)
    TextView textPriceCut;
    @BindView(R.id.btn_pay)
    TextView btnPay;
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
    private BaseAdapter gvAdapter;
    private float discount = 1.0f;
    private boolean isLoadingPaymentInfo;
    private Unbinder unbinder;

    public CardRechargeMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (CardRechargeActivity) getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_recharge_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        actionTitle.setText("充值");
        actionMenuButton.setVisibility(View.GONE);
        actionBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setResult(Activity.RESULT_CANCELED);
                activity.finish();
            }
        });
        Utils.showThumb(sdvCard, activity.cardEntity.imgUrl(), 110 * 2, 72 * 2, 10f);
        tvCardName.setText(activity.cardEntity.cardName());
        int balance = activity.cardEntity.balance();
        int i = balance / 100;
        int m = balance % 100;
        String substring = "" + i + "." + (m == 0 ? "00" : m);
        Utils.showLog(TAG, "卡片余额：" + substring);
        tvCardNumber.setText("卡号：" + activity.cardEntity.cardNo());
        tvCardBalance.setText("余额：" + substring);
        final ArrayList<CardRechargeInfo> cardRechargeInfos = new ArrayList<>();
        for (Integer integer : activity.cardCondition.amountList()) {
            cardRechargeInfos.add(new CardRechargeInfo(integer, false));
        }
        gvAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return cardRechargeInfos.size();
            }

            @Override
            public CardRechargeInfo getItem(int position) {
                return cardRechargeInfos.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = View.inflate(getContext(), R.layout.view_open_card_recharge, null);
                TextView viewById = (TextView) convertView.findViewById(R.id.text);
                viewById.setText(Utils.getAmountText(getItem(position).getAmount()));
                viewById.setBackgroundColor(getItem(position).isSelected() ? Color.parseColor("#25A613") : Color.WHITE);
                return convertView;
            }
        };
        gvRecharge.setAdapter(gvAdapter);
        gvRecharge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activity.cardRechargeInfo = cardRechargeInfos.get(position);
                textRechargeAmount.setText(Utils.getAmountText(activity.cardRechargeInfo.getAmount()));
                for (CardRechargeInfo rechargeInfo : cardRechargeInfos) {
                    rechargeInfo.setSelected(cardRechargeInfos.indexOf(rechargeInfo) == position);
                }
                gvAdapter.notifyDataSetChanged();
            }
        });

        pllBankcards.setBankCards(activity.bankCards);
        if (activity.bankCards == null || activity.bankCards.isEmpty()) {
            btnPay.setTextColor(Color.WHITE);
            btnPay.setText("添加卡片");
        }

        textRechargeAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                int v = (int) (Float.valueOf(string.substring(string.indexOf(" ") + 1)) * 100);
                int amoutNeedPay = (int) (v * discount);
                tvNeedPay.setVisibility(View.VISIBLE);
                textNeedPay.setVisibility(View.VISIBLE);
                textNeedPay.setText(Utils.getAmountText(amoutNeedPay));
                int amout = v - amoutNeedPay;
                if (amout == 0) {
                    tvPriceCut.setVisibility(View.INVISIBLE);
                    textPriceCut.setVisibility(View.INVISIBLE);
                } else {
                    tvPriceCut.setVisibility(View.VISIBLE);
                    textPriceCut.setVisibility(View.VISIBLE);
                    textPriceCut.setText(Utils.getAmountText(amout));
                }
            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.bankCards == null || activity.bankCards.isEmpty()) {
                    //添加卡片
                    Intent intent = new Intent();
                    intent.putExtra(INTENT_TAG, INTENT_OPEN_BANK_CARD);
                    activity.setResult(Activity.RESULT_CANCELED, intent);
                    activity.finish();
//                    startActivityForResult(intent, REQUEST_CODE_OPEN_BANK_CARD);
                } else {
                    if (activity.cardRechargeInfo == null) {
                        Utils.customToast(getContext(), getString(R.string.no_selected_charge_amout_alert), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!isLoadingPaymentInfo) {
                        isLoadingPaymentInfo = true;
                        final int amount = (int) (activity.cardRechargeInfo.getAmount() * discount);
                        final String s1 = activity.cardEntity.cardName() + "充值" + Utils.getAmountText(amount) + "元";
                        Utils.showLog(TAG, "支付信息：" + s1);
                        final LoadingDialog loadingDialog = LoadingDialog.getInstance();
                        loadingDialog.setLoadingDialogListener(new LoadingDialog.LoadingDialogListener() {
                            @Override
                            public void onDismiss() {
                                isLoadingPaymentInfo = false;
                            }

                            @Override
                            public void onShow() {
                                activity.orderNum = String.valueOf(System.currentTimeMillis());
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
                                                    String.valueOf(amount),
                                                    String.valueOf(amount),
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
                                                Utils.customToast(getContext(), "发起支付出错，参数不正确！", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onNext(final String s) {
                                                super.onNext(s);
                                                Intent intent = new Intent(getContext(), OPaySDKActivity.class);
                                                CardEntity selectedCard = pllBankcards.getSelectedCard();
                                                if (selectedCard != null) {
                                                    activity.bankCards.add(0, activity.bankCards.remove(activity.bankCards.indexOf(selectedCard)));
                                                }
                                                intent.putExtra(OPaySDKActivity.PAYMENT_ARGUMENT_CARDLIST, activity.bankCards);
                                                String orderDesc = s1.replace("¥ ", "");
                                                BankDebitInfo bankDebitInfo = new BankDebitInfo(
                                                        String.valueOf(amount),//支付金额
                                                        activity.cardEntity.cardMainType(),//卡片类型
                                                        String.valueOf(activity.cardEntity.cardId()),//卡片id
                                                        activity.cardEntity.cardName(),//卡片名称
                                                        activity.cardEntity.cardNo(),//卡号
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
                                                activity.startActivityForResult(intent, REQUEST_CODE_RECHARGE);
                                            }
                                        });
                            }
                        });
                        loadingDialog.show(getFragmentManager(), LoadingDialog.class.getName());
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int count = gvRecharge.getAdapter().getCount();
        View view1 = gvRecharge.getAdapter().getView(0, null, gvRecharge);
        view1.measure(0, 0);
        int measuredHeight = view1.getMeasuredHeight();
        if (count <= 3) {
            ViewGroup.LayoutParams layoutParams = gvRecharge.getLayoutParams();
            layoutParams.height = measuredHeight + Utils.dip2px(getContext(), 10) * 2;
            gvRecharge.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = gvRecharge.getLayoutParams();
            int verticalSpacing = gvRecharge.getVerticalSpacing();
            Utils.showLog(TAG, "竖向间距：" + verticalSpacing);
            layoutParams.height = measuredHeight * 2 + verticalSpacing + Utils.dip2px(getContext(), 5) * 2;
            gvRecharge.setLayoutParams(layoutParams);
        }

        pllBankcards.adjustSize();
    }

    private static final String TAG = "CardRechargeMainFragment";

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RECHARGE) {
            if (resultCode == activity.RESULT_OK) {
                activity.paymentResult = 1;
                activity.bankDebitInfo = (BankDebitInfo) data.getSerializableExtra(BankDebitInfo.BANK_DEBIT_INFO);
                Utils.showLog(TAG, "支付信息：" + activity.bankDebitInfo.toString());
                activity.orderNo = data.getStringExtra(OPaySDKActivity.PAYMENT_ARGUMENT_TRIALNUMBER);
                Utils.showLog(TAG, "支付模块返回的流水号：" + activity.orderNo);
                activity.vpOpenCard.setCurrentItem(1);
            } else if (resultCode == activity.RESULT_CANCELED) {
                if (data != null && data.getBooleanExtra(OPayPasswordFragment.RETURN_TO_INITIAL_PAGE, false)) {
                    //返回原页面，不做任何处理
                } else {
                    activity.paymentResult = 2;
                    activity.failMessage = data != null ? data.getStringExtra(PaymentRunningDialog.FAIL_MESSAGE) : "支付已取消";
                    activity.vpOpenCard.setCurrentItem(1);
                }
            }
        }
    }
}
