// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.TransferAccounts;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TransRecordActivity_ViewBinding<T extends TransRecordActivity> implements Unbinder {
  protected T target;

  @UiThread
  public TransRecordActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.fragment_toolbar, "field 'toolbar'", FragmentToolbar.class);
    target.trans_record_recyclerView = Utils.findRequiredViewAsType(source, R.id.trans_record_recyclerView, "field 'trans_record_recyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.trans_record_recyclerView = null;

    this.target = null;
  }
}
