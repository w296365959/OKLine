// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.opay;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PaymentSuccessfulActivity_ViewBinding<T extends PaymentSuccessfulActivity> implements Unbinder {
  protected T target;

  @UiThread
  public PaymentSuccessfulActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.anchor = Utils.findRequiredView(source, R.id.anchor, "field 'anchor'");
    target.llSettingsReturn = Utils.findRequiredViewAsType(source, R.id.ll_settings_return, "field 'llSettingsReturn'", ImageView.class);
    target.tvHeaderTitle = Utils.findRequiredViewAsType(source, R.id.tv_header_title, "field 'tvHeaderTitle'", TextView.class);
    target.tvHeaderSave = Utils.findRequiredViewAsType(source, R.id.tv_header_save, "field 'tvHeaderSave'", TextView.class);
    target.ivOlLogo = Utils.findRequiredViewAsType(source, R.id.iv_ol_logo, "field 'ivOlLogo'", ImageView.class);
    target.tvOlPaymentSuccessful = Utils.findRequiredViewAsType(source, R.id.tv_ol_payment_successful, "field 'tvOlPaymentSuccessful'", TextView.class);
    target.lvPaymentinfo = Utils.findRequiredViewAsType(source, R.id.lv_paymentinfo, "field 'lvPaymentinfo'", ListView.class);
    target.tvOlPaymentSuccessfulAmout = Utils.findRequiredViewAsType(source, R.id.tv_ol_payment_successful_amout, "field 'tvOlPaymentSuccessfulAmout'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.anchor = null;
    target.llSettingsReturn = null;
    target.tvHeaderTitle = null;
    target.tvHeaderSave = null;
    target.ivOlLogo = null;
    target.tvOlPaymentSuccessful = null;
    target.lvPaymentinfo = null;
    target.tvOlPaymentSuccessfulAmout = null;

    this.target = null;
  }
}
