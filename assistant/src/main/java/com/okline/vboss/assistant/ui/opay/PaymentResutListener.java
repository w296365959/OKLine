package com.okline.vboss.assistant.ui.opay;

/**
 * Created by zhengjun on 16/12/22.
 */
public interface PaymentResutListener {
    void onPaymentFinished(boolean successful, BankDebitInfo info);
}
