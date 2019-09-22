// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.myCard;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChooseBankFragment_ViewBinding<T extends ChooseBankFragment> implements Unbinder {
  protected T target;

  @UiThread
  public ChooseBankFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_choose_bank, "field 'toolbar'", FragmentToolbar.class);
    target.lvChooseBank = Utils.findRequiredViewAsType(source, R.id.lv_choose_bank, "field 'lvChooseBank'", ListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.lvChooseBank = null;

    this.target = null;
  }
}
