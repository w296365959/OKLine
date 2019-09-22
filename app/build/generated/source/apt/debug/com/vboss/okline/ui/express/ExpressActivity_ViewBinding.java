// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.express;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.view.widget.ExpressNoViewPager;
import com.vboss.okline.view.widget.OKCardView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ExpressActivity_ViewBinding<T extends ExpressActivity> implements Unbinder {
  protected T target;

  private View view2131755157;

  private View view2131755295;

  private View view2131755271;

  private View view2131755272;

  private View view2131755273;

  private View view2131755276;

  @UiThread
  public ExpressActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.action_back, "field 'actionBack' and method 'onViewClicked'");
    target.actionBack = Utils.castView(view, R.id.action_back, "field 'actionBack'", ImageButton.class);
    view2131755157 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.ivOcardState = Utils.findRequiredViewAsType(source, R.id.iv_ocard_state, "field 'ivOcardState'", LogoView.class);
    target.okcardView = Utils.findRequiredViewAsType(source, R.id.okcard_view, "field 'okcardView'", OKCardView.class);
    target.actionBackLayout = Utils.findRequiredViewAsType(source, R.id.action_back_layout, "field 'actionBackLayout'", RelativeLayout.class);
    target.sdvLogo = Utils.findRequiredViewAsType(source, R.id.sdv_logo, "field 'sdvLogo'", SimpleDraweeView.class);
    target.actionTitle = Utils.findRequiredViewAsType(source, R.id.action_title, "field 'actionTitle'", TextView.class);
    target.actionMenuButton = Utils.findRequiredViewAsType(source, R.id.action_menu_button, "field 'actionMenuButton'", ImageButton.class);
    view = Utils.findRequiredView(source, R.id.action_menu_right_rl, "field 'actionMenuRightRl' and method 'onViewClicked'");
    target.actionMenuRightRl = Utils.castView(view, R.id.action_menu_right_rl, "field 'actionMenuRightRl'", RelativeLayout.class);
    view2131755295 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.express_on_the_way_tv, "field 'expressOnTheWayTv' and method 'onViewClicked'");
    target.expressOnTheWayTv = Utils.castView(view, R.id.express_on_the_way_tv, "field 'expressOnTheWayTv'", TextView.class);
    view2131755271 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.express_to_be_confirmed_tv, "field 'expressToBeConfirmedTv' and method 'onViewClicked'");
    target.expressToBeConfirmedTv = Utils.castView(view, R.id.express_to_be_confirmed_tv, "field 'expressToBeConfirmedTv'", TextView.class);
    view2131755272 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.express_completed_tv, "field 'expressCompletedTv' and method 'onViewClicked'");
    target.expressCompletedTv = Utils.castView(view, R.id.express_completed_tv, "field 'expressCompletedTv'", TextView.class);
    view2131755273 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.expressNoViewPager = Utils.findRequiredViewAsType(source, R.id.express_noViewPager, "field 'expressNoViewPager'", ExpressNoViewPager.class);
    view = Utils.findRequiredView(source, R.id.express_next_tv, "field 'expressNextTv' and method 'onViewClicked'");
    target.expressNextTv = Utils.castView(view, R.id.express_next_tv, "field 'expressNextTv'", TextView.class);
    view2131755276 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.activityExpressBottomRl = Utils.findRequiredViewAsType(source, R.id.activity_express_bottom_rl, "field 'activityExpressBottomRl'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.actionBack = null;
    target.ivOcardState = null;
    target.okcardView = null;
    target.actionBackLayout = null;
    target.sdvLogo = null;
    target.actionTitle = null;
    target.actionMenuButton = null;
    target.actionMenuRightRl = null;
    target.expressOnTheWayTv = null;
    target.expressToBeConfirmedTv = null;
    target.expressCompletedTv = null;
    target.expressNoViewPager = null;
    target.expressNextTv = null;
    target.activityExpressBottomRl = null;

    view2131755157.setOnClickListener(null);
    view2131755157 = null;
    view2131755295.setOnClickListener(null);
    view2131755295 = null;
    view2131755271.setOnClickListener(null);
    view2131755271 = null;
    view2131755272.setOnClickListener(null);
    view2131755272 = null;
    view2131755273.setOnClickListener(null);
    view2131755273 = null;
    view2131755276.setOnClickListener(null);
    view2131755276 = null;

    this.target = null;
  }
}
