// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.group;

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

public class EditGroupActivity$MemberAdapter$ViewHolder_ViewBinding<T extends EditGroupActivity.MemberAdapter.ViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public EditGroupActivity$MemberAdapter$ViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.ivAvatar = Utils.findRequiredViewAsType(source, R.id.iv_avatar, "field 'ivAvatar'", SimpleDraweeView.class);
    target.tvName = Utils.findRequiredViewAsType(source, R.id.tv_name, "field 'tvName'", TextView.class);
    target.tvRemarkName = Utils.findRequiredViewAsType(source, R.id.tv_remark_name, "field 'tvRemarkName'", TextView.class);
    target.llContainer = Utils.findRequiredView(source, R.id.ll_container, "field 'llContainer'");
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.ivAvatar = null;
    target.tvName = null;
    target.tvRemarkName = null;
    target.llContainer = null;

    this.target = null;
  }
}
