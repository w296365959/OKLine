// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.callPhone;

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

public class DialActivity_ViewBinding<T extends DialActivity> implements Unbinder {
  protected T target;

  @UiThread
  public DialActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_contact, "field 'toolbar'", FragmentToolbar.class);
    target.lvNewContact = Utils.findRequiredViewAsType(source, R.id.lv_newContact, "field 'lvNewContact'", ListView.class);
    target.keyboardView = Utils.findRequiredViewAsType(source, R.id.keyboardView, "field 'keyboardView'", KeyboardView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.lvNewContact = null;
    target.keyboardView = null;

    this.target = null;
  }
}
