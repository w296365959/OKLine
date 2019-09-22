// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.express.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HorizontalExpressCompanyRecyclerViewAdapter$HHorizontalExpressCompanyRecyclerViewHolder_ViewBinding<T extends HorizontalExpressCompanyRecyclerViewAdapter.HHorizontalExpressCompanyRecyclerViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public HorizontalExpressCompanyRecyclerViewAdapter$HHorizontalExpressCompanyRecyclerViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.expressEditKdTextView = Utils.findRequiredViewAsType(source, R.id.express_edit_kd_textView, "field 'expressEditKdTextView'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.expressEditKdTextView = null;

    this.target = null;
  }
}
