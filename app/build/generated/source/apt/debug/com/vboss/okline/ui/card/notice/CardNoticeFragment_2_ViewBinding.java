// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.card.notice;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardNoticeFragment_2_ViewBinding<T extends CardNoticeFragment_2> implements Unbinder {
  protected T target;

  private View view2131756001;

  private View view2131756000;

  private View view2131755999;

  private View view2131756012;

  private View view2131756015;

  private View view2131756013;

  @UiThread
  public CardNoticeFragment_2_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.layout_amount = Utils.findRequiredViewAsType(source, R.id.layout_amount, "field 'layout_amount'", LinearLayout.class);
    target.tv_amount = Utils.findRequiredViewAsType(source, R.id.tv_amount, "field 'tv_amount'", TextView.class);
    target.tv_balance = Utils.findRequiredViewAsType(source, R.id.tv_balance, "field 'tv_balance'", TextView.class);
    target.layout_vip_amount = Utils.findRequiredViewAsType(source, R.id.layout_vip_amount, "field 'layout_vip_amount'", LinearLayout.class);
    target.tv_vip_consume_amount = Utils.findRequiredViewAsType(source, R.id.tv_vip_amount, "field 'tv_vip_consume_amount'", TextView.class);
    target.tv_vip_consume_integral = Utils.findRequiredViewAsType(source, R.id.tv_vip_integral, "field 'tv_vip_consume_integral'", TextView.class);
    target.tv_vip_balance = Utils.findRequiredViewAsType(source, R.id.tv_vip_balance, "field 'tv_vip_balance'", TextView.class);
    target.tv_vip_integral = Utils.findRequiredViewAsType(source, R.id.tv_vip_remainder_integral, "field 'tv_vip_integral'", TextView.class);
    target.simple_card_image = Utils.findRequiredViewAsType(source, R.id.simple_card_image, "field 'simple_card_image'", SimpleDraweeView.class);
    target.iv_card_notice_state = Utils.findRequiredViewAsType(source, R.id.iv_card_notice_state, "field 'iv_card_notice_state'", ImageView.class);
    target.layout_print = Utils.findRequiredViewAsType(source, R.id.layout_print, "field 'layout_print'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.ib_print_invoice, "field 'ib_print_invoice' and method 'onViewClick'");
    target.ib_print_invoice = Utils.castView(view, R.id.ib_print_invoice, "field 'ib_print_invoice'", ImageButton.class);
    view2131756001 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ib_print_ticket, "field 'ib_print_ticket' and method 'onViewClick'");
    target.ib_print_ticket = Utils.castView(view, R.id.ib_print_ticket, "field 'ib_print_ticket'", ImageButton.class);
    view2131756000 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ib_print_pos, "field 'ib_print_pos' and method 'onViewClick'");
    target.ib_print_pos = Utils.castView(view, R.id.ib_print_pos, "field 'ib_print_pos'", ImageButton.class);
    view2131755999 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ib_voice, "field 'ib_voice' and method 'onViewClick'");
    target.ib_voice = Utils.castView(view, R.id.ib_voice, "field 'ib_voice'", ImageButton.class);
    view2131756012 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_give_up, "field 'tv_give_up' and method 'onViewClick'");
    target.tv_give_up = Utils.castView(view, R.id.tv_give_up, "field 'tv_give_up'", TextView.class);
    view2131756015 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_ensure, "field 'tv_ensure' and method 'onViewClick'");
    target.tv_ensure = Utils.castView(view, R.id.tv_ensure, "field 'tv_ensure'", TextView.class);
    view2131756013 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClick(p0);
      }
    });
    target.tv_no_balance = Utils.findRequiredViewAsType(source, R.id.tv_no_balance, "field 'tv_no_balance'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.layout_amount = null;
    target.tv_amount = null;
    target.tv_balance = null;
    target.layout_vip_amount = null;
    target.tv_vip_consume_amount = null;
    target.tv_vip_consume_integral = null;
    target.tv_vip_balance = null;
    target.tv_vip_integral = null;
    target.simple_card_image = null;
    target.iv_card_notice_state = null;
    target.layout_print = null;
    target.ib_print_invoice = null;
    target.ib_print_ticket = null;
    target.ib_print_pos = null;
    target.ib_voice = null;
    target.tv_give_up = null;
    target.tv_ensure = null;
    target.tv_no_balance = null;

    view2131756001.setOnClickListener(null);
    view2131756001 = null;
    view2131756000.setOnClickListener(null);
    view2131756000 = null;
    view2131755999.setOnClickListener(null);
    view2131755999 = null;
    view2131756012.setOnClickListener(null);
    view2131756012 = null;
    view2131756015.setOnClickListener(null);
    view2131756015 = null;
    view2131756013.setOnClickListener(null);
    view2131756013 = null;

    this.target = null;
  }
}
