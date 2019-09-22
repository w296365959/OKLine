// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.user;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.view.widget.PaymentLauchLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardOpenRechargeFragment_ViewBinding<T extends CardOpenRechargeFragment> implements Unbinder {
  protected T target;

  @UiThread
  public CardOpenRechargeFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.sdvCard = Utils.findRequiredViewAsType(source, R.id.sdv_card, "field 'sdvCard'", SimpleDraweeView.class);
    target.tvCardName = Utils.findRequiredViewAsType(source, R.id.tv_card_name, "field 'tvCardName'", TextView.class);
    target.tvCardNumber = Utils.findRequiredViewAsType(source, R.id.tv_card_number, "field 'tvCardNumber'", TextView.class);
    target.tvCardOpenCost = Utils.findRequiredViewAsType(source, R.id.tv_card_open_cost, "field 'tvCardOpenCost'", TextView.class);
    target.pllBankcards = Utils.findRequiredViewAsType(source, R.id.pll_bankcards, "field 'pllBankcards'", PaymentLauchLayout.class);
    target.tvCardPayOpen = Utils.findRequiredViewAsType(source, R.id.tv_card_pay_open, "field 'tvCardPayOpen'", TextView.class);
    target.actionBack = Utils.findRequiredViewAsType(source, R.id.action_back, "field 'actionBack'", ImageButton.class);
    target.actionBackLayout = Utils.findRequiredViewAsType(source, R.id.action_back_layout, "field 'actionBackLayout'", RelativeLayout.class);
    target.ivOcardState = Utils.findRequiredViewAsType(source, R.id.iv_ocard_state, "field 'ivOcardState'", ImageView.class);
    target.actionTitle = Utils.findRequiredViewAsType(source, R.id.action_title, "field 'actionTitle'", TextView.class);
    target.actionMenuButton = Utils.findRequiredViewAsType(source, R.id.action_menu_button, "field 'actionMenuButton'", ImageButton.class);
    target.actionMenuLayout = Utils.findRequiredViewAsType(source, R.id.action_menu_layout, "field 'actionMenuLayout'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.sdvCard = null;
    target.tvCardName = null;
    target.tvCardNumber = null;
    target.tvCardOpenCost = null;
    target.pllBankcards = null;
    target.tvCardPayOpen = null;
    target.actionBack = null;
    target.actionBackLayout = null;
    target.ivOcardState = null;
    target.actionTitle = null;
    target.actionMenuButton = null;
    target.actionMenuLayout = null;

    this.target = null;
  }
}
