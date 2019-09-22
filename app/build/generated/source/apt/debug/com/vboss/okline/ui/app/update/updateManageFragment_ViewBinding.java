// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.app.update;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class updateManageFragment_ViewBinding<T extends updateManageFragment> implements Unbinder {
  protected T target;

  private View view2131756268;

  private View view2131756270;

  @UiThread
  public updateManageFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.updateRecyclerview = Utils.findRequiredViewAsType(source, R.id.update_recyclerview, "field 'updateRecyclerview'", RecyclerView.class);
    target.need_update_num = Utils.findRequiredViewAsType(source, R.id.need_update_num, "field 'need_update_num'", TextView.class);
    view = Utils.findRequiredView(source, R.id.update_back, "method 'ViewOnClick'");
    view2131756268 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ViewOnClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.all_update, "method 'ViewOnClick'");
    view2131756270 = view;
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

    target.updateRecyclerview = null;
    target.need_update_num = null;

    view2131756268.setOnClickListener(null);
    view2131756268 = null;
    view2131756270.setOnClickListener(null);
    view2131756270 = null;

    this.target = null;
  }
}
