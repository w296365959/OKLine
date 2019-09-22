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

public class HorizontalExpressCompanyRecyclerViewAdapter$HorizontalExpressCompanyRecyclerViewHolder_ViewBinding<T extends HorizontalExpressCompanyRecyclerViewAdapter.HorizontalExpressCompanyRecyclerViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public HorizontalExpressCompanyRecyclerViewAdapter$HorizontalExpressCompanyRecyclerViewHolder_ViewBinding(T target, View source) {
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
