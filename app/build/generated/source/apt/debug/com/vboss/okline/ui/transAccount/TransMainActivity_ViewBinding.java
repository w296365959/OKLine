// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.transAccount;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TransMainActivity_ViewBinding<T extends TransMainActivity> implements Unbinder {
  protected T target;

  private View view2131755407;

  private View view2131755409;

  private View view2131755413;

  @UiThread
  public TransMainActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.fragment_toolbar, "field 'toolbar'", FragmentToolbar.class);
    target.trans_record_listView = Utils.findRequiredViewAsType(source, R.id.trans_record_listView, "field 'trans_record_listView'", RecyclerView.class);
    target.mTabLayout = Utils.findRequiredViewAsType(source, R.id.tab_layout, "field 'mTabLayout'", TabLayout.class);
    view = Utils.findRequiredView(source, R.id.trans_next, "field 'trans_next' and method 'onViewClick'");
    target.trans_next = Utils.castView(view, R.id.trans_next, "field 'trans_next'", TextView.class);
    view2131755407 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClick(p0);
      }
    });
    target.trans_confirm = Utils.findRequiredViewAsType(source, R.id.trans_confirm, "field 'trans_confirm'", TextView.class);
    target.trans_money = Utils.findRequiredViewAsType(source, R.id.trans_money, "field 'trans_money'", LineEditText.class);
    target.trans_noInfo = Utils.findRequiredViewAsType(source, R.id.trans_noInfo, "field 'trans_noInfo'", TextView.class);
    target.trans_state_running = Utils.findRequiredViewAsType(source, R.id.trans_state_running, "field 'trans_state_running'", TextView.class);
    target.trans_state_running1 = Utils.findRequiredViewAsType(source, R.id.trans_state_running1, "field 'trans_state_running1'", TextView.class);
    target.trans_state_complete1 = Utils.findRequiredViewAsType(source, R.id.trans_state_complete1, "field 'trans_state_complete1'", TextView.class);
    target.trans_state_complete = Utils.findRequiredViewAsType(source, R.id.trans_state_complete, "field 'trans_state_complete'", TextView.class);
    target.trans_running_num = Utils.findRequiredViewAsType(source, R.id.trans_running_num, "field 'trans_running_num'", TextView.class);
    target.trans_complete_num = Utils.findRequiredViewAsType(source, R.id.trans_complete_num, "field 'trans_complete_num'", TextView.class);
    view = Utils.findRequiredView(source, R.id.trans_running_button, "field 'trans_running_button' and method 'onViewClick'");
    target.trans_running_button = Utils.castView(view, R.id.trans_running_button, "field 'trans_running_button'", LinearLayout.class);
    view2131755409 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.trans_complete_button, "field 'trans_complete_button' and method 'onViewClick'");
    target.trans_complete_button = Utils.castView(view, R.id.trans_complete_button, "field 'trans_complete_button'", LinearLayout.class);
    view2131755413 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.trans_record_listView = null;
    target.mTabLayout = null;
    target.trans_next = null;
    target.trans_confirm = null;
    target.trans_money = null;
    target.trans_noInfo = null;
    target.trans_state_running = null;
    target.trans_state_running1 = null;
    target.trans_state_complete1 = null;
    target.trans_state_complete = null;
    target.trans_running_num = null;
    target.trans_complete_num = null;
    target.trans_running_button = null;
    target.trans_complete_button = null;

    view2131755407.setOnClickListener(null);
    view2131755407 = null;
    view2131755409.setOnClickListener(null);
    view2131755409 = null;
    view2131755413.setOnClickListener(null);
    view2131755413 = null;

    this.target = null;
  }
}
