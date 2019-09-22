// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.app.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class JiugonggeAdapter$ViewHolder_ViewBinding<T extends JiugonggeAdapter.ViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public JiugonggeAdapter$ViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.jiugongge_item = Utils.findRequiredViewAsType(source, R.id.jiugongge_item, "field 'jiugongge_item'", LinearLayout.class);
    target.app_gridview = Utils.findRequiredViewAsType(source, R.id.app_gridview, "field 'app_gridview'", GridView.class);
    target.jiugongge_typeName = Utils.findRequiredViewAsType(source, R.id.jiugongge_typeName, "field 'jiugongge_typeName'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.jiugongge_item = null;
    target.app_gridview = null;
    target.jiugongge_typeName = null;

    this.target = null;
  }
}
