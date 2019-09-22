// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.view.widget.OKCardView;
import com.vboss.okline.view.widget.SideBar;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ContactsFragment_ViewBinding<T extends ContactsFragment> implements Unbinder {
  protected T target;

  @UiThread
  public ContactsFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.lvContacts = Utils.findRequiredViewAsType(source, R.id.lv_contacts, "field 'lvContacts'", ListView.class);
    target.dialog = Utils.findRequiredViewAsType(source, R.id.dialog, "field 'dialog'", TextView.class);
    target.sidebar = Utils.findRequiredViewAsType(source, R.id.sidebar, "field 'sidebar'", SideBar.class);
    target.pbAddContacts = Utils.findRequiredViewAsType(source, R.id.pb_add_contacts, "field 'pbAddContacts'", ProgressBar.class);
    target.ivContactAvatar = Utils.findRequiredViewAsType(source, R.id.iv_contact_avatar, "field 'ivContactAvatar'", SimpleDraweeView.class);
    target.tvMe = Utils.findRequiredViewAsType(source, R.id.tv_me, "field 'tvMe'", TextView.class);
    target.tvMy = Utils.findRequiredViewAsType(source, R.id.tv_my, "field 'tvMy'", TextView.class);
    target.rlHeadview = Utils.findRequiredViewAsType(source, R.id.rl_headview, "field 'rlHeadview'", RelativeLayout.class);
    target.transPtrFrameLayout = Utils.findRequiredViewAsType(source, R.id.trans_ptrFrameLayout, "field 'transPtrFrameLayout'", PtrClassicFrameLayout.class);
    target.nonNewFriend = Utils.findRequiredViewAsType(source, R.id.non_new_friend, "field 'nonNewFriend'", TextView.class);
    target.tvCompleteInformation = Utils.findRequiredViewAsType(source, R.id.tv_complete_information, "field 'tvCompleteInformation'", TextView.class);
    target.btnReceivables = Utils.findRequiredViewAsType(source, R.id.btn_receivables, "field 'btnReceivables'", ImageButton.class);
    target.ibDropAdd = Utils.findRequiredViewAsType(source, R.id.ib_drop_add, "field 'ibDropAdd'", ImageButton.class);
    target.actionTitle = Utils.findRequiredViewAsType(source, R.id.action_title, "field 'actionTitle'", TextView.class);
    target.actionMenuButton = Utils.findRequiredViewAsType(source, R.id.action_menu_button, "field 'actionMenuButton'", ImageButton.class);
    target.logoView = Utils.findRequiredViewAsType(source, R.id.logoView, "field 'logoView'", LogoView.class);
    target.okCardView = Utils.findRequiredViewAsType(source, R.id.ocardView, "field 'okCardView'", OKCardView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.lvContacts = null;
    target.dialog = null;
    target.sidebar = null;
    target.pbAddContacts = null;
    target.ivContactAvatar = null;
    target.tvMe = null;
    target.tvMy = null;
    target.rlHeadview = null;
    target.transPtrFrameLayout = null;
    target.nonNewFriend = null;
    target.tvCompleteInformation = null;
    target.btnReceivables = null;
    target.ibDropAdd = null;
    target.actionTitle = null;
    target.actionMenuButton = null;
    target.logoView = null;
    target.okCardView = null;

    this.target = null;
  }
}
