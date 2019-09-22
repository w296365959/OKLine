// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.card.recharge;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.ui.opay.NonScrollableViewPager;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardRechargeActivity_ViewBinding<T extends CardRechargeActivity> implements Unbinder {
  protected T target;

  @UiThread
  public CardRechargeActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.vpOpenCard = Utils.findRequiredViewAsType(source, R.id.vp_open_card, "field 'vpOpenCard'", NonScrollableViewPager.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.vpOpenCard = null;

    this.target = null;
  }
}
