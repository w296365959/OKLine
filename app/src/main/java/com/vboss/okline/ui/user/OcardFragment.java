package com.vboss.okline.ui.user;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cosw.sdkblecard.DeviceInfo;
import com.facebook.drawee.view.SimpleDraweeView;
import com.idcard.CardInfo;
import com.idcard.TFieldID;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.OKCard;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.auth.CameraIDActivity;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.callback.OCadAbsenceDeclarationListener;
import com.vboss.okline.ui.user.callback.OCardInfoListener;
import com.vboss.okline.ui.user.callback.OCardResumeListener;
import com.vboss.okline.ui.user.customized.CustomDialog;
import com.vboss.okline.ui.user.customized.LoadingDialog;
import com.vboss.okline.ui.user.customized.OcardButton;
import com.vboss.okline.ui.user.customized.UnionLoadingDialog;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.view.widget.OKCardView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/3/28
 * Summary : 个人中心主页面fragment
 */
public class OcardFragment extends BaseFragment implements OCardContract.View, View.OnClickListener {

    public static final int REQUEST_CODE = 561;
    public static final String INTENT_TAG = "tag";
    @BindView(R.id.action_back)
    ImageButton actionBack;
    @BindView(R.id.action_back_layout)
    RelativeLayout actionBackLayout;
    @BindView(R.id.action_title)
    TextView actionTitle;
    @BindView(R.id.action_menu_button)
    ImageButton actionMenuButton;
    @BindView(R.id.action_menu_layout)
    RelativeLayout actionMenuLayout;
    @BindView(R.id.iv_ocard_header)
    ImageView ivOcardHeader;
    @BindView(R.id.tv_ocard_online_tag)
    TextView tvOcardOnlineTag;
    @BindView(R.id.iv_ocard_state)
    LogoView ivOcardState;
    @BindView(R.id.okcard_view)
    OKCardView okcardView;
    @BindView(R.id.btn_ocard_absence_declaration)
    OcardButton btnOcardAbsenceDeclaration;
    @BindView(R.id.btn_ocard_resume)
    OcardButton btnOcardResume;
    @BindView(R.id.btn_ocard_apply_new)
    OcardButton btnOcardApplyNew;
    @BindView(R.id.ocard_absence_date)
    TextView ocardAbsenceDate;
    @BindView(R.id.ocard_absence_status)
    LinearLayout ocardAbsenceStatus;
    @BindView(R.id.text_card_number)
    TextView textCardNumber;
    @BindView(R.id.text_birthday)
    TextView textBirthday;
    @BindView(R.id.text_total_volume)
    TextView textTotalVolume;
    @BindView(R.id.text_available_volume)
    TextView textAvailableVolume;
    @BindView(R.id.text_version_info)
    TextView textVersionInfo;
    @BindView(R.id.text_battery_volume)
    TextView textBatteryVolume;
    private OKCard okCard;
    private View view;
    private OCardPresenter oCardPresenter;
    private MainActivity activity;
    public static final int OCARD_STATUS_NORMAL = 1;
    public static final int OCARD_STATUS_ABSENCE_DECLARED = 2;
    public static final int OCARD_STATUS_ABSENCE_DECLARED_TIMEOUT = 3;
    public static final String TAG = "OcardFragment";
    private LoadingDialog loadingDialog;
    private int intent_state;
    private User user;
    private Unbinder unbinder;
    private boolean bluetoothConnected;
    private Subscription subscription;
    private UnionLoadingDialog unionLoadingDialog;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("OcardFragment.onAttach");
    }

    public static OcardFragment newInstance() throws IllegalStateException {
        OcardFragment ocardFragment = new OcardFragment();
        return ocardFragment;
    }

    public OcardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("OcardFragment.onCreateView");
        activity = (MainActivity) getActivity();
        this.okCard = BaseActivity.okCard;
        Utils.showLog(TAG,"初始化时欧卡："+okCard);
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ocard, container, false);
        unbinder = ButterKnife.bind(this, view);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        user = UserRepository.getInstance(getContext()).getUser();
        oCardPresenter = new OCardPresenter(this, activity);
        oCardPresenter.getOCardInfo(new OCardInfoListener() {
            @Override
            public void onStart() {
                unionLoadingDialog = new UnionLoadingDialog();
                unionLoadingDialog.show(getFragmentManager(),UnionLoadingDialog.class.getName());
            }

            @Override
            public void onFetch(DeviceInfo de) {
                if (unionLoadingDialog != null) {
                    unionLoadingDialog.dismiss();
                }
                Utils.customToast(getContext(), "欧卡已连接", Toast.LENGTH_SHORT).show();
                bluetoothConnected = true;
                Utils.showLog(TAG, "欧卡蓝牙连接状态：" + de);
                if (de != null) {
                    SharedPreferences.Editor editor = activity.sharedPreferences.edit();
                    editor.putString(UserFragment.AVAILABLE_VOLUME, "93KB");
                    editor.putString(UserFragment.TOTAL_VOLUME, "180KB");
                    editor.putString(UserFragment.BATTERY_VOLUME, de.getDumpEnergy() + "%");
                    editor.putString(UserFragment.VERSION_INFO, de.getDeviceCosVersion());
                    editor.apply();
                }
                initUI();
            }

            @Override
            public void onErr(String message) {
                if (unionLoadingDialog != null) {
                    unionLoadingDialog.dismiss();
                }
                Utils.customToast(getContext(), "未检测到绑定的欧卡", Toast.LENGTH_SHORT).show();
                bluetoothConnected = false;
            }
        });

        //注册欧卡状态广播
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BaseActivity.ACTION_OCARD_STATE_CHANGED);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onOcardStateChanged();
            }
        };
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

        btnOcardAbsenceDeclaration.setOnClickListener(this);
        btnOcardApplyNew.setOnClickListener(this);
        btnOcardResume.setOnClickListener(this);
        return view;
    }

    private void onOcardStateChanged() {
        switch (BaseActivity.getOcardState()) {
            case BaseActivity.OCARD_STATE_BOND:
                bluetoothConnected = true;
                break;
            case BaseActivity.OCARD_STATE_NOT_CONNECTED:
                bluetoothConnected = false;
                break;
        }
        initUI();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        System.out.println("OcardFragment.onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        initUI();
        actionBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.removeSecondFragment();
            }
        });
        actionTitle.setText(R.string.my_ocard);
        actionMenuButton.setVisibility(View.INVISIBLE);
        ivOcardHeader.setImageResource(bluetoothConnected?R.drawable.ocard_online:R.drawable.ocard_offline);
        tvOcardOnlineTag.setVisibility(bluetoothConnected?View.GONE:View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("OcardFragment.onDestroyView");
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        oCardPresenter.onViewDestroy();
        unbinder.unbind();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        System.out.println("OcardFragment.onHiddenChanged:"+hidden);
        if (!hidden) {
            subscription = UserRepository.getInstance(getContext()).getOCard().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultSubscribe<OKCard>(TAG) {
                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
//                            instance.onFinished(null, 0);
                            Utils.customToast(getContext(), getString(R.string.ocard_info_refresh_failed), Toast.LENGTH_SHORT).show();
                            initUI();
                        }

                        @Override
                        public void onNext(OKCard okCard) {
                            Utils.showLog(TAG, "OcardFragment.onHiddenChanged查询到的欧卡: " + okCard.toString());
                            super.onNext(okCard);
                            OcardFragment.this.okCard = okCard;
//                            instance.onFinished(null, 0);
                            initUI();
                        }
                    });
        }
    }

    @Override
    public void initUI() {
        if (textCardNumber != null) {
            Utils.showLog(TAG, "调用initUI：" + Utils.transformatTime(System.currentTimeMillis(), 3));
            textBirthday.setText(BaseActivity.okCard.getBindDate());
            textCardNumber.setText(BaseActivity.okCard.getDeviceNo());
            if (bluetoothConnected) {
                textAvailableVolume.setText(getInfo(UserFragment.AVAILABLE_VOLUME));
                textBatteryVolume.setText(getInfo(UserFragment.BATTERY_VOLUME));
                textTotalVolume.setText(getInfo(UserFragment.TOTAL_VOLUME));
                textVersionInfo.setText(getInfo(UserFragment.VERSION_INFO));
                ivOcardHeader.setImageResource(R.drawable.ocard_online);
                tvOcardOnlineTag.setVisibility(View.GONE);
            } else {
                ivOcardHeader.setImageResource(R.drawable.ocard_offline);
                tvOcardOnlineTag.setVisibility(View.VISIBLE);
                textAvailableVolume.setText("");
                textBatteryVolume.setText("");
                textTotalVolume.setText("");
                textVersionInfo.setText("");
            }
            if (okCard != null && (okCard.getBindState() == 2 || okCard.getBindState() == 3 ))  {
                ocardAbsenceStatus.setVisibility(View.VISIBLE);
                ocardAbsenceDate.setText("挂失时间：" + okCard.getLossTime());
            } else {
                ocardAbsenceStatus.setVisibility(View.GONE);
            }

            if (okCard != null) {
                switch (okCard.getIsBind()) {
                    case 0:
    //                未绑定
                        break;
                    case 1:
    //                已绑定
                        break;
                }

                int status = okCard.getBindState();
                initButtons(status,bluetoothConnected);
    //        1.正常 2.已挂失 3.已失效
            }
        }
    }

    private String getInfo(String s) {
        return activity.sharedPreferences.getString(s, null);
    }

    private void initButtons(int status, boolean bluetoothConnected) {
        switch (status) {
            case OCARD_STATUS_NORMAL:
                btnOcardAbsenceDeclaration.setStatus(bluetoothConnected?0:1);
                btnOcardResume.setStatus(0);
                btnOcardApplyNew.setStatus(0);
                break;
            case OCARD_STATUS_ABSENCE_DECLARED:
                btnOcardAbsenceDeclaration.setStatus(2);
                btnOcardResume.setStatus(1);
                btnOcardApplyNew.setStatus(1);
                break;
            case OCARD_STATUS_ABSENCE_DECLARED_TIMEOUT:
                btnOcardAbsenceDeclaration.setStatus(2);
                btnOcardResume.setStatus(0);
                btnOcardApplyNew.setStatus(1);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ocard_absence_declaration:
                Utils.showLog(TAG, "点击挂失按钮");
                switch (btnOcardAbsenceDeclaration.getStatus()) {
                    case 0:

                        break;
                    case 1:
                        Utils.showLog(TAG, "可以挂失");
                        new CustomDialog(getContext(), null, getString(R.string.absence_declaration_confirm), null, getString(R.string.cancel), getString(R.string.confirm), new CustomDialog.DialogClickListener() {
                            @Override
                            public void onNegtiveClick() {

                            }

                            @Override
                            public void onPositiveClick() {
                                loadingDialog = LoadingDialog.getInstance();
                                loadingDialog.setLoadingDialogListener(new LoadingDialog.LoadingDialogListener() {
                                    @Override
                                    public void onDismiss() {

                                    }

                                    @Override
                                    public void onShow() {
                                        oCardPresenter.declareAbsence(
                                                User.DEV_OCARD,
                                                user != null ? user.getDeviceNo() : (TextUtils.isEmpty(okCard.getDeviceNo()) ? activity.sharedPreferences.getString(UserFragment.DEVICE_NO, null) : okCard.getDeviceNo()),
                                                new OCadAbsenceDeclarationListener() {
                                                    @Override
                                                    public void onSuccess(OKCard okCard) {
                                                        OcardFragment.this.okCard = okCard;
                                                        Utils.showLog(TAG, "挂失成功！ 返回数据：" + okCard.toString());
                                                        initUI();
                                                        loadingDialog.onFinished("挂失成功", 2000);
                                                    }

                                                    @Override
                                                    public void onFail(String message, OKCard okCard) {
                                                        if (message != null) {
                                                            loadingDialog.onFinished(null, 0);
                                                            Utils.showLog(TAG, "欧卡挂失出错：" + message);
                                                            Utils.customToast(getContext(), "挂失操作失败，请稍后再试", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            onSuccess(okCard);
                                                        }
                                                    }
                                                });
                                    }
                                });
                                loadingDialog.show(getFragmentManager(), LoadingDialog.class.getName());
                            }
                        }, CustomDialog.MODE_OKLINE2).show();
                        break;
                    case 2:

                        break;
                }
                break;
            case R.id.btn_ocard_resume:
                Utils.showLog(TAG, "点击重启按钮");
                switch (btnOcardResume.getStatus()) {
                    case 0:

                        break;
                    case 1:
                        Utils.showLog(TAG, "可以重启");
                        new CustomDialog(getContext(), null, getString(R.string.ocard_resume_confirm), "确定重启吗？", getString(R.string.cancel), getString(R.string.confirm), new CustomDialog.DialogClickListener() {
                            @Override
                            public void onNegtiveClick() {

                            }

                            @Override
                            public void onPositiveClick() {
                                intent_state = 1;
                                Intent intent = new Intent(getContext(), CameraIDActivity.class);
                                intent.putExtra(INTENT_TAG, TAG);
                                startActivityForResult(intent, REQUEST_CODE);
                            }
                        }, CustomDialog.MODE_OKLINE2).show();
                        break;
                }
                break;
            case R.id.btn_ocard_apply_new:
                Utils.showLog(TAG, "点击补新卡按钮");
                switch (btnOcardApplyNew.getStatus()) {
                    case 0:

                        break;
                    case 1:
                        Utils.showLog(TAG, "可以补新卡");
                        new CustomDialog(getContext(), null, getString(R.string.ocard_applynew_confirm), "确定补新卡吗", getString(R.string.cancel), getString(R.string.confirm), new CustomDialog.DialogClickListener() {
                            @Override
                            public void onNegtiveClick() {

                            }

                            @Override
                            public void onPositiveClick() {
                                intent_state = 2;
                                Intent intent = new Intent(getContext(), CameraIDActivity.class);
                                intent.putExtra(INTENT_TAG, TAG);
                                startActivityForResult(intent, REQUEST_CODE);
                            }
                        }, CustomDialog.MODE_OKLINE2).show();
                        break;
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.showLog(TAG, "requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (requestCode == REQUEST_CODE && data != null && data.hasExtra("CARD")) {
            CardInfo card = (CardInfo) data.getSerializableExtra("CARD");
            Utils.showLog(TAG, card.getAllinfo());
            String realName = card.getFieldString(TFieldID.NAME);
            Utils.showLog(TAG, "姓名：" + realName);
            String idCardNo = card.getFieldString(TFieldID.NUM);
            Utils.showLog(TAG, "身份证号：" + idCardNo);

            final String deviceNo = TextUtils.isEmpty(okCard.getDeviceNo()) ? activity.sharedPreferences.getString(UserFragment.DEVICE_NO, null) : okCard.getDeviceNo();

            final LoadingDialog loadingDialog1 = LoadingDialog.getInstance();
            UserRepository.getInstance(getContext()).authenticateToHandleOCard(Integer.parseInt(User.CREDIT_TYPE_ID), realName, idCardNo)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultSubscribe<Boolean>(TAG) {
                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            Utils.showLog(TAG, "身份验证出错：" + throwable.getMessage());
                            intent_state = 0;
                            loadingDialog1.onFinished(null, 0);
                            Utils.customToast(getContext(), getString(R.string.id_approve_fail), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            super.onNext(aBoolean);
                            if (aBoolean) {
                                if (intent_state == 1) {
                                    intent_state = 0;
                                    oCardPresenter.resume(
                                            User.DEV_OCARD,
                                            user != null ? (user.getDeviceNo()) : deviceNo,
                                            new OCardResumeListener() {
                                                @Override
                                                public void onSuccess(OKCard okCard) {
                                                    loadingDialog1.onFinished("重启成功！", 1000);
                                                    OcardFragment.this.okCard = okCard;
                                                    initUI();
                                                }

                                                @Override
                                                public void onFail() {
                                                    loadingDialog1.onFinished(null, 0);
                                                }
                                            });
                                } else if (intent_state == 2) {
                                    intent_state = 0;
                                    loadingDialog1.onFinished(null, 0);
                                    activity.addSecondFragment(OCardAttachFragment.instance(true));
                                }
                            } else {
                                intent_state = 0;
                                loadingDialog1.onFinished(null, 0);
                                Utils.customToast(getContext(), getString(R.string.id_approve_fail), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            loadingDialog1.show(getFragmentManager(), LoadingDialog.class.getName());
        }
    }
}
