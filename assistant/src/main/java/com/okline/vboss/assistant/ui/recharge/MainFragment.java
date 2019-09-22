package com.okline.vboss.assistant.ui.recharge;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cosw.sdkblecard.DeviceInfo;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.okline.ocp.CreateOrder;
import com.okline.vboss.assistant.R2;
import com.okline.vboss.assistant.net.CardType;
import com.okline.vboss.assistant.net.OLApiService;
import com.okline.vboss.assistant.R;
import com.okline.vboss.assistant.base.Config;
import com.okline.vboss.assistant.base.DefaultSubscribe;
import com.okline.vboss.assistant.net.CardCondition;
import com.okline.vboss.assistant.net.CardEntity;
import com.okline.vboss.assistant.ui.MainActivity;
import com.okline.vboss.assistant.ui.opay.BankDebitInfo;
import com.okline.vboss.assistant.ui.opay.OLResult;
import com.okline.vboss.assistant.ui.opay.OPayPasswordFragment;
import com.okline.vboss.assistant.ui.opay.OPaySDKActivity;
import com.okline.vboss.assistant.ui.opencard.UploadCardActivity;
import com.okline.vboss.http.GsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    public static final int REQUEST_CODE_OPEN_CARD = 383;
    public static final String EXTRA_RETURN_CARD_ENTITY = "return_card_entity";
    @BindView(R2.id.iv_card)
    SimpleDraweeView ivCard;
    @BindView(R2.id.tv_card_balance)
    TextView tvCardBalance;
    @BindView(R2.id.tv_recharge_amout)
    TextView tvRechargeAmout;
    @BindView(R2.id.btn_confirm)
    TextView btnConfirm;
    @BindView(R2.id.ll_recharge_amout)
    LinearLayout llRechargeAmout;
    @BindView(R2.id.gv_recharge)
    GridView gvRecharge;
    @BindView(R2.id.rl_step2)
    RelativeLayout rlStep2;
    Unbinder unbinder;
    @BindView(R2.id.pll_bank_list)
    PaymentLauchLayout pllBankList;
    private CardRechargeActivity activity;
    private static final String TAG = "MainFragment";
    private BaseAdapter gvAdapter;
    private Handler handler;
    private Runnable runnable;
    private boolean isLoadingPaymentInfo;
    private static final int REQUEST_CODE = 279;
    private boolean isBankCardAvailable;
    private CardCondition cardCondition;
    private Subscription loadCardInfo;
    private int manualClickTag;
    private Subscription createOrder;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (CardRechargeActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_assistant, container, false);
        unbinder = ButterKnife.bind(this, view);
        ivCard.setImageURI(Uri.parse(activity.cardEntity.getImgUrl()));
        int balance = activity.cardEntity.getBalance();
        int i = balance / 100;
        int i1 = balance % 100;
        String s1 = String.valueOf(i1);
        String s = String.valueOf(i)+"."+ (s1.length() == 1?("0"+s1):s1);
        tvCardBalance.setText("卡内余额：¥ "+ s);
        loadCardCondition();
        return view;
    }

    public void updatePaymentInfo() {
        llRechargeAmout.setVisibility(View.VISIBLE);
        rlStep2.setVisibility(View.VISIBLE);
        pllBankList.setVisibility(View.VISIBLE);
        isBankCardAvailable = activity.bankCards != null && !activity.bankCards.isEmpty();
        btnConfirm.setVisibility(View.VISIBLE);
        gvRecharge.setVisibility(View.GONE);
        tvRechargeAmout.setText(Utils.getAmountText(activity.cardRechargeInfo.getAmount()));
        llRechargeAmout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gvRecharge.setVisibility(View.VISIBLE);
                llRechargeAmout.setVisibility(View.GONE);
                rlStep2.setVisibility(View.GONE);
                pllBankList.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
            }
        });
        btnConfirm.setText(isBankCardAvailable?"确认":"开通银行卡");
        pllBankList.setBankCards(activity.bankCards);
        pllBankList.adjustSize();
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBankCardAvailable) {
                    manualClickTag = 0;
                    startPayment();
                } else {
                    Utils.showLog(TAG,"跳转至开卡界面开通银行卡");
                    Intent intent = new Intent(getContext(), UploadCardActivity.class);
                    intent.putExtra("card", activity.availableBanks.get(0));
                    intent.putExtra(EXTRA_RETURN_CARD_ENTITY,true);
                    startActivityForResult(intent, REQUEST_CODE_OPEN_CARD);
                }
            }
        });
    }

    private void loadCardCondition() {
        OLApiService olApiService = OLApiService.getInstance();
        loadCardInfo = Observable.zip(
                olApiService.getDownloadableCardList(CardType.BANK_CARD),
                olApiService.getCardOpenCondition(activity.cardEntity.getCardMainType(), activity.cardEntity.getAid()),
                olApiService.getDownloadedCardList(CardType.BANK_CARD),
                new Func3<List<CardEntity>, CardCondition, List<CardEntity>, Boolean>() {
                    @Override
                    public Boolean call(List<CardEntity> cardEntities, CardCondition cardCondition, List<CardEntity> cardEntities2) {
                        activity.availableBanks = (ArrayList<CardEntity>) cardEntities;
                        for (CardEntity availableBank : activity.availableBanks) {
                            Utils.showLog(TAG,"服务器回传可开银行卡："+availableBank);
                        }
                        MainFragment.this.cardCondition = cardCondition;
                        activity.bankCards = (ArrayList<CardEntity>) cardEntities2;
                        for (CardEntity bankCard : activity.bankCards) {
                            Utils.showLog(TAG,"可用银行卡："+bankCard);
                        }
                        return true;
                    }
                }
        )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<Boolean>(TAG) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Utils.showLog(TAG, "getCardOpenCondition出错：" + e.getMessage());
                        Utils.customToast(getContext(), "未能获取到充值信息，请稍候再试", Toast.LENGTH_SHORT).show();
                        Utils.unsubscribeRxJava(loadCardInfo);
                        activity.finish();
                    }

                    @Override
                    public void onNext(Boolean cardCondition) {
                        super.onNext(cardCondition);
                        ArrayList<CardRechargeInfo> cardRechargeInfos = new ArrayList<>();
                        for (Integer integer : MainFragment.this.cardCondition.getAmountList()) {
                            cardRechargeInfos.add(new CardRechargeInfo(integer, false));
                        }
                        showCardCondition(cardRechargeInfos);
                        Utils.unsubscribeRxJava(loadCardInfo);
                    }
                });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pllBankList.adjustSize();
        pllBankList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //暂无动作
            }
        });
    }

    private void startPayment() {
        if (activity.cardRechargeInfo == null) {
            Utils.customToast(getContext(), "请选择充值金额", Toast.LENGTH_SHORT).show();
            return;
        }
        if (activity.bankCards == null || activity.bankCards.isEmpty()) {
            Utils.customToast(getContext(), "没有可用的银行卡，请重试", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isLoadingPaymentInfo) {
            isLoadingPaymentInfo = true;
            if (MainActivity.ocardState != 1){
                OLApiService.getInstance().requestOCardConnection(getContext(),Config.ADDRESS,5*1000)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultSubscribe<DeviceInfo>(TAG){
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                Utils.showLog(TAG,"查询欧卡状态出错："+e.getMessage());
                                Utils.customToast(getContext(),"该业务需要蓝牙功能，请检查欧卡与手机蓝牙连接是否正常", Toast.LENGTH_SHORT).show();
                                isLoadingPaymentInfo = false;
                            }

                            @Override
                            public void onNext(DeviceInfo deviceInfo) {
                                super.onNext(deviceInfo);
                                if (deviceInfo != null) {
                                    Utils.showLog(TAG,"获取到的DeviceInfo对象："+deviceInfo.getDeviceName());
                                    executePayment();
                                } else {
                                    onError(new Exception("onNext返回的DeviceInfo对象为null"));
                                }
                            }
                        });
            } else {
                executePayment();
            }
        }
    }

    private void executePayment() {
        int amount = activity.cardRechargeInfo.getAmount();
        String s = String.valueOf(amount);
        String s2 = s.substring(0, s.length() - 2) + "." + s.substring(s.length() - 2);
        activity.orderDesc = activity.cardEntity.getCardName() + "充值" + s2 + "元";
        Utils.showLog(TAG, "支付信息：" + activity.orderDesc);
        activity.orderNum = System.nanoTime() + "";
        createOrder = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String resultJson = new CreateOrder().createOrder(
                            Config.OL_NUM,
                            activity.orderNum,
                            "1",
                            Config.USER_NAME,
                            Config.MOBILE,
                            String.valueOf(activity.cardRechargeInfo.getAmount()),
                            String.valueOf(activity.cardRechargeInfo.getAmount()),
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
                        isLoadingPaymentInfo = false;
                        Utils.showLog(TAG, "支付出错：" + throwable.getMessage());
                        Utils.customToast(getContext(), "流水号生成出错，请稍候重试！", Toast.LENGTH_SHORT).show();
                        Utils.unsubscribeRxJava(createOrder);
                    }

                    @Override
                    public void onNext(final String s) {
                        super.onNext(s);
                        isLoadingPaymentInfo = false;
                        Intent intent = new Intent(getContext(), OPaySDKActivity.class);
                        ArrayList<CardEntity> cardEntities = new ArrayList<>();
                        cardEntities.add(pllBankList.getSelectedCard());
                        intent.putExtra(OPaySDKActivity.PAYMENT_ARGUMENT_CARDLIST, cardEntities);
                        BankDebitInfo bankDebitInfo = new BankDebitInfo(
                                String.valueOf(activity.cardRechargeInfo.getAmount()),//支付金额
                                activity.cardEntity.getCardMainType(),//卡片类型
                                String.valueOf(activity.cardEntity.getCardId()),//卡片id
                                activity.cardEntity.getCardName(),//卡片名称
                                activity.cardEntity.getCardNo(),//卡号
                                activity.orderNum,//商户订单号
                                "6",//商户号
                                s,//流水号
                                activity.orderDesc,//订单描述
                                true, //已选中银行卡
                                false//显示结果页
                        );
                        intent.putExtra(BankDebitInfo.BANK_DEBIT_INFO, bankDebitInfo);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Utils.showLog(TAG, "支付控件弹出开卡标记：" + manualClickTag);
                        if (0 == manualClickTag++) {
                            Utils.showLog(TAG, "弹出支付控件！");
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                        Utils.unsubscribeRxJava(createOrder);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.showLog(TAG,"支付模块返回：requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        switch (requestCode) {
            case REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        BankDebitInfo bankDebitInfo = (BankDebitInfo) data.getSerializableExtra(BankDebitInfo.BANK_DEBIT_INFO);
                        Utils.showLog(TAG,"支付结果："+bankDebitInfo);
                        activity.bankDebitInfo=bankDebitInfo;
                        activity.vpCardRecharge.setCurrentItem(1);
                        break;
                    case Activity.RESULT_CANCELED:
                        //            支付失败退回
//            Utils.customToast(getContext(),"未能支付成功，请重试", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case REQUEST_CODE_OPEN_CARD:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        CardEntity serializableExtra = (CardEntity) data.getSerializableExtra(UploadCardActivity.CARD_INSTANCE);
                        activity.bankCards.add(serializableExtra);
                        updatePaymentInfo();
                        break;
                    case Activity.RESULT_CANCELED:
                        Utils.customToast(getContext(),"开通银行卡失败，请重试", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }

    private void adjustGridView() {
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
            layoutParams.height = measuredHeight * 2 + verticalSpacing + Utils.dip2px(getContext(), 5) * 2;
            gvRecharge.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Utils.unsubscribeRxJava(loadCardInfo);
        unbinder.unbind();
    }

    public void showCardCondition(final ArrayList<CardRechargeInfo> cardRechargeInfos) {
        gvRecharge.setVisibility(View.VISIBLE);
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
                convertView = View.inflate(getContext(), R.layout.view_open_card_recharge_assistant, null);
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
                for (CardRechargeInfo rechargeInfo : cardRechargeInfos) {
                    rechargeInfo.setSelected(cardRechargeInfos.indexOf(rechargeInfo) == position);
                }
                gvAdapter.notifyDataSetChanged();
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        updatePaymentInfo();
                    }
                };
                handler.postDelayed(runnable, 200);
            }
        });
        adjustGridView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
