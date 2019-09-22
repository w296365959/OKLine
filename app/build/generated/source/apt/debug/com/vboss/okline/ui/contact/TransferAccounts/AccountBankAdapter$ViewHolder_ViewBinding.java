// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.TransferAccounts;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AccountBankAdapter$ViewHolder_ViewBinding<T extends AccountBankAdapter.ViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public AccountBankAdapter$ViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.account_bank_name = Utils.findRequiredViewAsType(source, R.id.account_bank_name, "field 'account_bank_name'", TextView.class);
    target.account_bank_img = Utils.findRequiredViewAsType(source, R.id.account_bank_img, "field 'account_bank_img'", SimpleDraweeView.class);
    target.select_bank_catalog = Utils.findRequiredViewAsType(source, R.id.select_bank_catalog, "field 'select_bank_catalog'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.account_bank_name = null;
    target.account_bank_img = null;
    target.select_bank_catalog = null;

    this.target = null;
  }
}
