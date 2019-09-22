// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.app;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.ui.app.jiugongge.DraggableGridViewPager;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.view.widget.OKCardView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AppFragment_ViewBinding<T extends AppFragment> implements Unbinder {
  protected T target;

  private View view2131755926;

  private View view2131755923;

  private View view2131755924;

  private View view2131755158;

  @UiThread
  public AppFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.appRecyclerView = Utils.findRequiredViewAsType(source, R.id.app_recyclerView, "field 'appRecyclerView'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.enter_apppool_button, "field 'enterApppoolButton' and method 'ViewOnClick'");
    target.enterApppoolButton = Utils.castView(view, R.id.enter_apppool_button, "field 'enterApppoolButton'", TextView.class);
    view2131755926 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ViewOnClick(p0);
      }
    });
    target.noApp = Utils.findRequiredViewAsType(source, R.id.no_app, "field 'noApp'", TextView.class);
    view = Utils.findRequiredView(source, R.id.app_update_RL, "field 'appUpdateRL' and method 'ViewOnClick'");
    target.appUpdateRL = Utils.castView(view, R.id.app_update_RL, "field 'appUpdateRL'", RelativeLayout.class);
    view2131755923 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ViewOnClick(p0);
      }
    });
    target.jiugongge_viewpager = Utils.findRequiredViewAsType(source, R.id.jiugongge_viewpager, "field 'jiugongge_viewpager'", DraggableGridViewPager.class);
    target.dot_container = Utils.findRequiredViewAsType(source, R.id.dot_container, "field 'dot_container'", LinearLayout.class);
    target.logoView = Utils.findRequiredViewAsType(source, R.id.logoView, "field 'logoView'", LogoView.class);
    target.okCardView = Utils.findRequiredViewAsType(source, R.id.ocardView, "field 'okCardView'", OKCardView.class);
    target.jiugongge_bg = Utils.findRequiredViewAsType(source, R.id.jiugongge_bg, "field 'jiugongge_bg'", FrameLayout.class);
    view = Utils.findRequiredView(source, R.id.myapp_search, "method 'ViewOnClick'");
    view2131755924 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ViewOnClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ib_more, "method 'ViewOnClick'");
    view2131755158 = view;
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

    target.appRecyclerView = null;
    target.enterApppoolButton = null;
    target.noApp = null;
    target.appUpdateRL = null;
    target.jiugongge_viewpager = null;
    target.dot_container = null;
    target.logoView = null;
    target.okCardView = null;
    target.jiugongge_bg = null;

    view2131755926.setOnClickListener(null);
    view2131755926 = null;
    view2131755923.setOnClickListener(null);
    view2131755923 = null;
    view2131755924.setOnClickListener(null);
    view2131755924 = null;
    view2131755158.setOnClickListener(null);
    view2131755158 = null;

    this.target = null;
  }
}
