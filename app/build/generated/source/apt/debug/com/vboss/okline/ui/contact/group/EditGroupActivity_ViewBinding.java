// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.group;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EditGroupActivity_ViewBinding<T extends EditGroupActivity> implements Unbinder {
  protected T target;

  private View view2131755270;

  @UiThread
  public EditGroupActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.tv_exit, "field 'tvExit' and method 'onClick'");
    target.tvExit = Utils.castView(view, R.id.tv_exit, "field 'tvExit'", TextView.class);
    view2131755270 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_edit_group, "field 'toolbar'", FragmentToolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.recyclerView = null;
    target.tvExit = null;
    target.toolbar = null;

    view2131755270.setOnClickListener(null);
    view2131755270 = null;

    this.target = null;
  }
}
