// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.opay;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardListFragment_ViewBinding<T extends CardListFragment> implements Unbinder {
  protected T target;

  @UiThread
  public CardListFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.ivReturn3 = Utils.findRequiredViewAsType(source, R.id.iv_return3, "field 'ivReturn3'", ImageView.class);
    target.lvBankaccount = Utils.findRequiredViewAsType(source, R.id.lv_bankaccount, "field 'lvBankaccount'", ListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.ivReturn3 = null;
    target.lvBankaccount = null;

    this.target = null;
  }
}
