// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.record.search;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.view.widget.ClearEditText;
import com.vboss.okline.view.widget.OKCardView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RecordSearchFragment_ViewBinding<T extends RecordSearchFragment> implements Unbinder {
  protected T target;

  private View view2131755157;

  private View view2131755371;

  private View view2131755172;

  @UiThread
  public RecordSearchFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.action_back, "field 'actionBack' and method 'onClick'");
    target.actionBack = Utils.castView(view, R.id.action_back, "field 'actionBack'", ImageButton.class);
    view2131755157 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.actionBackLayout = Utils.findRequiredViewAsType(source, R.id.action_back_layout, "field 'actionBackLayout'", RelativeLayout.class);
    target.ivOklineLogo = Utils.findRequiredViewAsType(source, R.id.iv_okline_logo, "field 'ivOklineLogo'", LogoView.class);
    target.okcardView = Utils.findRequiredViewAsType(source, R.id.okcard_view, "field 'okcardView'", OKCardView.class);
    target.tvSearchContent = Utils.findRequiredViewAsType(source, R.id.tv_search_content, "field 'tvSearchContent'", TextView.class);
    target.searchContentView = Utils.findRequiredViewAsType(source, R.id.searchContentView, "field 'searchContentView'", ClearEditText.class);
    view = Utils.findRequiredView(source, R.id.searchBtn, "field 'searchBtn' and method 'onClick'");
    target.searchBtn = Utils.castView(view, R.id.searchBtn, "field 'searchBtn'", TextView.class);
    view2131755371 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.countTextView = Utils.findRequiredViewAsType(source, R.id.countTextView, "field 'countTextView'", TextView.class);
    target.itemCountLL = Utils.findRequiredViewAsType(source, R.id.itemCountLL, "field 'itemCountLL'", LinearLayout.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
    target.ptrFrameLayout = Utils.findRequiredViewAsType(source, R.id.ptrFrameLayout, "field 'ptrFrameLayout'", PtrClassicFrameLayout.class);
    view = Utils.findRequiredView(source, R.id.empty, "field 'empty' and method 'onClick'");
    target.empty = view;
    view2131755172 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.actionBack = null;
    target.actionBackLayout = null;
    target.ivOklineLogo = null;
    target.okcardView = null;
    target.tvSearchContent = null;
    target.searchContentView = null;
    target.searchBtn = null;
    target.countTextView = null;
    target.itemCountLL = null;
    target.recyclerView = null;
    target.ptrFrameLayout = null;
    target.empty = null;

    view2131755157.setOnClickListener(null);
    view2131755157 = null;
    view2131755371.setOnClickListener(null);
    view2131755371 = null;
    view2131755172.setOnClickListener(null);
    view2131755172 = null;

    this.target = null;
  }
}
