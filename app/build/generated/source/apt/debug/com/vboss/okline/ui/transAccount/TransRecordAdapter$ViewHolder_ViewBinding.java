// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.transAccount;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TransRecordAdapter$ViewHolder_ViewBinding<T extends TransRecordAdapter.ViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public TransRecordAdapter$ViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.bankName = Utils.findRequiredViewAsType(source, R.id.trans_record_bankName, "field 'bankName'", TextView.class);
    target.name = Utils.findRequiredViewAsType(source, R.id.trans_record_name, "field 'name'", TextView.class);
    target.directionIcon = Utils.findRequiredViewAsType(source, R.id.trans_direction, "field 'directionIcon'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.bankName = null;
    target.name = null;
    target.directionIcon = null;

    this.target = null;
  }
}
