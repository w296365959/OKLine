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

public class ContactListAdapter$ViewHolder_ViewBinding<T extends ContactListAdapter.ViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public ContactListAdapter$ViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.tvCatalog = Utils.findRequiredViewAsType(source, R.id.tv_catalog, "field 'tvCatalog'", TextView.class);
    target.itemUnderline = Utils.findRequiredView(source, R.id.item_underline, "field 'itemUnderline'");
    target.checkboxItem = Utils.findRequiredViewAsType(source, R.id.checkbox_item, "field 'checkboxItem'", ImageView.class);
    target.ivContactAvatar = Utils.findRequiredViewAsType(source, R.id.iv_contact_avatar, "field 'ivContactAvatar'", SimpleDraweeView.class);
    target.ivContactIscheck = Utils.findRequiredViewAsType(source, R.id.iv_contact_ischeck, "field 'ivContactIscheck'", ImageView.class);
    target.tvContactName = Utils.findRequiredViewAsType(source, R.id.tv_contact_name, "field 'tvContactName'", TextView.class);
    target.tvContactRealName = Utils.findRequiredViewAsType(source, R.id.tv_contact_real_name, "field 'tvContactRealName'", TextView.class);
    target.tvGroupName = Utils.findRequiredViewAsType(source, R.id.tv_group_name, "field 'tvGroupName'", TextView.class);
    target.bottomLine = Utils.findRequiredView(source, R.id.item_bottom_line, "field 'bottomLine'");
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvCatalog = null;
    target.itemUnderline = null;
    target.checkboxItem = null;
    target.ivContactAvatar = null;
    target.ivContactIscheck = null;
    target.tvContactName = null;
    target.tvContactRealName = null;
    target.tvGroupName = null;
    target.bottomLine = null;

    this.target = null;
  }
}
