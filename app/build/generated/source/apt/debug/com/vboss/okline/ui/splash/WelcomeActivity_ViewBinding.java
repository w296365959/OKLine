// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.splash;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WelcomeActivity_ViewBinding<T extends WelcomeActivity> implements Unbinder {
  protected T target;

  @UiThread
  public WelcomeActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.draweeView = Utils.findRequiredViewAsType(source, R.id.sdv_welcome, "field 'draweeView'", SimpleDraweeView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.draweeView = null;

    this.target = null;
  }
}
