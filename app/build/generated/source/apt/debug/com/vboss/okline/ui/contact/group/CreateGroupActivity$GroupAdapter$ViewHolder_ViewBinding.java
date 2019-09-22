// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.group;

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

public class CreateGroupActivity$GroupAdapter$ViewHolder_ViewBinding<T extends CreateGroupActivity.GroupAdapter.ViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public CreateGroupActivity$GroupAdapter$ViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.tvCatalog = Utils.findRequiredViewAsType(source, R.id.tv_catalog, "field 'tvCatalog'", TextView.class);
    target.itemUnderline = Utils.findRequiredView(source, R.id.item_underline, "field 'itemUnderline'");
    target.avatar = Utils.findRequiredViewAsType(source, R.id.iv_contact_avatar, "field 'avatar'", SimpleDraweeView.class);
    target.ivContactIscheck = Utils.findRequiredViewAsType(source, R.id.iv_contact_ischeck, "field 'ivContactIscheck'", ImageView.class);
    target.tvUserName = Utils.findRequiredViewAsType(source, R.id.tv_contact_name, "field 'tvUserName'", TextView.class);
    target.tvRealName = Utils.findRequiredViewAsType(source, R.id.tv_contact_real_name, "field 'tvRealName'", TextView.class);
    target.tvGroupName = Utils.findRequiredViewAsType(source, R.id.tv_group_name, "field 'tvGroupName'", TextView.class);
    target.checkBox = Utils.findRequiredViewAsType(source, R.id.checkbox_item, "field 'checkBox'", ImageView.class);
    target.bottomLine = Utils.findRequiredView(source, R.id.item_bottom_line, "field 'bottomLine'");
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvCatalog = null;
    target.itemUnderline = null;
    target.avatar = null;
    target.ivContactIscheck = null;
    target.tvUserName = null;
    target.tvRealName = null;
    target.tvGroupName = null;
    target.checkBox = null;
    target.bottomLine = null;

    this.target = null;
  }
}
