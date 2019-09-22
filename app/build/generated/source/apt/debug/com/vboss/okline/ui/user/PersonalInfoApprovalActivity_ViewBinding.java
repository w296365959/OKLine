// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.user;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PersonalInfoApprovalActivity_ViewBinding<T extends PersonalInfoApprovalActivity> implements Unbinder {
  protected T target;

  @UiThread
  public PersonalInfoApprovalActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.container = Utils.findRequiredViewAsType(source, R.id.container, "field 'container'", FrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.container = null;

    this.target = null;
  }
}
