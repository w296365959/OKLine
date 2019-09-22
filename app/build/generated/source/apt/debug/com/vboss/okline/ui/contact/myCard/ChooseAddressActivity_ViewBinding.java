// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.myCard;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.ui.contact.NonScrollableViewPager;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChooseAddressActivity_ViewBinding<T extends ChooseAddressActivity> implements Unbinder {
  protected T target;

  @UiThread
  public ChooseAddressActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_choose_address, "field 'toolbar'", FragmentToolbar.class);
    target.viewPager = Utils.findRequiredViewAsType(source, R.id.choose_address_viewpager, "field 'viewPager'", NonScrollableViewPager.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.viewPager = null;

    this.target = null;
  }
}
