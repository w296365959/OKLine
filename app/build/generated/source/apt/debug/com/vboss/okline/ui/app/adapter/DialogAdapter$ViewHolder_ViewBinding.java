// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.app.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DialogAdapter$ViewHolder_ViewBinding<T extends DialogAdapter.ViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public DialogAdapter$ViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.appName = Utils.findRequiredViewAsType(source, R.id.app_name, "field 'appName'", TextView.class);
    target.appIcon = Utils.findRequiredViewAsType(source, R.id.app_icon, "field 'appIcon'", SimpleDraweeView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.appName = null;
    target.appIcon = null;

    this.target = null;
  }
}
