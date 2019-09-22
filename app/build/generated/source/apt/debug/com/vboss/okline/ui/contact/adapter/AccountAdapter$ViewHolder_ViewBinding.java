// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AccountAdapter$ViewHolder_ViewBinding<T extends AccountAdapter.ViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public AccountAdapter$ViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.tvBankName = Utils.findRequiredViewAsType(source, R.id.tv_bank_name, "field 'tvBankName'", TextView.class);
    target.tvBankCount = Utils.findRequiredViewAsType(source, R.id.tv_bank_count, "field 'tvBankCount'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvBankName = null;
    target.tvBankCount = null;

    this.target = null;
  }
}
