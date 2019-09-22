package com.vboss.okline.ui.contact.TransferAccounts;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.okline.ocp.CreateOrder;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.data.CardRepository;
import com.vboss.okline.data.GsonUtils;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.data.local.DataConfig;
import com.vboss.okline.ui.app.App;
import com.vboss.okline.ui.app.adapter.CommonAdapter;
import com.vboss.okline.ui.app.adapter.ViewHolder;
import com.vboss.okline.ui.app.jiugongge.AnimationDialog;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.opay.OLResult;
import com.vboss.okline.ui.opay.OPaySDKActivity;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.ui.user.customized.LoadingDialog;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.view.widget.PaymentLauchLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.hyphenate.easeui.ChatContentUtil.getContext;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: Yuan shaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/5/7 10:51 <br/>
 * Summary  : 转账支付通道界面
 */

public class TransPaymentActivity extends BaseActivity{
    private static final String TAG = "TransPaymentActivity";
    public static final int REQUEST_CODE = 2017;
    public static final String P_ID = "olApp";
    public static final String P_CODE = "OKLine";
    public static final String OL_NO = DataConfig.DEFAULT_OLNO;

    @BindView(R.id.fragment_toolbar)
    FragmentToolbar toolbar;

    @BindView(R.id.selected_income_Account)
    RelativeLayout selected_income_Account;

    @BindView(R.id.incomeAccount_cardicon)
    SimpleDraweeView incomeAccount_cardicon;

    @BindView(R.id.incomeAccount_cardName)
    TextView incomeAccount_cardName;

    @BindView(R.id.incomeAccount_name)
    TextView incomeAccount_name;

    @BindView(R.id.incomeAccount_cardNo)
    TextView incomeAccount_cardNo;

    @BindView(R.id.trans_edit_money)
    EditText trans_edit_money;

    @BindView(R.id.trans_to_pay_button)
    TextView trans_to_pay_button;

    @BindView(R.id.rmb)
    TextView rmb;

    CommonAdapter<CardEntity> bankRecordAdapter;
    List<CardEntity> bankRecordList;
    @BindView(R.id.transfer_paymentLauch)
    PaymentLauchLayout paymentLauchLayout;
    AnimationDialog dialog;
    RecyclerView selectDialog_recyclerView;
    String friendOlNo;
    String cardNo;
    AnimationDialog requestDialog;
    View dialogView;
    private boolean isLoadingPaymentInfo;
    User user;
    CardEntity cardEntityInfo;
    ArrayList<CardEntity> quickCardLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_trans_payment);
        ButterKnife.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
        getQuickCardList();

        Intent intent = getIntent();
        friendOlNo = intent.getStringExtra("olNo");

        trans_edit_money.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //得到焦点
                }else {
                    //失去焦点  关闭软键盘
                    hideSoftInput();
                }
            }
        });

        trans_edit_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //显示“¥”
                rmb.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //获取选中卡信息；
        cardEntityInfo = paymentLauchLayout.getSelectedCard();

        trans_to_pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/5/7 走支付接口  暂时直接跳转到转账记录界面
                user = UserRepository.getInstance(TransPaymentActivity.this).getUser();

                if (selected_income_Account.getVisibility() == View.VISIBLE && trans_edit_money.getText() != null) {
                    startTrans();
//
//                    Intent intent = new Intent(TransPaymentActivity.this,SelectAccountActivity.class);
//                    startActivity(intent);
//                    Intent intent = new Intent(TransPaymentActivity.this,TransRecordActivity.class);
//                    startActivity(intent);
                }
            }
        });
    }

    private void startTrans() {
        if (!isLoadingPaymentInfo) {
            isLoadingPaymentInfo = true;

            final LoadingDialog loadingDialog = LoadingDialog.getInstance();
            loadingDialog.setLoadingDialogListener(new LoadingDialog.LoadingDialogListener() {
                @Override
                public void onDismiss() {
                    isLoadingPaymentInfo = false;
                }

                @Override
                public void onShow() {
                    final String orderNo = String.valueOf(System.currentTimeMillis());
                    Observable.create(new Observable.OnSubscribe<String>() {
                        @Override
                        public void call(Subscriber<? super String> subscriber) {
                            try {
                                String resultJson = new CreateOrder().createOrder(
                                        user != null ? user.getOlNo() : OL_NO,
                                        orderNo,
                                        "1",
                                        user != null ? user.getRealName() : "张三",
                                        user != null ? user.getPhone() : "17681821398",
                                        String.valueOf(trans_edit_money.getText()),
                                        String.valueOf(trans_edit_money.getText()),
                                        "6",
                                        P_ID,
                                        P_CODE);
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
                                    Utils.customToast(getContext(), "流水号生成出错，请稍候重试！", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNext(final String s) {
                                    super.onNext(s);
//                                    Intent intent = new Intent(getContext(), OPaySDKActivity.class);
//                                    intent.putExtra(OPaySDKActivity.PAYMENT_ARGUMENT_ORDERNUMBER, orderNo);
//                                    intent.putExtra(OPaySDKActivity.PAYMENT_ARGUMENT_TRIALNUMBER, s);
//                                    intent.putExtra(OPaySDKActivity.PAYMENT_ARGUMENT_ORDERAMOUT, String.valueOf(trans_edit_money.getText()));
//                                    intent.putExtra(OPaySDKActivity.PAYMENT_ARGUMENT_MERNO, "6");
//                                    intent.putExtra(OPaySDKActivity.PAYMENT_ARGUMENT_ORDERDESC, "转账"+trans_edit_money.getText() + "元");
//                                    intent.putExtra(OPaySDKActivity.PAYMENT_ARGUMENT_SHOWRESULTPAGE, false);
//                                    intent.putExtra(OPaySDKActivity.PAYMENT_ARGUMENT_CARDID, String.valueOf(cardEntityInfo.cardId()));
//                                    intent.putExtra(OPaySDKActivity.PAYMENT_ARGUMENT_CARDTYPE, cardEntityInfo.cardMainType());
//                                    intent.putExtra(OPaySDKActivity.PAYMENT_ARGUMENT_CARDLIST, quickCardLists);
//                                    intent.putExtra(OPaySDKActivity.PAYMENT_ARGUMENT_CARD_SELECTED, true);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    Utils.showLog(TAG, "关闭加载框");
//                                    loadingDialog.onFinished(null, 0);
//                                    TransPaymentActivity.this.startActivityForResult(intent, REQUEST_CODE);
//                                    onCompleted();
                                }
                            });
                }
            });
            loadingDialog.show(getSupportFragmentManager(),LoadingDialog.class.getName());
        }
    }


    private void initBankRecordAdapter() {
        bankRecordList = new ArrayList<>();
        bankRecordAdapter = new CommonAdapter<CardEntity>(TransPaymentActivity.this,R.layout.bank_record_item,bankRecordList) {
            @Override
            public void convert(final ViewHolder holder, CardEntity cardEntity, int position) {
                holder.setOnClickListener(R.id.bank_Record_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        selected_income_Account.setVisibility(View.VISIBLE);
                        incomeAccount_cardName.setText(holder.getText(R.id.trans_dialog_cardName));
                        incomeAccount_name.setText(holder.getText(R.id.trans_dialog_personName));
                        incomeAccount_cardNo.setText(holder.getText(R.id.trans_dialog_cardNo));
                    }
                });
            }
        };
        selectDialog_recyclerView.setLayoutManager(new LinearLayoutManager(TransPaymentActivity.this));
        selectDialog_recyclerView.setAdapter(bankRecordAdapter);


    }

    private void initTransRecordBankList() {
        UserRepository userRepository = UserRepository.getInstance(TransPaymentActivity.this);
        userRepository.queryTransferCardList(friendOlNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<CardEntity>>(TAG){
                    @Override
                    public void onNext(List<CardEntity> cardEntities) {
                        super.onNext(cardEntities);
                        bankRecordList.addAll(cardEntities);
                        if (bankRecordList.size() > 0) {
                            dialog.showDialog(dialogView);
                        }else {
                            initQueryDialog();
                        }
                        Log.i(TAG,"转账卡片记录列表 cardEntities :" +cardEntities.size());
                    }
                });

        bankRecordAdapter.setmDatas(bankRecordList);
        bankRecordAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.select_incomeAccount})
    public void ViewOnClick(View view) {
        switch (view.getId()) {
            case R.id.select_incomeAccount:
                //支付的银行卡号
                cardNo = paymentLauchLayout.getSelectedCard().cardNo();
                //选择收款账户
                dialogView = LayoutInflater.from(TransPaymentActivity.this).inflate(R.layout.select_income_ccounts_dialog, null);
                dialog = new AnimationDialog(TransPaymentActivity.this);

                dialogView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                selectDialog_recyclerView = (RecyclerView) dialogView.findViewById(R.id.incomeAccounts_recyclerView);

                //对方提供账户
                TextView otherside_offer = (TextView) dialogView.findViewById(R.id.otherside_offer);
                otherside_offer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initQueryDialog();
                    }
                });
                initBankRecordAdapter();

                initTransRecordBankList();

                break;
            default:
                break;
        }
    }

    private void initQueryDialog() {
        //询问是否邀请的弹窗
        View requestOfferView = LayoutInflater.from(TransPaymentActivity.this).inflate(R.layout.request_offer_dialog_layout,null);
        requestDialog = new AnimationDialog(TransPaymentActivity.this);
        requestDialog.showDialog(requestOfferView);
        requestOfferView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog.dismiss();
            }
        });

        TextView button_yes = (TextView) requestOfferView.findViewById(R.id.quest_offer_yes);
        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog.dismiss();
                if (dialog !=null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                TransHelper.getInstance().startTransRequest(cardNo,friendOlNo);
                finish();
            }
        });
        requestDialog.show();
    }

    /**
     *
     */
    public void getQuickCardList(){
        CardRepository repository = CardRepository.getInstance(getContext());
        repository.cardList(CardType.BANK_CARD)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<CardEntity>>(TAG) {
                    @Override
                    public void onNext(List<CardEntity> cardEntities) {
                        super.onNext(cardEntities);
                        quickCardLists = new ArrayList<>();
                        for (int i = 0; i < cardEntities.size(); i++) {
                            if (cardEntities.get(i).isQuickPass() == 2) {
                                quickCardLists.add(cardEntities.get(i));
                            }
                        }
                        paymentLauchLayout.setBankCards(quickCardLists);
                    }
                });
    }

    private void initToolBar() {
        toolbar.setActionTitle("转账");
        toolbar.setActionLogoIcon(R.mipmap.orange_logo);
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                // 返回一级界面
                finish();
            }
        });

    }

    /**
     * 取消软键盘
     */
    private void hideSoftInput() {
        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideSoftInput();
    }
}