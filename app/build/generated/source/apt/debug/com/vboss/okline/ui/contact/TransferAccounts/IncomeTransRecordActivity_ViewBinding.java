// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.TransferAccounts;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class IncomeTransRecordActivity_ViewBinding<T extends IncomeTransRecordActivity> implements Unbinder {
  protected T target;

  @UiThread
  public IncomeTransRecordActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.fragment_toolbar, "field 'toolbar'", FragmentToolbar.class);
    target.incomeaccount_edit = Utils.findRequiredViewAsType(source, R.id.incomeaccount_edit, "field 'incomeaccount_edit'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.incomeaccount_edit = null;

    this.target = null;
  }
}
