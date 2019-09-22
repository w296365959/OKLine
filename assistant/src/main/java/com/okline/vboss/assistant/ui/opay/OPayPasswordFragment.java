package com.okline.vboss.assistant.ui.opay;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.okline.ocp.CustomDebit;
import com.okline.olint.CheckPayPwd;
import com.okline.vboss.assistant.R;
import com.okline.vboss.assistant.R2;
import com.okline.vboss.assistant.base.Config;
import com.okline.vboss.assistant.base.DefaultSubscribe;
import com.okline.vboss.assistant.ui.recharge.Utils;
import com.okline.vboss.http.EncryptUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.app.Activity.RESULT_CANCELED;

/**
 * OKLine(Hangzhou) Co.,ltd.
 * Author:Zheng Jun
 * Email:zhengjun@okline.cn
 * Date: 2016-6-16 11:28:11
 * Desc:
 */
public class OPayPasswordFragment extends Fragment implements View.OnClickListener {

    public static final String P_ID = "olApp";
    public static final String P_CODE = "OKLine";
    public static final String RETURN_TO_INITIAL_PAGE = "return_to_initial_page";
    @BindView(R2.id.iv_icon)
    ImageView ivIcon;
    @BindView(R2.id.tv_paypophint)
    TextView tvPaypophint;
    @BindView(R2.id.tv_opaypwdcancel)
    TextView tvOpaypwdcancel;
    @BindView(R2.id.psw_dot1)
    ImageView pswDot1;
    @BindView(R2.id.psw_dot2)
    ImageView pswDot2;
    @BindView(R2.id.psw_dot3)
    ImageView pswDot3;
    @BindView(R2.id.psw_dot4)
    ImageView pswDot4;
    @BindView(R2.id.psw_dot5)
    ImageView pswDot5;
    @BindView(R2.id.psw_dot6)
    ImageView pswDot6;
    @BindView(R2.id.ll_pwdface)
    LinearLayout llPwdface;
    @BindView(R2.id.tv_forgetpwd)
    TextView tvForgetpwd;
    @BindView(R2.id.btn_pwdkey1)
    TextView btnPwdkey1;
    @BindView(R2.id.btn_pwdkey2)
    TextView btnPwdkey2;
    @BindView(R2.id.btn_pwdkey3)
    TextView btnPwdkey3;
    @BindView(R2.id.btn_pwdkey4)
    TextView btnPwdkey4;
    @BindView(R2.id.btn_pwdkey5)
    TextView btnPwdkey5;
    @BindView(R2.id.btn_pwdkey6)
    TextView btnPwdkey6;
    @BindView(R2.id.btn_pwdkey7)
    TextView btnPwdkey7;
    @BindView(R2.id.btn_pwdkey8)
    TextView btnPwdkey8;
    @BindView(R2.id.btn_pwdkey9)
    TextView btnPwdkey9;
    @BindView(R2.id.btn_pwdkeyblank)
    TextView btnPwdkeyblank;
    @BindView(R2.id.btn_pwdkey0)
    TextView btnPwdkey0;
    @BindView(R2.id.btn_pwdkeydel)
    RelativeLayout btnPwdkeydel;
    @BindView(R2.id.gv_keyboard)
    GridLayout gvKeyboard;
    String bankDebitResult;
    private StringBuffer pwd;
    private View view;
    private ArrayList<ImageView> list;
    private OPaySDKActivity activity;
    private String checkPayPwd;
    private String pwdtext;
    private boolean isPwdCheckRunning;
    private String paymentMsg;
    private String paymentCode;
    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (OPaySDKActivity) getActivity();
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_opaysdk3_assistant, container, false);
            unbinder = ButterKnife.bind(this, view);

            pwd = new StringBuffer(6);
            list = new ArrayList<>(6);
            list.add(pswDot1);
            list.add(pswDot2);
            list.add(pswDot3);
            list.add(pswDot4);
            list.add(pswDot5);
            list.add(pswDot6);
            btnPwdkey0.setOnClickListener(this);
            btnPwdkey1.setOnClickListener(this);
            btnPwdkey2.setOnClickListener(this);
            btnPwdkey3.setOnClickListener(this);
            btnPwdkey4.setOnClickListener(this);
            btnPwdkey5.setOnClickListener(this);
            btnPwdkey6.setOnClickListener(this);
            btnPwdkey7.setOnClickListener(this);
            btnPwdkey8.setOnClickListener(this);
            btnPwdkey9.setOnClickListener(this);
            btnPwdkeydel.setOnClickListener(this);
            tvForgetpwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent("com.okline.vboss.oule.Password_Reset_Request");
//                    intent.putExtra(ForgetPasswordActivity.PASSWORD_TYPE, UserManager.PASSWORD_TYPE_OPAY);
//                    intent.putExtra(AutoCountDownTextView.INTENT_EXTRA_NAME, TAG);
//                    startActivity(intent);
//                    Utils.customToast(getContext(), "暂时不可用", Toast.LENGTH_SHORT).show();
                }
            });
            tvOpaypwdcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //R010014	取消支付时，只按一次就取消
                    Intent intent = new Intent();
                    intent.putExtra(RETURN_TO_INITIAL_PAGE,true);
                    activity.setResult(RESULT_CANCELED,intent);
                    activity.finish();
                }
            });
            if (tvPaypophint!=null) {
                tvPaypophint.setText("请输入"+activity.selectedCard.getCardName()+"密码");
            }
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && tvPaypophint!=null) {
            System.out.println("OPayPasswordFragment.setUserVisibleHint");
            tvPaypophint.setText("请输入"+activity.selectedCard.getCardName()+"密码");
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_pwdkeydel) {
            if (pwd.length() > 0) {
                pwd.deleteCharAt(pwd.length() - 1);
                setDotVisibility(list, pwd.length());
            }
        } else if (i == R.id.btn_pwdkey0) {
            pwd.append(0);
            checkPwd();

        } else if (i == R.id.btn_pwdkey1) {
            pwd.append(1);
            checkPwd();

        } else if (i == R.id.btn_pwdkey2) {
            pwd.append(2);
            checkPwd();

        } else if (i == R.id.btn_pwdkey3) {
            pwd.append(3);
            checkPwd();

        } else if (i == R.id.btn_pwdkey4) {
            pwd.append(4);
            checkPwd();

        } else if (i == R.id.btn_pwdkey5) {
            pwd.append(5);
            checkPwd();

        } else if (i == R.id.btn_pwdkey6) {
            pwd.append(6);
            checkPwd();

        } else if (i == R.id.btn_pwdkey7) {
            pwd.append(7);
            checkPwd();

        } else if (i == R.id.btn_pwdkey8) {
            pwd.append(8);
            checkPwd();

        } else if (i == R.id.btn_pwdkey9) {
            pwd.append(9);
            checkPwd();

        }
    }

    private static final String TAG = "OPayPasswordFragment";

    private void checkPwd() {
        if (pwd.length() >= 6) {
            //将数字键盘禁用
            setKeyboardClickable(false);
        }
        pwdtext = pwd.length() > 6 ? pwd.substring(0, 6) : pwd.toString();
        setDotVisibility(list, pwdtext.length());
        if (pwdtext.length() == 6) {

            if (!isPwdCheckRunning) {
                isPwdCheckRunning = true;
                if (!Utils.isNetWorkConnected(getContext())) {
                    Utils.showLog(TAG, "没有网络");
                    new CustomDialog(getContext(), null, "网络不可用", null, null, getString(R.string.confirm), new CustomDialog.DialogClickListener() {
                        @Override
                        public void onNegtiveClick() {

                        }

                        @Override
                        public void onPositiveClick() {
                            //重新启用数字键盘
                            setKeyboardClickable(true);
                            pwd.setLength(0);
                            setDotVisibility(list, 0);

                            isPwdCheckRunning = false;
                        }
                    }, 0).show();
                    return;
                }
            /*开始检查密码*/
                Observable.create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        try {
                            checkPayPwd = new CheckPayPwd().checkPayPwd(Config.OL_NUM, EncryptUtils.password(pwdtext), "olApp", "OKLine");
                            if (!TextUtils.isEmpty(checkPayPwd)) {
                                subscriber.onNext(true);
                                subscriber.onCompleted();
                            } else {
                                subscriber.onError(new IllegalStateException("网络异常"));
                                subscriber.onCompleted();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                            subscriber.onCompleted();
                        }
                    }
                })
                        .timeout(5, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultSubscribe<Boolean>(TAG) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        isPwdCheckRunning = false;
                                    }
                                }, 100);
                                String message = e.getMessage();
                                Utils.showLog(TAG, "密码验证出错：" + message);
                                new CustomDialog(getContext(), null, !TextUtils.isEmpty(message)?message:"密码验证超时，请稍后重试", null,null, getString(R.string.confirm), new CustomDialog.DialogClickListener() {
                                    @Override
                                    public void onNegtiveClick() {

                                    }

                                    @Override
                                    public void onPositiveClick() {
                                        activity.setResult(Activity.RESULT_CANCELED);
                                        activity.finish();
                                    }
                                }, 0).show();
                            }

                            @Override
                            public void onNext(Boolean aBoolean) {
                                super.onNext(aBoolean);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        isPwdCheckRunning = false;
                                    }
                                }, 500);
                                if (aBoolean) {
                                    Utils.showLog(TAG, "密码比对信息返回！！");
                                    onPwdCheckResult();
                                }
                            }
                        });
            }
        }
    }

    private void setKeyboardClickable(boolean b) {
        if (gvKeyboard != null) {
            gvKeyboard.setClickable(b);
        }
    }

    private void onPwdCheckResult() {
        try {
            JSONObject jsonObject = new JSONObject(checkPayPwd);
            String resultCode = jsonObject.getString("resultCode");
            String resultMsg = jsonObject.getString("resultMsg");
            switch (Integer.parseInt(resultCode)) {
                case 0:
                    //重新启用数字键盘
                    setKeyboardClickable(true);
                    pwd.setLength(0);
                    setDotVisibility(list, 0);
                    
                    
                    /*弹窗开始发起支付*/
                    new PaymentRunningDialog(activity, new PaymentRunningDialogListener() {
                        @Override
                        public void startPayment(final PaymentResutListener listener) {
                            Observable.create(new Observable.OnSubscribe<Boolean>() {
                                @Override
                                public void call(Subscriber<? super Boolean> subscriber) {
                                    try {
                                        Timber.tag(TAG).i("开始执行付款动作");
                                        String olNo = Config.OL_NUM;
                                        Timber.tag(TAG).i("olNo: " + olNo);
                                        Timber.tag(TAG).i("orderNumber:" + activity.orderNumber);
                                        Timber.tag(TAG).i("tn:" + activity.orderTn);
                                        Timber.tag(TAG).i("bindId:" + activity.selectedCard.getBindId());
                                        Timber.tag(TAG).i("orderAmount:" + activity.orderAmount);
                                        Timber.tag(TAG).i("merNo:" + activity.merNo);
                                        Timber.tag(TAG).i("orderDesc:" + activity.orderDesc);
                                        bankDebitResult = new CustomDebit().bankDebit(olNo, activity.orderNumber, activity.orderTn, activity.selectedCard.getBindId(), activity.orderAmount, activity.merNo, activity.orderDesc, P_ID, P_CODE);
                                        JSONObject jsonObject = new JSONObject(bankDebitResult);
                                        paymentCode = jsonObject.getString("resultCode");
                                        paymentMsg = jsonObject.getString("resultMsg");
                                        if ("0".equals(paymentCode)) {
                                            subscriber.onNext(true);
                                            subscriber.onCompleted();
                                        } else {
                                            Utils.showLog(TAG, "paymentCode不为零！");
                                            subscriber.onError(new Exception("paymentCode不为零！"));
                                            subscriber.onCompleted();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        subscriber.onError(e);
                                        subscriber.onCompleted();
                                    }
                                }
                            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).timeout(30, TimeUnit.SECONDS).subscribe(new DefaultSubscribe<Boolean>(TAG) {
                                @Override
                                public void onCompleted() {
                                    super.onCompleted();
                                    Utils.unsubscribeRxJava(this);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    Utils.showLog(TAG, "支付出错：" + paymentMsg);
                                    activity.paymentNote = paymentMsg;
                                    listener.onPaymentFinished(false, null);
                                }

                                @Override
                                public void onNext(Boolean aBoolean) {
                                    super.onNext(aBoolean);
                                    //payment successful,show messages
                                    final String paymentSuccessfulTime = DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()).toString();
                                    activity.bankDebitInfo = new BankDebitInfo(
                                            activity.orderAmount,
                                            activity.cardMainType,
                                            activity.cardId,
                                            activity.selectedCard.getCardName(),
                                            activity.selectedCard.getCardNo(),
                                            activity.orderNumber,
                                            activity.merNo,
                                            activity.orderTn,
                                            activity.orderDesc, true, false);
                                    activity.bankDebitInfo.setOrderDate(paymentSuccessfulTime);

                                    activity.setResult(Activity.RESULT_OK);
                                    listener.onPaymentFinished(true, activity.bankDebitInfo);
                                }
                            });
                        }
                    }).show();
                    break;
                case 5409:
                    //验证结果:密码错误,要求重新输入
                    Utils.customToast(getContext(), resultMsg, Toast.LENGTH_SHORT).show();
//                    Utils.customToast(getContext(), getString(R.string.mysecurity_pwd_mismatch_warning), Toast.LENGTH_SHORT).show();
                    //重新启用数字键盘
                    setKeyboardClickable(true);
                    pwd.setLength(0);
                    setDotVisibility(list, 0);
                    break;
                case 5503:
                    //验证结果:已经连续输错五次,限制状态ing,弹窗提示剩余分钟数
                    // 此处客户连续输错五次已经被限制访问,点击确认之后的操作是自动退出?
                    new CustomDialog(getContext(), null, resultMsg, null, getString(R.string.cancel), getString(R.string.confirm), new CustomDialog.DialogClickListener() {
                        @Override
                        public void onNegtiveClick() {
                            //重新启用数字键盘
                            setKeyboardClickable(true);
                            pwd.setLength(0);
                            setDotVisibility(list, 0);
                        }

                        @Override
                        public void onPositiveClick() {
                            //重新启用数字键盘
                            setKeyboardClickable(true);
                            pwd.setLength(0);
                            setDotVisibility(list, 0);
                            Utils.customToast(getContext(), "密码设置模块建设中", Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent("com.okline.vboss.oule.Password_Reset_Request");
//                                                intent.putExtra(ForgetPasswordActivity.PASSWORD_TYPE,UserManager.PASSWORD_TYPE_OPAY);
//                                                intent.putExtra(AutoCountDownTextView.INTENT_EXTRA_NAME,TAG);
//                                                startActivity(intent);
                        }
                    }, 0).show();
                    break;
                default:
                    //验证结果:密码错误,要求重新输入
                    Utils.customToast(getContext(), resultMsg, Toast.LENGTH_SHORT).show();
//                    Utils.customToast(getContext(), getString(R.string.mysecurity_pwd_mismatch_warning), Toast.LENGTH_SHORT).show();
                    //重新启用数字键盘
                    setKeyboardClickable(true);
                    pwd.setLength(0);
                    setDotVisibility(list, 0);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setDotVisibility(ArrayList<ImageView> list, int length) {
        for (int i = 0; i < length; i++) {
            list.get(i).setVisibility(View.VISIBLE);
        }
        for (int i = length; i < list.size(); i++) {
            list.get(i).setVisibility(View.INVISIBLE);
        }
    }
}
