// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.app.search;

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
import com.vboss.okline.view.widget.ClearEditText;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AppSearchFragment_ViewBinding<T extends AppSearchFragment> implements Unbinder {
  protected T target;

  private View view2131755172;

  private View view2131755156;

  private View view2131755158;

  @UiThread
  public AppSearchFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.edt_card = Utils.findRequiredViewAsType(source, R.id.edt_card, "field 'edt_card'", ClearEditText.class);
    view = Utils.findRequiredView(source, R.id.empty, "field 'empty' and method 'onActionBarClick'");
    target.empty = view;
    view2131755172 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onActionBarClick(p0);
      }
    });
    target.search_app_RecyclerView = Utils.findRequiredViewAsType(source, R.id.search_app_RecyclerView, "field 'search_app_RecyclerView'", RecyclerView.class);
    target.frameLayout = Utils.findRequiredViewAsType(source, R.id.fl_data, "field 'frameLayout'", FrameLayout.class);
    target.no_result = Utils.findRequiredViewAsType(source, R.id.no_result, "field 'no_result'", RelativeLayout.class);
    target.search_content = Utils.findRequiredViewAsType(source, R.id.search_content, "field 'search_content'", TextView.class);
    target.tv_cancel = Utils.findRequiredViewAsType(source, R.id.tv_cancel, "field 'tv_cancel'", TextView.class);
    view = Utils.findRequiredView(source, R.id.action_back_layout, "field 'action_back_layout' and method 'onActionBarClick'");
    target.action_back_layout = Utils.castView(view, R.id.action_back_layout, "field 'action_back_layout'", RelativeLayout.class);
    view2131755156 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onActionBarClick(p0);
      }
    });
    target.action_back = Utils.findRequiredViewAsType(source, R.id.action_back, "field 'action_back'", ImageButton.class);
    target.logo = Utils.findRequiredViewAsType(source, R.id.iv, "field 'logo'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.ib_more, "method 'onActionBarClick'");
    view2131755158 = view;
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
    target.search_app_RecyclerView = null;
    target.frameLayout = null;
    target.no_result = null;
    target.search_content = null;
    target.tv_cancel = null;
    target.action_back_layout = null;
    target.action_back = null;
    target.logo = null;

    view2131755172.setOnClickListener(null);
    view2131755172 = null;
    view2131755156.setOnClickListener(null);
    view2131755156 = null;
    view2131755158.setOnClickListener(null);
    view2131755158 = null;

    this.target = null;
  }
}
