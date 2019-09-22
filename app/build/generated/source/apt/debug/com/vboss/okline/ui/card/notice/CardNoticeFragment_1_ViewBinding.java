// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.card.notice;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardNoticeFragment_1_ViewBinding<T extends CardNoticeFragment_1> implements Unbinder {
  protected T target;

  private View view2131755996;

  private View view2131755999;

  private View view2131756000;

  private View view2131756001;

  private View view2131755207;

  private View view2131755206;

  @UiThread
  public CardNoticeFragment_1_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.tv_amount = Utils.findRequiredViewAsType(source, R.id.tv_amount, "field 'tv_amount'", TextView.class);
    target.tv_discount = Utils.findRequiredViewAsType(source, R.id.tv_discount, "field 'tv_discount'", TextView.class);
    target.iv_state_logo = Utils.findRequiredViewAsType(source, R.id.iv_card_notice_state, "field 'iv_state_logo'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.simple_card_image, "field 'simple_card_image' and method 'onCardImage'");
    target.simple_card_image = Utils.castView(view, R.id.simple_card_image, "field 'simple_card_image'", SimpleDraweeView.class);
    view2131755996 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onCardImage(p0);
      }
    });
    target.tv_bus_card_balance = Utils.findRequiredViewAsType(source, R.id.tv_bus_card_balance, "field 'tv_bus_card_balance'", TextView.class);
    target.layout_print = Utils.findRequiredViewAsType(source, R.id.layout_print, "field 'layout_print'", RelativeLayout.class);
    view = Utils.findRequiredView(source, R.id.ib_print_pos, "field 'ib_print_pos' and method 'onPrint'");
    target.ib_print_pos = Utils.castView(view, R.id.ib_print_pos, "field 'ib_print_pos'", ImageButton.class);
    view2131755999 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPrint(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ib_print_ticket, "field 'ib_print_ticket' and method 'onPrint'");
    target.ib_print_ticket = Utils.castView(view, R.id.ib_print_ticket, "field 'ib_print_ticket'", ImageButton.class);
    view2131756000 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPrint(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ib_print_invoice, "field 'ib_print_invoice' and method 'onPrint'");
    target.ib_print_invoice = Utils.castView(view, R.id.ib_print_invoice, "field 'ib_print_invoice'", ImageButton.class);
    view2131756001 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPrint(p0);
      }
    });
    target.layout_no_enough_balance = Utils.findRequiredViewAsType(source, R.id.layout_no_enough_layout, "field 'layout_no_enough_balance'", LinearLayout.class);
    target.layout_member_pay_fail = Utils.findRequiredViewAsType(source, R.id.layout_member_pay_fail, "field 'layout_member_pay_fail'", LinearLayout.class);
    target.tv_card_member_balance = Utils.findRequiredViewAsType(source, R.id.tv_card_member_balance, "field 'tv_card_member_balance'", TextView.class);
    target.tv_card_member_integral = Utils.findRequiredViewAsType(source, R.id.tv_card_member_integral, "field 'tv_card_member_integral'", TextView.class);
    target.tv_exit = Utils.findRequiredViewAsType(source, R.id.tv_exit, "field 'tv_exit'", TextView.class);
    view = Utils.findRequiredView(source, R.id.tv_card_no_recharge, "method 'onRecharge'");
    view2131755207 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onRecharge(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_card_now_recharge, "method 'onRecharge'");
    view2131755206 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onRecharge(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tv_amount = null;
    target.tv_discount = null;
    target.iv_state_logo = null;
    target.simple_card_image = null;
    target.tv_bus_card_balance = null;
    target.layout_print = null;
    target.ib_print_pos = null;
    target.ib_print_ticket = null;
    target.ib_print_invoice = null;
    target.layout_no_enough_balance = null;
    target.layout_member_pay_fail = null;
    target.tv_card_member_balance = null;
    target.tv_card_member_integral = null;
    target.tv_exit = null;

    view2131755996.setOnClickListener(null);
    view2131755996 = null;
    view2131755999.setOnClickListener(null);
    view2131755999 = null;
    view2131756000.setOnClickListener(null);
    view2131756000 = null;
    view2131756001.setOnClickListener(null);
    view2131756001 = null;
    view2131755207.setOnClickListener(null);
    view2131755207 = null;
    view2131755206.setOnClickListener(null);
    view2131755206 = null;

    this.target = null;
  }
}
