// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.myCard;

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

public class ChooseBankFragment$BankAdapter$ViewHolder_ViewBinding<T extends ChooseBankFragment.BankAdapter.ViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public ChooseBankFragment$BankAdapter$ViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.tvCatalog = Utils.findRequiredViewAsType(source, R.id.tv_catalog, "field 'tvCatalog'", TextView.class);
    target.tvBankName = Utils.findRequiredViewAsType(source, R.id.tv_bank_name, "field 'tvBankName'", TextView.class);
    target.ivBankIcon = Utils.findRequiredViewAsType(source, R.id.iv_bank_icon, "field 'ivBankIcon'", SimpleDraweeView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvCatalog = null;
    target.tvBankName = null;
    target.ivBankIcon = null;

    this.target = null;
  }
}
