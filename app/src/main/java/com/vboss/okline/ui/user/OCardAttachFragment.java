package com.vboss.okline.ui.user;


import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cosw.sdkblecard.DeviceInfo;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.OKCard;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.customized.CustomDialog;
import com.vboss.okline.ui.user.customized.LoadingDialog;
import com.vboss.okline.ui.user.customized.LoadingLayout;
import com.vboss.okline.view.widget.OKCardView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/3/28
 * Summary : 添加欧卡页面
 */
public class OCardAttachFragment extends BaseFragment implements OCardAttachContract.View {


    public static final int REQUEST_CODE_PERMISSION = 273;
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
    @BindView(R.id.iv_ocard_state)
    LogoView ivOcardState;
    @BindView(R.id.okcard_view)
    OKCardView okcardView;
    @BindView(R.id.tv_ocard_online_tag)
    TextView tvOcardOnlineTag;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.ll_step1)
    LoadingLayout llStep1;
    @BindView(R.id.ll_step2)
    LoadingLayout llStep2;
    @BindView(R.id.ll_step3)
    LoadingLayout llStep3;
    @BindView(R.id.text_no_nfc_header)
    TextView textNoNfcHeader;
    @BindView(R.id.et_ocard_number)
    EditText etOcardNumber;
    @BindView(R.id.tv0)
    TextView tv0;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv5)
    TextView tv5;
    @BindView(R.id.btn_ocard_number)
    TextView btnOcardNumber;
    @BindView(R.id.ll_no_NFC)
    LinearLayout llNoNFC;
    @BindView(R.id.ll_step4)
    LoadingLayout llStep4;
    @BindView(R.id.ll_step5)
    LoadingLayout llStep5;
    @BindView(R.id.ll_step6)
    LoadingLayout llStep6;
    @BindView(R.id.tv_ocard_attatch_success_no)
    TextView tvOcardAttatchSuccessNo;
    @BindView(R.id.ll_ocard_attatch_success)
    LinearLayout llOcardAttatchSuccess;
    @BindView(R.id.btn_re_attac)
    Button btnReAttac;
    @BindView(R.id.ll_ocard_attatch_fail)
    LinearLayout llOcardAttatchFail;
    private View view;
    private boolean isNFCavailable;
    private OCardAttachPresenter basePresenter;
    private MainActivity activity;
    private String manualTextOCardNum;
    private String okCardID = "1234567890123456";
    private boolean enable_nfc_intent;
    private boolean isApplyNew;
    private Subscription subscription;
    private Handler handler;
    private Runnable runnable;
    private int timeRes;
    private Runnable runnable1;
    private Handler handler1;
    private boolean requestPermission;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Activity.RESULT_CANCELED) {
                Utils.showLog(TAG,"收到回传");
                activity.removeSecondFragment();
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Utils.showLog(TAG, "OCardAttachFragment.onAttach");
        activity = (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ocard_attach, container, false);
        ButterKnife.bind(this, view);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        actionBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.removeSecondFragment();
            }
        });
        actionTitle.setText("添加欧卡");
        actionMenuButton.setVisibility(View.GONE);
        basePresenter = new OCardAttachPresenter(this);
        return view;
    }

    private void startAttaching() {
        llStep1.setVisibility(View.GONE);
        llStep2.setVisibility(View.GONE);
        llStep3.setVisibility(View.GONE);
        llNoNFC.setVisibility(View.GONE);
        llStep4.setVisibility(View.GONE);
        llStep5.setVisibility(View.GONE);
        llStep6.setVisibility(View.GONE);
        llOcardAttatchFail.setVisibility(View.GONE);
        llOcardAttatchSuccess.setVisibility(View.GONE);
        startImageAnimation(true);
        // TODO: 2017/4/1 页面加载后检查欧卡是否在线——确定绿色在线标志的状态
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //UI布局准备好之后开始流程
        basePresenter.onAttached();
    }

    private void startImageAnimation(boolean b) {
        if (b) {
            ivLoading.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotation);
            animation.setDuration(2000);
            animation.setRepeatCount(Animation.INFINITE);
            animation.setInterpolator(new LinearInterpolator());
            ivLoading.startAnimation(animation);
        } else {
            ivLoading.clearAnimation();
            ivLoading.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        if (handler1 != null) {
            handler1.removeCallbacks(runnable1);
        }
        basePresenter.onViewDestroy();
    }


    @Override
    public void executeAttaching(int i, boolean b) {
        switch (i) {
            case 0:
                startAttaching();
                if (b) {
                    llStep1.setVisibility(View.VISIBLE);
                    llStep1.startLoading();
                    basePresenter.detectNFC();
                } else {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Utils.showLog(TAG,"权限申请开始");
                        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION};
                        requestPermissions(permissions,REQUEST_CODE_PERMISSION);
                        requestPermission = true;
                    } else {
                        boolean bool = testingAppOps();
                        if (bool) {
                            Utils.showLog(TAG, "权限已获取，开始绑卡");
                            executeAttaching(1, false);
                        } else {
                            Utils.customToast(getContext(), "该功能需要使用位置信息，请在设置中打开欧乐应用的定位权限", Toast.LENGTH_SHORT).show();
                            mHandler.sendEmptyMessage(Activity.RESULT_CANCELED);
                        }
                    }
                }
                break;
            case 1:
                isNFCavailable = b;
                if (isNFCavailable) {
                    llStep1.endLoadingOnSuccess();
                }
                ivOcardHeader.setImageResource(R.drawable.ocard_invalid);
                llStep2.setVisibility(View.VISIBLE);
                llStep2.startLoading();
                basePresenter.detectBT();
                break;
            case 2:
                if (b) {
                    llStep2.endLoadingOnSuccess();
                } else {
                    llStep2.endLoadingOnFail(true, null);
                }
                llStep3.setVisibility(View.VISIBLE);
                llStep3.startLoading();
                llStep3.endLoadingOnFail(false, null);
                executeAttaching(3, true);
                break;
            case 3:
                Utils.showLog(TAG, "开始执行第三步");
                if (isNFCavailable) {
                    llStep4.setVisibility(View.VISIBLE);
                    llStep4.startLoading();
                } else {
                    if (b) {
                        llNoNFC.setVisibility(View.VISIBLE);
                        final ArrayList<TextView> textViews = new ArrayList<>();
                        textViews.add((TextView) view.findViewById(R.id.tv0));
                        textViews.add((TextView) view.findViewById(R.id.tv1));
                        textViews.add((TextView) view.findViewById(R.id.tv2));
                        textViews.add((TextView) view.findViewById(R.id.tv3));
                        textViews.add((TextView) view.findViewById(R.id.tv4));
                        textViews.add((TextView) view.findViewById(R.id.tv5));
                        timeRes = 120;
                        Utils.showLog(TAG, "倒计时开始：" + timeRes);
                        textNoNfcHeader.setText(getString(R.string.ocard_attatch_enter_no) + "(" + timeRes + "S)");
                        handler1 = new Handler();
                        runnable1 = new Runnable() {
                            @Override
                            public void run() {
                                if (--timeRes >= 0) {
                                    if (isAdded()) {
                                        Utils.showLog(TAG, "倒计时：" + timeRes);
                                        textNoNfcHeader.setText(getString(R.string.ocard_attatch_enter_no) + "(" + timeRes + "S)");
                                        handler1.postDelayed(runnable1, 1000);
                                    }
                                } else {
                                    handler1.removeCallbacks(runnable1);
                                    handler1 = null;
                                    runnable1 = null;
                                    llNoNFC.setVisibility(View.GONE);
                                    llStep4.setVisibility(View.VISIBLE);
                                    llStep4.setText(getString(R.string.ocard_attatch_enter_no));
                                    llStep4.endLoadingOnFail(true, "超时");
                                    executeAttaching(4, false);
                                }
                            }
                        };
                        handler1.postDelayed(runnable1, 1000);
                        etOcardNumber.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                String string = s.toString();
                                Utils.showLog(TAG, "输入：" + string);
                                for (int i1 = 0; i1 < 6; i1++) {
                                    if (i1 <= string.length() - 1) {
                                        String text = "" + string.charAt(i1);
                                        textViews.get(i1).setText(text);
                                    } else {
                                        textViews.get(i1).setText("");
                                    }
                                }
                            }
                        });
                        btnOcardNumber.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String trim = etOcardNumber.getText().toString().trim();
                                if (TextUtils.isEmpty(trim)) {
                                    Utils.customToast(getContext(), "欧卡卡号不得为空", Toast.LENGTH_SHORT).show();
                                    return;
                                } else if (trim.length() < 6) {
                                    Utils.customToast(getContext(), "请输入欧卡卡号末六位", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    manualTextOCardNum = trim.toUpperCase();
                                    etOcardNumber.setText("");
                                    handler1.removeCallbacks(runnable1);
                                    handler1 = null;
                                    runnable1 = null;
                                    actionBack.setImageResource(R.drawable.background_000);
                                    actionBackLayout.setOnClickListener(null);
                                    llNoNFC.setVisibility(View.GONE);
                                    llStep4.setVisibility(View.VISIBLE);
                                    llStep4.setText(getString(R.string.ocard_attatch_enter_no));
                                    llStep4.startLoading();
                                    llStep5.setVisibility(View.VISIBLE);
                                    llStep5.setText(getString(R.string.ocard_no) + manualTextOCardNum);
                                    llStep6.setVisibility(View.VISIBLE);
                                    llStep6.setText("4.请在蓝灯连续闪烁时，按下欧卡电源键");
                                    basePresenter.detectOCard(manualTextOCardNum);
                                }
                            }
                        });
                    } else {
                        llStep3.endLoadingOnFail(false, null);
                        executeAttaching(5, false);
                    }
                }
                break;
            case 4:
                if (isNFCavailable) {
                    llStep4.endLoadingOnSuccess();
                    llStep5.setVisibility(View.VISIBLE);
                    llStep5.startLoading();
                    basePresenter.matchOCardNo(okCardID);
                    actionBack.setImageResource(R.drawable.background_000);
                    actionBackLayout.setOnClickListener(null);
                } else {
                    if (b) {
                        Utils.showLog(TAG, "扫描到欧卡蓝牙地址，开始绑卡");
                        basePresenter.matchOCardNo(manualTextOCardNum);
                    } else {
                        executeAttaching(5, false);
                    }
                }
                break;
            case 5:
                MainActivity.inOCardBinding = false;
                actionBack.setImageResource(R.drawable.ic_toolbar_back);
                actionBackLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.removeSecondFragment();
                    }
                });
                if (isNFCavailable) {
                    llStep5.endLoadingOnSuccess();
                    llStep6.setVisibility(View.VISIBLE);
                    executeAttaching(6, true);
                } else {
                    if (llStep6.getVisibility() == View.VISIBLE) {
                        llStep6.setVisibility(View.GONE);
                    }
                    Utils.showLog(TAG, "绑卡结果返回："+UserRepository.getInstance(getContext()).getUser());
                    if (!TextUtils.isEmpty(manualTextOCardNum)) {
                        llStep4.endLoadingOnFail(false, null);
                    }
                    startImageAnimation(false);
                    if (b) {
                        BaseActivity.setOcardState(BaseActivity.OCARD_STATE_BOND);
                        llStep3.endLoadingOnSuccess();
                        llStep5.endLoadingOnSuccess();
                        ivOcardHeader.setImageResource(R.drawable.ocard_online);
                        String text = "我的欧卡：" + activity.sharedPreferences.getString(UserFragment.DEVICE_NO, manualTextOCardNum);
                        tvOcardAttatchSuccessNo.setText(text);
                        if (isApplyNew) {
                            applyNew(manualTextOCardNum);
                            return;
                        } else {
                            gotoOCardFragment();
                            llOcardAttatchSuccess.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (TextUtils.isEmpty(manualTextOCardNum)) {
                            llStep5.setVisibility(View.GONE);
                        }
                        llOcardAttatchFail.setVisibility(View.VISIBLE);
                        btnReAttac.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                llOcardAttatchFail.setVisibility(View.GONE);
                                basePresenter.onAttached();
                            }
                        });
                    }
                }
                break;
            case 6:
                llStep6.endLoadingOnSuccess();
                /*结束动画效果*/
                startImageAnimation(false);
                if (b) {
                    ivOcardHeader.setImageResource(R.drawable.ocard_online);
                    if (isApplyNew) {
                        applyNew(okCardID);
                        return;
                    } else {
                        gotoOCardFragment();
                        llOcardAttatchSuccess.setVisibility(View.VISIBLE);
                    }

                } else {
                    llOcardAttatchFail.setVisibility(View.VISIBLE);
                    llStep4.endLoadingOnSuccess();
                    btnReAttac.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            basePresenter.onAttached();
                        }
                    });
                }
                break;
        }
    }

    private boolean testingAppOps() {
        AppOpsManager appOpsManagerCompat = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            int checkOp = appOpsManagerCompat.checkOp(AppOpsManager.OPSTR_COARSE_LOCATION, Binder.getCallingUid(), activity.getPackageName());
            if (checkOp == AppOpsManager.MODE_IGNORED) {
                return false;
            }
        }
        return true;
    }

    private void gotoOCardFragment() {
        final LoadingDialog loadingDialog = LoadingDialog.getInstance();
        loadingDialog.setLoadingDialogListener(new LoadingDialog.LoadingDialogListener() {
            @Override
            public void onDismiss() {
                if (subscription != null && !subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }
            }

            @Override
            public void onShow() {
                User user = UserRepository.getInstance(getContext()).getUser();
                subscription = UserRepository.getInstance(getContext()).requestOCardConnection(getContext(), user.getBhtAddress(), 10 * 1000)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultSubscribe<DeviceInfo>(TAG) {
                            @Override
                            public void onError(Throwable throwable) {
                                super.onError(throwable);
                                loadingDialog.onFinished(null, 0);
                                Utils.customToast(getContext(), getString(R.string.bluetooth_connexion_failed), Toast.LENGTH_SHORT).show();
                                activity.removeSecondFragment();
                            }

                            @Override
                            public void onNext(final DeviceInfo deviceInfo) {
                                super.onNext(deviceInfo);
                                Utils.showLog(TAG, "欧卡蓝牙连接状态：" + deviceInfo);
                                if (deviceInfo != null) {
                                    SharedPreferences.Editor editor = activity.sharedPreferences.edit();
                                    editor.putString(UserFragment.AVAILABLE_VOLUME, "93KB");
                                    editor.putString(UserFragment.TOTAL_VOLUME, "180KB");
                                    editor.putString(UserFragment.BATTERY_VOLUME, deviceInfo.getDumpEnergy() + "%");
                                    editor.putString(UserFragment.VERSION_INFO, deviceInfo.getDeviceCosVersion());
                                    editor.apply();
                                }
                                loadingDialog.onFinished(null, 0);
                                runnable = new Runnable() {
                                    public void run() {
                                        activity.removeSecondFragment();
                                        activity.addSecondFragment(OcardFragment.newInstance());
                                    }
                                };
                                handler = new Handler();
                                handler.postDelayed(runnable, 4000);
                            }
                        });
            }
        });
        loadingDialog.show(getFragmentManager(), LoadingDialog.class.getName());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Utils.showLog(TAG, "requestCode = [" + requestCode + "], permissions = [" + permissions + "], grantResults = [" + grantResults + "]");
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.showLog(TAG, "权限已通过，开始绑卡");
                executeAttaching(1, false);
            } else {
                Utils.customToast(getContext(), getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessage(Activity.RESULT_CANCELED);
            }
        }
    }

    private void applyNew(final String okCardID) {
        final LoadingDialog loadingDialog = LoadingDialog.getInstance();
        loadingDialog.setLoadingDialogListener(new LoadingDialog.LoadingDialogListener() {
            @Override
            public void onDismiss() {
                activity.removeSecondFragment();
            }

            @Override
            public void onShow() {
                UserRepository.getInstance(getContext()).makeupOCard(User.DEV_OCARD, okCardID,
                        activity.sharedPreferences.getString(UserFragment.BLUETOOTH_NAME, null),
                        activity.sharedPreferences.getString(UserFragment.BLUETOOTH_ADDRESS, null))
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultSubscribe<OKCard>(TAG) {
                            @Override
                            public void onError(Throwable throwable) {
                                super.onError(throwable);
                                executeAttaching(6, false);
                                loadingDialog.onFinished(null, 0);
                                Utils.customToast(getContext(), "补新卡出错：" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNext(OKCard okCard) {
                                super.onNext(okCard);
                                saveCardInfo(okCard);
                                loadingDialog.onFinished("操作成功", 2000);
                            }
                        });
            }
        });
        loadingDialog.show(getFragmentManager(), LoadingDialog.class.getName());
    }

    @Override
    public void enableNFC() {
        activity.startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        enable_nfc_intent = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.showLog(TAG, "OCardAttachFragment.onResume");
        if (enable_nfc_intent) {
            executeAttaching(0, true);
        }
//        if (requestPermission) {
//            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSION);
//            } else {
//                Utils.showLog(TAG, "权限获取成功，开始绑卡");
//                executeAttaching(1, false);
//            }
//        }
    }

    private static final String TAG = "OCardAttachFragment";

    @Override
    public void showDialog(String message, String subMessage, boolean cancelEnabled, CustomDialog.DialogClickListener listener) {
        new CustomDialog(getContext(), null, message, subMessage, cancelEnabled ? getString(R.string.cancel) : null, getString(R.string.confirm), listener, CustomDialog.MODE_OKLINE2).show();
    }

    @Override
    public void saveCardInfo(OKCard okCard) {
        activity.okCard = okCard;
        Utils.showLog(TAG, "绑定欧卡之后存储欧卡信息");
        SharedPreferences.Editor editor = activity.sharedPreferences.edit();
        editor.putString(UserFragment.DEVICE_NO, okCard.getDeviceNo()).apply();
        editor.putString(UserFragment.BIND_DATE, okCard.getBindDate()).apply();
        editor.putString(UserFragment.BLUETOOTH_ADDRESS, okCard.getBhtAddress()).apply();
        editor.putString(UserFragment.BLUETOOTH_NAME, okCard.getBhtName()).apply();
    }

    public static Fragment instance(boolean b) {
        OCardAttachFragment oCardAttachFragment = new OCardAttachFragment();
        oCardAttachFragment.isApplyNew = b;
        return oCardAttachFragment;
    }
}
