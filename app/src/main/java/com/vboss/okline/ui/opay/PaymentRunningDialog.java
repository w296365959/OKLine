package com.vboss.okline.ui.opay;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.vboss.okline.R;

public class PaymentRunningDialog extends Dialog {
    private static final int TIME_GAP = 250;
    public static final String FAIL_MESSAGE = "failMessage";
    private View footerPlaceholder;
    private Context context;
    private PaymentRunningDialogListener listener;
    private View headerPlaceholder;
    private TextView tvPaymentState;
    private ImageView ivPaymentLoading;
    private Handler handler;
    private Runnable runnable;
    private int loadingState;
    private OPaySDKActivity activity;

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
        View view = layoutInflater.inflate(R.layout.dialog_payment_running, null);
        setContentView(view);
        footerPlaceholder = view.findViewById(R.id.footer_placeholder);
        headerPlaceholder = view.findViewById(R.id.header_placeholder);
        tvPaymentState = (TextView) view.findViewById(R.id.tv_payment_state);
        ivPaymentLoading = (ImageView) view.findViewById(R.id.iv_payment_loading);

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                switchLoadingView();
                handler.postDelayed(this, TIME_GAP);
            }
        };
        handler.postDelayed(runnable, TIME_GAP);

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

    private void onPaymentFinished(final boolean isPaymentSuccessful, final BankDebitInfo info) {
        headerPlaceholder.setVisibility(View.VISIBLE);
        if (handler != null) {
            handler.removeCallbacks(runnable);
            ivPaymentLoading.setVisibility(View.GONE);
        }
        footerPlaceholder.setVisibility(View.VISIBLE);
        if (isPaymentSuccessful) {
            tvPaymentState.setText("支付成功");
        } else {
            tvPaymentState.setText("支付异常");
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (activity.cardSelected || !activity.showResultPage) {
                    Intent intent = new Intent();
                    if (isPaymentSuccessful) {
                        intent.putExtra(OPaySDKActivity.PAYMENT_ARGUMENT_TRIALNUMBER,activity.orderTn);
                        intent.putExtra(BankDebitInfo.BANK_DEBIT_INFO,info);
                        activity.setResult(Activity.RESULT_OK,intent);
                    } else {
                        intent.putExtra(FAIL_MESSAGE,activity.paymentNote);
                        activity.setResult(Activity.RESULT_CANCELED,intent);
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
        },2000);
    }

    private void switchLoadingView() {
        switch (loadingState) {
            case 0:
                ivPaymentLoading.setImageResource(R.drawable.loading002);
                loadingState = 1;
                break;
            case 1:
                ivPaymentLoading.setImageResource(R.drawable.loading003);
                loadingState = 2;
                break;
            case 2:
                ivPaymentLoading.setImageResource(R.drawable.loading001);
                loadingState = 0;
                break;
        }
    }
}
