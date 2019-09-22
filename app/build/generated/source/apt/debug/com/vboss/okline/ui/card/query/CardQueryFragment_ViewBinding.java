// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.card.query;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.ui.card.widget.SliderListView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardQueryFragment_ViewBinding<T extends CardQueryFragment> implements Unbinder {
  protected T target;

  @UiThread
  public CardQueryFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.mListView = Utils.findRequiredViewAsType(source, R.id.listView, "field 'mListView'", SliderListView.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.rl_card_log_query, "field 'recyclerView'", RecyclerView.class);
    target.refreshFrameLayout = Utils.findRequiredViewAsType(source, R.id.refreshFrameLayout, "field 'refreshFrameLayout'", PtrClassicFrameLayout.class);
    target.tv_search_count = Utils.findRequiredViewAsType(source, R.id.tv_search_count, "field 'tv_search_count'", TextView.class);
    target.layout_count = Utils.findRequiredViewAsType(source, R.id.layout_count, "field 'layout_count'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mListView = null;
    target.recyclerView = null;
    target.refreshFrameLayout = null;
    target.tv_search_count = null;
    target.layout_count = null;

    this.target = null;
  }
}
