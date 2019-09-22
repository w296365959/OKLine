// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.myCard;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChooseProvinceFragment_ViewBinding<T extends ChooseProvinceFragment> implements Unbinder {
  protected T target;

  @UiThread
  public ChooseProvinceFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.lvChooseProvince = Utils.findRequiredViewAsType(source, R.id.lv_choose_province, "field 'lvChooseProvince'", ListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.lvChooseProvince = null;

    this.target = null;
  }
}
