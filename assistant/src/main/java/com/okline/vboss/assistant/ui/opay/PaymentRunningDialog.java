package com.okline.vboss.assistant.ui.opay;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.okline.vboss.assistant.R;


public class PaymentRunningDialog extends Dialog {
    public static final String FAIL_MESSAGE = "failMessage";
    private Context context;
    private PaymentRunningDialogListener listener;
    private TextView tvPaymentState;
    private OPaySDKActivity activity;
    private Handler handler;
    private Runnable runnable;
    private ImageView ivLoading;

    PaymentRunningDialog(Context context, PaymentRunningDialogListener listener) {
        super(context,R.style.dialog);
        /*外接参数、接口的传入*/
        this.context = context;
        this.listener = listener;
        activity = (OPaySDKActivity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*构造view*/
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialog_payment_running_assistant, null);
        setContentView(view);
        tvPaymentState = (TextView) view.findViewById(R.id.tv_payment_state);
        ivLoading = (ImageView) view.findViewById(R.id.iv_loading);
        startImageAnimation(true);
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });


        /*外围点击无法触发触发消失*/
        setCanceledOnTouchOutside(false);

        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setAttributes(lp);
        }

        if (listener != null) {
            listener.startPayment(new PaymentResutListener() {
                @Override
                public void onPaymentFinished(final boolean successful, final BankDebitInfo info) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            PaymentRunningDialog.this.onPaymentFinished(successful, info);
                        }
                    }, 3500);
                }
            });
        }
    }

    private void startImageAnimation(boolean b) {
        if (b) {
            ivLoading.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotation_clockwise);
            animation.setDuration(2000);
            animation.setRepeatCount(Animation.INFINITE);
            animation.setInterpolator(new LinearInterpolator());
            ivLoading.startAnimation(animation);
        } else {
            ivLoading.clearAnimation();
        }
    }

    private void onPaymentFinished(final boolean isPaymentSuccessful, final BankDebitInfo info) {
        tvPaymentState.setText(isPaymentSuccessful?"支付成功":"支付异常");
        startImageAnimation(false);
        ivLoading.setImageResource(R.mipmap.bingo);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (activity.cardSelected || !activity.showResultPage) {
                    Intent intent = new Intent();
                    if (isPaymentSuccessful) {
                        intent.putExtra(OPaySDKActivity.PAYMENT_ARGUMENT_TRIALNUMBER, activity.orderTn);
                        intent.putExtra(BankDebitInfo.BANK_DEBIT_INFO, info);
                        activity.setResult(Activity.RESULT_OK, intent);
                    } else {
                        intent.putExtra(FAIL_MESSAGE, activity.paymentNote);
                        activity.setResult(Activity.RESULT_CANCELED, intent);
                    }
                    activity.finish();
                    dismiss();
                } else {
                    Intent intent = new Intent(context, PaymentSuccessfulActivity.class);
                    intent.putExtra(BankDebitInfo.BANK_DEBIT_INFO, info);
                    activity.startActivityForResult(intent, 251);
                    dismiss();
                }
            }
        };
        handler.postDelayed(runnable,2000);
    }

    @Override
    public void dismiss() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        super.dismiss();
    }
}
