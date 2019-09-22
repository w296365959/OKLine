// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.card.notice;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardNoticeFragment_ViewBinding<T extends CardNoticeFragment> implements Unbinder {
  protected T target;

  @UiThread
  public CardNoticeFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.tv_card_tip = Utils.findRequiredViewAsType(source, R.id.tv_pay_tip, "field 'tv_card_tip'", TextView.class);
    target.draweeView = Utils.findRequiredViewAsType(source, R.id.sdv_card_pay, "field 'draweeView'", SimpleDraweeView.class);
    target.tv_card_pay_tip = Utils.findRequiredViewAsType(source, R.id.tv_card_pay_tip, "field 'tv_card_pay_tip'", TextView.class);
    target.iv_card_logo = Utils.findRequiredViewAsType(source, R.id.iv_card_logo, "field 'iv_card_logo'", ImageView.class);
    target.layout_card_balance = Utils.findRequiredViewAsType(source, R.id.layout_card_member, "field 'layout_card_balance'", LinearLayout.class);
    target.tv_card_balance = Utils.findRequiredViewAsType(source, R.id.tv_card_member_balance, "field 'tv_card_balance'", TextView.class);
    target.tv_card_integral = Utils.findRequiredViewAsType(source, R.id.tv_card_member_integral, "field 'tv_card_integral'", TextView.class);
    target.layout_success = Utils.findRequiredViewAsType(source, R.id.layout_card_success, "field 'layout_success'", LinearLayout.class);
    target.tv_card_pay_money = Utils.findRequiredViewAsType(source, R.id.tv_card_pay_money, "field 'tv_card_pay_money'", TextView.class);
    target.tv_card_pay_discount = Utils.findRequiredViewAsType(source, R.id.tv_card_pay_discount, "field 'tv_card_pay_discount'", TextView.class);
    target.layout_card_ticket = Utils.findRequiredViewAsType(source, R.id.layout_card_ticket, "field 'layout_card_ticket'", LinearLayout.class);
    target.tv_card_pos = Utils.findRequiredViewAsType(source, R.id.tv_card_pay_pos, "field 'tv_card_pos'", TextView.class);
    target.tv_card_ticket = Utils.findRequiredViewAsType(source, R.id.tv_card_pay_ticket, "field 'tv_card_ticket'", TextView.class);
    target.tv_card_trade = Utils.findRequiredViewAsType(source, R.id.tv_card_trade, "field 'tv_card_trade'", TextView.class);
    target.layout_balance = Utils.findRequiredViewAsType(source, R.id.layout_card_balance, "field 'layout_balance'", LinearLayout.class);
    target.tv_recharge_1 = Utils.findRequiredViewAsType(source, R.id.tv_card_recharge_1, "field 'tv_recharge_1'", TextView.class);
    target.tv_recharge_2 = Utils.findRequiredViewAsType(source, R.id.tv_card_recharge_2, "field 'tv_recharge_2'", TextView.class);
    target.tv_recharge_3 = Utils.findRequiredViewAsType(source, R.id.tv_card_recharge_3, "field 'tv_recharge_3'", TextView.class);
    target.tv_no_recharge = Utils.findRequiredViewAsType(source, R.id.tv_card_no_recharge, "field 'tv_no_recharge'", TextView.class);
    target.tv_now_recharge = Utils.findRequiredViewAsType(source, R.id.tv_card_now_recharge, "field 'tv_now_recharge'", TextView.class);
    target.tv_no_enough_balance = Utils.findRequiredViewAsType(source, R.id.tv_card_member_no_balance, "field 'tv_no_enough_balance'", TextView.class);
    target.layout_back = Utils.findRequiredViewAsType(source, R.id.iv_card_pay_close, "field 'layout_back'", ImageView.class);
    target.tv_card_no = Utils.findRequiredViewAsType(source, R.id.tv_card_no, "field 'tv_card_no'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tv_card_tip = null;
    target.draweeView = null;
    target.tv_card_pay_tip = null;
    target.iv_card_logo = null;
    target.layout_card_balance = null;
    target.tv_card_balance = null;
    target.tv_card_integral = null;
    target.layout_success = null;
    target.tv_card_pay_money = null;
    target.tv_card_pay_discount = null;
    target.layout_card_ticket = null;
    target.tv_card_pos = null;
    target.tv_card_ticket = null;
    target.tv_card_trade = null;
    target.layout_balance = null;
    target.tv_recharge_1 = null;
    target.tv_recharge_2 = null;
    target.tv_recharge_3 = null;
    target.tv_no_recharge = null;
    target.tv_now_recharge = null;
    target.tv_no_enough_balance = null;
    target.layout_back = null;
    target.tv_card_no = null;

    this.target = null;
  }
}
