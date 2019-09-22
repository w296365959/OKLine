// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.record.person;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PersonAdapter$PersonViewHolder_ViewBinding<T extends PersonAdapter.PersonViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public PersonAdapter$PersonViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.avatar = Utils.findRequiredViewAsType(source, R.id.avatar, "field 'avatar'", SimpleDraweeView.class);
    target.ivOlVip = Utils.findRequiredViewAsType(source, R.id.iv_ol_vip, "field 'ivOlVip'", ImageView.class);
    target.avatarContainer = Utils.findRequiredViewAsType(source, R.id.avatar_container, "field 'avatarContainer'", RelativeLayout.class);
    target.name = Utils.findRequiredViewAsType(source, R.id.name, "field 'name'", TextView.class);
    target.remark = Utils.findRequiredViewAsType(source, R.id.remark, "field 'remark'", TextView.class);
    target.phone = Utils.findRequiredViewAsType(source, R.id.phone, "field 'phone'", TextView.class);
    target.unreadMsgNumber = Utils.findRequiredViewAsType(source, R.id.unread_msg_number, "field 'unreadMsgNumber'", TextView.class);
    target.msgState = Utils.findRequiredViewAsType(source, R.id.msg_state, "field 'msgState'", ImageView.class);
    target.messageIcon = Utils.findRequiredViewAsType(source, R.id.message_icon, "field 'messageIcon'", ImageView.class);
    target.message = Utils.findRequiredViewAsType(source, R.id.message, "field 'message'", TextView.class);
    target.time = Utils.findRequiredViewAsType(source, R.id.time, "field 'time'", TextView.class);
    target.container = Utils.findRequiredView(source, R.id.container, "field 'container'");
    target.llViewName = Utils.findRequiredView(source, R.id.ll_view_name, "field 'llViewName'");
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.avatar = null;
    target.ivOlVip = null;
    target.avatarContainer = null;
    target.name = null;
    target.remark = null;
    target.phone = null;
    target.unreadMsgNumber = null;
    target.msgState = null;
    target.messageIcon = null;
    target.message = null;
    target.time = null;
    target.container = null;
    target.llViewName = null;

    this.target = null;
  }
}
