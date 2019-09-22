// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.user;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.ui.card.widget.LogoView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardOpenAuthenticFragment_ViewBinding<T extends CardOpenAuthenticFragment> implements Unbinder {
  protected T target;

  @UiThread
  public CardOpenAuthenticFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.ivIcon = Utils.findRequiredViewAsType(source, R.id.iv_icon, "field 'ivIcon'", SimpleDraweeView.class);
    target.btnApprove = Utils.findRequiredViewAsType(source, R.id.btn_approve, "field 'btnApprove'", TextView.class);
    target.tvMerName = Utils.findRequiredViewAsType(source, R.id.tv_merName, "field 'tvMerName'", TextView.class);
    target.tvCardOpenInfoName = Utils.findRequiredViewAsType(source, R.id.tv_card_open_info_name, "field 'tvCardOpenInfoName'", TextView.class);
    target.tvCardOpenInfoMobile = Utils.findRequiredViewAsType(source, R.id.tv_card_open_info_mobile, "field 'tvCardOpenInfoMobile'", TextView.class);
    target.tvCardOpenInfoID = Utils.findRequiredViewAsType(source, R.id.tv_card_open_info_ID, "field 'tvCardOpenInfoID'", TextView.class);
    target.actionBack = Utils.findRequiredViewAsType(source, R.id.action_back, "field 'actionBack'", ImageButton.class);
    target.actionBackLayout = Utils.findRequiredViewAsType(source, R.id.action_back_layout, "field 'actionBackLayout'", RelativeLayout.class);
    target.ivOcardState = Utils.findRequiredViewAsType(source, R.id.iv_ocard_state, "field 'ivOcardState'", LogoView.class);
    target.actionTitle = Utils.findRequiredViewAsType(source, R.id.action_title, "field 'actionTitle'", TextView.class);
    target.actionMenuButton = Utils.findRequiredViewAsType(source, R.id.action_menu_button, "field 'actionMenuButton'", ImageButton.class);
    target.actionMenuLayout = Utils.findRequiredViewAsType(source, R.id.action_menu_layout, "field 'actionMenuLayout'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.ivIcon = null;
    target.btnApprove = null;
    target.tvMerName = null;
    target.tvCardOpenInfoName = null;
    target.tvCardOpenInfoMobile = null;
    target.tvCardOpenInfoID = null;
    target.actionBack = null;
    target.actionBackLayout = null;
    target.ivOcardState = null;
    target.actionTitle = null;
    target.actionMenuButton = null;
    target.actionMenuLayout = null;

    this.target = null;
  }
}
