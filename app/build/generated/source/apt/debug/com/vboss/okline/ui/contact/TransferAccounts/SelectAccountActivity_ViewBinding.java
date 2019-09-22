// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.TransferAccounts;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SelectAccountActivity_ViewBinding<T extends SelectAccountActivity> implements Unbinder {
  protected T target;

  @UiThread
  public SelectAccountActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.fragment_toolbar, "field 'toolbar'", FragmentToolbar.class);
    target.select_add_recyclerView = Utils.findRequiredViewAsType(source, R.id.select_add_recyclerView, "field 'select_add_recyclerView'", RecyclerView.class);
    target.select_bind_recyclerView = Utils.findRequiredViewAsType(source, R.id.select_bind_recyclerView, "field 'select_bind_recyclerView'", RecyclerView.class);
    target.add_account_button = Utils.findRequiredViewAsType(source, R.id.add_account_button, "field 'add_account_button'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.select_add_recyclerView = null;
    target.select_bind_recyclerView = null;
    target.add_account_button = null;

    this.target = null;
  }
}
