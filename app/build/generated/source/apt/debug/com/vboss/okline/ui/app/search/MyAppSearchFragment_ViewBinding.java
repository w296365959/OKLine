// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.app.search;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyAppSearchFragment_ViewBinding<T extends MyAppSearchFragment> implements Unbinder {
  protected T target;

  @UiThread
  public MyAppSearchFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.app_search_RecyclerView = Utils.findRequiredViewAsType(source, R.id.app_search_RecyclerView, "field 'app_search_RecyclerView'", RecyclerView.class);
    target.searchResultNum = Utils.findRequiredViewAsType(source, R.id.search_result_num, "field 'searchResultNum'", TextView.class);
    target.hot_app_title = Utils.findRequiredViewAsType(source, R.id.hot_app_title, "field 'hot_app_title'", TextView.class);
    target.search_app_title = Utils.findRequiredViewAsType(source, R.id.search_app_title, "field 'search_app_title'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.app_search_RecyclerView = null;
    target.searchResultNum = null;
    target.hot_app_title = null;
    target.search_app_title = null;

    this.target = null;
  }
}
