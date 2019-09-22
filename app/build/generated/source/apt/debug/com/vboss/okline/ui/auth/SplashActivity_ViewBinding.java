// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.auth;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SplashActivity_ViewBinding<T extends SplashActivity> implements Unbinder {
  protected T target;

  @UiThread
  public SplashActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.tvInfo = Utils.findRequiredViewAsType(source, R.id.tv_splash_info, "field 'tvInfo'", TextView.class);
    target.btnApprove = Utils.findRequiredViewAsType(source, R.id.btn_start_approve, "field 'btnApprove'", Button.class);
    target.checkBox = Utils.findRequiredViewAsType(source, R.id.cb_selector, "field 'checkBox'", CheckBox.class);
    target.tvTitle = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'tvTitle'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvInfo = null;
    target.btnApprove = null;
    target.checkBox = null;
    target.tvTitle = null;

    this.target = null;
  }
}
