// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.TransferAccounts;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.view.widget.PaymentLauchLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TransPaymentActivity_ViewBinding<T extends TransPaymentActivity> implements Unbinder {
  protected T target;

  private View view2131756240;

  @UiThread
  public TransPaymentActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.fragment_toolbar, "field 'toolbar'", FragmentToolbar.class);
    target.selected_income_Account = Utils.findRequiredViewAsType(source, R.id.selected_income_Account, "field 'selected_income_Account'", RelativeLayout.class);
    target.incomeAccount_cardicon = Utils.findRequiredViewAsType(source, R.id.incomeAccount_cardicon, "field 'incomeAccount_cardicon'", SimpleDraweeView.class);
    target.incomeAccount_cardName = Utils.findRequiredViewAsType(source, R.id.incomeAccount_cardName, "field 'incomeAccount_cardName'", TextView.class);
    target.incomeAccount_name = Utils.findRequiredViewAsType(source, R.id.incomeAccount_name, "field 'incomeAccount_name'", TextView.class);
    target.incomeAccount_cardNo = Utils.findRequiredViewAsType(source, R.id.incomeAccount_cardNo, "field 'incomeAccount_cardNo'", TextView.class);
    target.trans_edit_money = Utils.findRequiredViewAsType(source, R.id.trans_edit_money, "field 'trans_edit_money'", EditText.class);
    target.trans_to_pay_button = Utils.findRequiredViewAsType(source, R.id.trans_to_pay_button, "field 'trans_to_pay_button'", TextView.class);
    target.rmb = Utils.findRequiredViewAsType(source, R.id.rmb, "field 'rmb'", TextView.class);
    target.paymentLauchLayout = Utils.findRequiredViewAsType(source, R.id.transfer_paymentLauch, "field 'paymentLauchLayout'", PaymentLauchLayout.class);
    view = Utils.findRequiredView(source, R.id.select_incomeAccount, "method 'ViewOnClick'");
    view2131756240 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ViewOnClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.selected_income_Account = null;
    target.incomeAccount_cardicon = null;
    target.incomeAccount_cardName = null;
    target.incomeAccount_name = null;
    target.incomeAccount_cardNo = null;
    target.trans_edit_money = null;
    target.trans_to_pay_button = null;
    target.rmb = null;
    target.paymentLauchLayout = null;

    view2131756240.setOnClickListener(null);
    view2131756240 = null;

    this.target = null;
  }
}
