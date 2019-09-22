// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.myCard;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProvinceFragment_ViewBinding<T extends ProvinceFragment> implements Unbinder {
  protected T target;

  @UiThread
  public ProvinceFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_contact_newcard, "field 'toolbar'", FragmentToolbar.class);
    target.tvAddress = Utils.findRequiredViewAsType(source, R.id.tv_address, "field 'tvAddress'", TextView.class);
    target.mProvinceSpinner = Utils.findRequiredViewAsType(source, R.id.spinner_pro, "field 'mProvinceSpinner'", Spinner.class);
    target.mCitySpinner = Utils.findRequiredViewAsType(source, R.id.spinner_city, "field 'mCitySpinner'", Spinner.class);
    target.mAreaSpinner = Utils.findRequiredViewAsType(source, R.id.spinner_area, "field 'mAreaSpinner'", Spinner.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.tvAddress = null;
    target.mProvinceSpinner = null;
    target.mCitySpinner = null;
    target.mAreaSpinner = null;

    this.target = null;
  }
}
