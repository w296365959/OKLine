// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.c2cPart;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.ui.user.customized.LoadingView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class C2CwaitingActivity_ViewBinding<T extends C2CwaitingActivity> implements Unbinder {
  protected T target;

  @UiThread
  public C2CwaitingActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.loadingview = Utils.findRequiredViewAsType(source, R.id.loadingview, "field 'loadingview'", LoadingView.class);
    target.action_back = Utils.findRequiredViewAsType(source, R.id.action_back, "field 'action_back'", ImageButton.class);
    target.action_menu = Utils.findRequiredViewAsType(source, R.id.action_menu_button, "field 'action_menu'", ImageButton.class);
    target.action_title = Utils.findRequiredViewAsType(source, R.id.action_title, "field 'action_title'", TextView.class);
    target.action_logo = Utils.findRequiredViewAsType(source, R.id.sdv_logo, "field 'action_logo'", SimpleDraweeView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.loadingview = null;
    target.action_back = null;
    target.action_menu = null;
    target.action_title = null;
    target.action_logo = null;

    this.target = null;
  }
}
