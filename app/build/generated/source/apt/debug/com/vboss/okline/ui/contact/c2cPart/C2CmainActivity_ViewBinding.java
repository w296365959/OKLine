// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.c2cPart;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class C2CmainActivity_ViewBinding<T extends C2CmainActivity> implements Unbinder {
  protected T target;

  @UiThread
  public C2CmainActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.etInputTransferAmount = Utils.findRequiredViewAsType(source, R.id.et_input_transfer_amount, "field 'etInputTransferAmount'", EditText.class);
    target.tvConfirmTransfer = Utils.findRequiredViewAsType(source, R.id.tv_confirm_transfer, "field 'tvConfirmTransfer'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.etInputTransferAmount = null;
    target.tvConfirmTransfer = null;

    this.target = null;
  }
}
