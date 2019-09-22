// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.TransferAccounts;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EditAccountActivity_ViewBinding<T extends EditAccountActivity> implements Unbinder {
  protected T target;

  private View view2131755266;

  private View view2131755267;

  @UiThread
  public EditAccountActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.fragment_toolbar, "field 'toolbar'", FragmentToolbar.class);
    target.account_no = Utils.findRequiredViewAsType(source, R.id.account_no, "field 'account_no'", EditText.class);
    target.account_name = Utils.findRequiredViewAsType(source, R.id.account_name, "field 'account_name'", EditText.class);
    view = Utils.findRequiredView(source, R.id.to_select_bank, "field 'toSelectBank' and method 'ViewOnClick'");
    target.toSelectBank = Utils.castView(view, R.id.to_select_bank, "field 'toSelectBank'", TextView.class);
    view2131755266 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ViewOnClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.save_account_button, "method 'ViewOnClick'");
    view2131755267 = view;
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
    target.account_no = null;
    target.account_name = null;
    target.toSelectBank = null;

    view2131755266.setOnClickListener(null);
    view2131755266 = null;
    view2131755267.setOnClickListener(null);
    view2131755267 = null;

    this.target = null;
  }
}
