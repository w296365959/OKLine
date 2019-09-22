// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.myCard;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyCardFragment_ViewBinding<T extends MyCardFragment> implements Unbinder {
  protected T target;

  @UiThread
  public MyCardFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.sdvUserAvatar = Utils.findRequiredViewAsType(source, R.id.sdv_user_avatar, "field 'sdvUserAvatar'", SimpleDraweeView.class);
    target.tvUserRemark = Utils.findRequiredViewAsType(source, R.id.tv_user_remark, "field 'tvUserRemark'", TextView.class);
    target.tvUserPhone = Utils.findRequiredViewAsType(source, R.id.tv_user_phone, "field 'tvUserPhone'", TextView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_contact, "field 'toolbar'", FragmentToolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.sdvUserAvatar = null;
    target.tvUserRemark = null;
    target.tvUserPhone = null;
    target.toolbar = null;

    this.target = null;
  }
}
