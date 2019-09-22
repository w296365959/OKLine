// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CallLogAdapter$ViewHolder_ViewBinding<T extends CallLogAdapter.ViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public CallLogAdapter$ViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.itemUnderline = Utils.findRequiredViewAsType(source, R.id.item_underline, "field 'itemUnderline'", TextView.class);
    target.tvPhoneNumber = Utils.findRequiredViewAsType(source, R.id.tv_phone_number, "field 'tvPhoneNumber'", TextView.class);
    target.tvLastDate = Utils.findRequiredViewAsType(source, R.id.tv_last_date, "field 'tvLastDate'", TextView.class);
    target.tvAdd = Utils.findRequiredViewAsType(source, R.id.tv_add, "field 'tvAdd'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.itemUnderline = null;
    target.tvPhoneNumber = null;
    target.tvLastDate = null;
    target.tvAdd = null;

    this.target = null;
  }
}
