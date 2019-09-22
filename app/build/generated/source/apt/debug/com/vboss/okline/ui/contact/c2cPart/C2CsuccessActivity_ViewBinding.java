// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.c2cPart;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class C2CsuccessActivity_ViewBinding<T extends C2CsuccessActivity> implements Unbinder {
  protected T target;

  @UiThread
  public C2CsuccessActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.tvGatherTotalMoney = Utils.findRequiredViewAsType(source, R.id.tv_gather_total_money, "field 'tvGatherTotalMoney'", TextView.class);
    target.tvGatherMoney = Utils.findRequiredViewAsType(source, R.id.tv_gather_money, "field 'tvGatherMoney'", TextView.class);
    target.tvGatherAccount = Utils.findRequiredViewAsType(source, R.id.tv_gather_account, "field 'tvGatherAccount'", TextView.class);
    target.tvGatherFromAccount = Utils.findRequiredViewAsType(source, R.id.tv_gather_from_account, "field 'tvGatherFromAccount'", TextView.class);
    target.tvGatherDate = Utils.findRequiredViewAsType(source, R.id.tv_gather_date, "field 'tvGatherDate'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvGatherTotalMoney = null;
    target.tvGatherMoney = null;
    target.tvGatherAccount = null;
    target.tvGatherFromAccount = null;
    target.tvGatherDate = null;

    this.target = null;
  }
}
