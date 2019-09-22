// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.myCard;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EditAddressAdapter$ViewHolder_ViewBinding<T extends EditAddressAdapter.ViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public EditAddressAdapter$ViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.tvName = Utils.findRequiredViewAsType(source, R.id.tv_name, "field 'tvName'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvName = null;

    this.target = null;
  }
}
