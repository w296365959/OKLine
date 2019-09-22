// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.TransferAccounts;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.view.widget.SideBar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SelectBankActivity_ViewBinding<T extends SelectBankActivity> implements Unbinder {
  protected T target;

  @UiThread
  public SelectBankActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.bank_listview = Utils.findRequiredViewAsType(source, R.id.bank_listview, "field 'bank_listview'", ListView.class);
    target.dialog = Utils.findRequiredViewAsType(source, R.id.select_bank_dialog, "field 'dialog'", TextView.class);
    target.sidebar = Utils.findRequiredViewAsType(source, R.id.select_bank_sidebar, "field 'sidebar'", SideBar.class);
    target.actionLogo = Utils.findRequiredViewAsType(source, R.id.sdv_logo, "field 'actionLogo'", SimpleDraweeView.class);
    target.actionTitle = Utils.findRequiredViewAsType(source, R.id.action_title, "field 'actionTitle'", TextView.class);
    target.action_menu_layout = Utils.findRequiredViewAsType(source, R.id.action_menu_layout, "field 'action_menu_layout'", RelativeLayout.class);
    target.action_back_layout = Utils.findRequiredViewAsType(source, R.id.action_back_layout, "field 'action_back_layout'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.bank_listview = null;
    target.dialog = null;
    target.sidebar = null;
    target.actionLogo = null;
    target.actionTitle = null;
    target.action_menu_layout = null;
    target.action_back_layout = null;

    this.target = null;
  }
}
