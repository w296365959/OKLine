// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.record.search;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RecordSearchActivity_ViewBinding<T extends RecordSearchActivity> implements Unbinder {
  protected T target;

  private View view2131755371;

  private View view2131755157;

  @UiThread
  public RecordSearchActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
    target.ptrFrameLayout = Utils.findRequiredViewAsType(source, R.id.ptrFrameLayout, "field 'ptrFrameLayout'", PtrClassicFrameLayout.class);
    target.searchContentView = Utils.findRequiredViewAsType(source, R.id.searchContentView, "field 'searchContentView'", EditText.class);
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
    view = Utils.findRequiredView(source, R.id.action_back, "method 'onClick'");
    view2131755157 = view;
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

    target.recyclerView = null;
    target.ptrFrameLayout = null;
    target.searchContentView = null;
    target.searchBtn = null;
    target.countTextView = null;
    target.itemCountLL = null;

    view2131755371.setOnClickListener(null);
    view2131755371 = null;
    view2131755157.setOnClickListener(null);
    view2131755157 = null;

    this.target = null;
  }
}
