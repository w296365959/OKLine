// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.search;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.card.widget.SliderListView;
import com.vboss.okline.view.widget.ClearEditText;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ContactsSearchFragment_ViewBinding<T extends ContactsSearchFragment> implements Unbinder {
  protected T target;

  private View view2131755156;

  private View view2131755158;

  private View view2131755164;

  private View view2131755172;

  @UiThread
  public ContactsSearchFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.actionBack = Utils.findRequiredViewAsType(source, R.id.action_back, "field 'actionBack'", ImageButton.class);
    view = Utils.findRequiredView(source, R.id.action_back_layout, "field 'actionBackLayout' and method 'onViewClicked'");
    target.actionBackLayout = Utils.castView(view, R.id.action_back_layout, "field 'actionBackLayout'", RelativeLayout.class);
    view2131755156 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ib_more, "field 'ibMore' and method 'onViewClicked'");
    target.ibMore = Utils.castView(view, R.id.ib_more, "field 'ibMore'", ImageButton.class);
    view2131755158 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.logoAnimation = Utils.findRequiredViewAsType(source, R.id.logo_animation, "field 'logoAnimation'", ImageView.class);
    target.rlLogo = Utils.findRequiredViewAsType(source, R.id.rl_logo, "field 'rlLogo'", RelativeLayout.class);
    view = Utils.findRequiredView(source, R.id.tv_cancel, "field 'tvCancel' and method 'onViewClicked'");
    target.tvCancel = Utils.castView(view, R.id.tv_cancel, "field 'tvCancel'", TextView.class);
    view2131755164 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.edtCard = Utils.findRequiredViewAsType(source, R.id.edt_card, "field 'edtCard'", ClearEditText.class);
    target.rlToolbar = Utils.findRequiredViewAsType(source, R.id.rl_toolbar, "field 'rlToolbar'", RelativeLayout.class);
    target.listView = Utils.findRequiredViewAsType(source, R.id.listView, "field 'listView'", SliderListView.class);
    target.frameLayout = Utils.findRequiredViewAsType(source, R.id.fl_data, "field 'frameLayout'", FrameLayout.class);
    target.rlCardLogQuery = Utils.findRequiredViewAsType(source, R.id.rl_card_log_query, "field 'rlCardLogQuery'", RecyclerView.class);
    target.refreshFrameLayout = Utils.findRequiredViewAsType(source, R.id.refreshFrameLayout, "field 'refreshFrameLayout'", PtrClassicFrameLayout.class);
    target.frameLayout1 = Utils.findRequiredViewAsType(source, R.id.fl_log_data, "field 'frameLayout1'", FrameLayout.class);
    view = Utils.findRequiredView(source, R.id.empty, "field 'empty' and method 'onViewClicked'");
    target.empty = view;
    view2131755172 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.emptyLayout = Utils.findRequiredViewAsType(source, R.id.fl_no_data, "field 'emptyLayout'", RelativeLayout.class);
    target.tv_search_key = Utils.findRequiredViewAsType(source, R.id.tv_search_key, "field 'tv_search_key'", TextView.class);
    target.logoView = Utils.findRequiredViewAsType(source, R.id.logoView, "field 'logoView'", LogoView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.actionBack = null;
    target.actionBackLayout = null;
    target.ibMore = null;
    target.logoAnimation = null;
    target.rlLogo = null;
    target.tvCancel = null;
    target.edtCard = null;
    target.rlToolbar = null;
    target.listView = null;
    target.frameLayout = null;
    target.rlCardLogQuery = null;
    target.refreshFrameLayout = null;
    target.frameLayout1 = null;
    target.empty = null;
    target.emptyLayout = null;
    target.tv_search_key = null;
    target.logoView = null;

    view2131755156.setOnClickListener(null);
    view2131755156 = null;
    view2131755158.setOnClickListener(null);
    view2131755158 = null;
    view2131755164.setOnClickListener(null);
    view2131755164 = null;
    view2131755172.setOnClickListener(null);
    view2131755172 = null;

    this.target = null;
  }
}
