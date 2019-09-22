// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.TransferAccounts;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SearchBankActivity_ViewBinding<T extends SearchBankActivity> implements Unbinder {
  protected T target;

  @UiThread
  public SearchBankActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.search_result_title = Utils.findRequiredViewAsType(source, R.id.search_result_title, "field 'search_result_title'", LinearLayout.class);
    target.search_bank_num = Utils.findRequiredViewAsType(source, R.id.search_bank_num, "field 'search_bank_num'", TextView.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.search_bank_recyclerView, "field 'recyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.search_result_title = null;
    target.search_bank_num = null;
    target.recyclerView = null;

    this.target = null;
  }
}
