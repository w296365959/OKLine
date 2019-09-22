// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NewFriendAdapter$ViewHolder_ViewBinding<T extends NewFriendAdapter.ViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public NewFriendAdapter$ViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.itemUnderline = Utils.findRequiredViewAsType(source, R.id.item_underline, "field 'itemUnderline'", TextView.class);
    target.ivContactAvatar = Utils.findRequiredViewAsType(source, R.id.iv_contact_avatar, "field 'ivContactAvatar'", SimpleDraweeView.class);
    target.ivContactIscheck = Utils.findRequiredViewAsType(source, R.id.iv_contact_ischeck, "field 'ivContactIscheck'", ImageView.class);
    target.tvContactName = Utils.findRequiredViewAsType(source, R.id.tv_contact_name, "field 'tvContactName'", TextView.class);
    target.tvPhone = Utils.findRequiredViewAsType(source, R.id.tv_phone, "field 'tvPhone'", TextView.class);
    target.tvGroupName = Utils.findRequiredViewAsType(source, R.id.tv_group_name, "field 'tvGroupName'", TextView.class);
    target.tvAddNew = Utils.findRequiredViewAsType(source, R.id.tv_add_new, "field 'tvAddNew'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.itemUnderline = null;
    target.ivContactAvatar = null;
    target.ivContactIscheck = null;
    target.tvContactName = null;
    target.tvPhone = null;
    target.tvGroupName = null;
    target.tvAddNew = null;

    this.target = null;
  }
}
