// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.card.query;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.ui.card.widget.SliderListView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardSearchFragment_ViewBinding<T extends CardSearchFragment> implements Unbinder {
  protected T target;

  private View view2131755172;

  private View view2131755158;

  private View view2131755156;

  private View view2131755164;

  @UiThread
  public CardSearchFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.edt_card = Utils.findRequiredViewAsType(source, R.id.edt_card, "field 'edt_card'", EditText.class);
    view = Utils.findRequiredView(source, R.id.empty, "field 'empty' and method 'onActionBarClick'");
    target.empty = view;
    view2131755172 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onActionBarClick(p0);
      }
    });
    target.listView = Utils.findRequiredViewAsType(source, R.id.listView, "field 'listView'", SliderListView.class);
    target.frameLayout = Utils.findRequiredViewAsType(source, R.id.fl_data, "field 'frameLayout'", FrameLayout.class);
    target.refreshFrameLayout = Utils.findRequiredViewAsType(source, R.id.refreshFrameLayout, "field 'refreshFrameLayout'", PtrClassicFrameLayout.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.rl_card_log_query, "field 'recyclerView'", RecyclerView.class);
    target.frameLayout1 = Utils.findRequiredViewAsType(source, R.id.fl_log_data, "field 'frameLayout1'", FrameLayout.class);
    view = Utils.findRequiredView(source, R.id.ib_more, "field 'ib_more' and method 'onActionBarClick'");
    target.ib_more = Utils.castView(view, R.id.ib_more, "field 'ib_more'", ImageButton.class);
    view2131755158 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onActionBarClick(p0);
      }
    });
    target.action_back = Utils.findRequiredViewAsType(source, R.id.action_back, "field 'action_back'", ImageButton.class);
    target.emptyLayout = Utils.findRequiredViewAsType(source, R.id.fl_no_data, "field 'emptyLayout'", RelativeLayout.class);
    target.tv_search_key = Utils.findRequiredViewAsType(source, R.id.tv_search_key, "field 'tv_search_key'", TextView.class);
    view = Utils.findRequiredView(source, R.id.action_back_layout, "field 'action_back_layout' and method 'onActionBarClick'");
    target.action_back_layout = Utils.castView(view, R.id.action_back_layout, "field 'action_back_layout'", RelativeLayout.class);
    view2131755156 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onActionBarClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_cancel, "method 'onActionBarClick'");
    view2131755164 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onActionBarClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.edt_card = null;
    target.empty = null;
    target.listView = null;
    target.frameLayout = null;
    target.refreshFrameLayout = null;
    target.recyclerView = null;
    target.frameLayout1 = null;
    target.ib_more = null;
    target.action_back = null;
    target.emptyLayout = null;
    target.tv_search_key = null;
    target.action_back_layout = null;

    view2131755172.setOnClickListener(null);
    view2131755172 = null;
    view2131755158.setOnClickListener(null);
    view2131755158 = null;
    view2131755156.setOnClickListener(null);
    view2131755156 = null;
    view2131755164.setOnClickListener(null);
    view2131755164 = null;

    this.target = null;
  }
}
