// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.myCard;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SelectAccountFragment_ViewBinding<T extends SelectAccountFragment> implements Unbinder {
  protected T target;

  @UiThread
  public SelectAccountFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.fragment_toolbar, "field 'toolbar'", FragmentToolbar.class);
    target.selectBindRecyclerView = Utils.findRequiredViewAsType(source, R.id.select_bind_recyclerView, "field 'selectBindRecyclerView'", RecyclerView.class);
    target.selectAddRecyclerView = Utils.findRequiredViewAsType(source, R.id.select_add_recyclerView, "field 'selectAddRecyclerView'", RecyclerView.class);
    target.addAccountButton = Utils.findRequiredViewAsType(source, R.id.add_account_button, "field 'addAccountButton'", TextView.class);
    target.activitySelectAccount = Utils.findRequiredViewAsType(source, R.id.activity_select_account, "field 'activitySelectAccount'", LinearLayout.class);
    target.llBindAccount = Utils.findRequiredViewAsType(source, R.id.ll_bind_account, "field 'llBindAccount'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.selectBindRecyclerView = null;
    target.selectAddRecyclerView = null;
    target.addAccountButton = null;
    target.activitySelectAccount = null;
    target.llBindAccount = null;

    this.target = null;
  }
}
