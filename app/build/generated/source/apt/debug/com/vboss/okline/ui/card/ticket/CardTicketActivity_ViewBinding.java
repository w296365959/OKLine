// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.card.ticket;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardTicketActivity_ViewBinding<T extends CardTicketActivity> implements Unbinder {
  protected T target;

  @UiThread
  public CardTicketActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.tv_pos_ticket_title = Utils.findRequiredViewAsType(source, R.id.tv_pos_ticket_title, "field 'tv_pos_ticket_title'", TextView.class);
    target.tv_merchant_name = Utils.findRequiredViewAsType(source, R.id.tv_merchant_name, "field 'tv_merchant_name'", TextView.class);
    target.tv_merchant_number = Utils.findRequiredViewAsType(source, R.id.tv_merchant_number, "field 'tv_merchant_number'", TextView.class);
    target.tv_terminal_number = Utils.findRequiredViewAsType(source, R.id.tv_terminal_number, "field 'tv_terminal_number'", TextView.class);
    target.tv_operator_number = Utils.findRequiredViewAsType(source, R.id.tv_operator_number, "field 'tv_operator_number'", TextView.class);
    target.tv_card_issuing_bank = Utils.findRequiredViewAsType(source, R.id.tv_card_issuing_bank, "field 'tv_card_issuing_bank'", TextView.class);
    target.tv_acquiring_bank = Utils.findRequiredViewAsType(source, R.id.tv_acquiring_bank, "field 'tv_acquiring_bank'", TextView.class);
    target.tv_trans_type = Utils.findRequiredViewAsType(source, R.id.tv_trans_type, "field 'tv_trans_type'", TextView.class);
    target.tv_card_num = Utils.findRequiredViewAsType(source, R.id.tv_card_num, "field 'tv_card_num'", TextView.class);
    target.tv_serial_number = Utils.findRequiredViewAsType(source, R.id.tv_serial_number, "field 'tv_serial_number'", TextView.class);
    target.tv_trans_date = Utils.findRequiredViewAsType(source, R.id.tv_trans_date, "field 'tv_trans_date'", TextView.class);
    target.tv_period_validity = Utils.findRequiredViewAsType(source, R.id.tv_period_validity, "field 'tv_period_validity'", TextView.class);
    target.tv_batch_number = Utils.findRequiredViewAsType(source, R.id.tv_batch_number, "field 'tv_batch_number'", TextView.class);
    target.tv_voucher_number = Utils.findRequiredViewAsType(source, R.id.tv_voucher_number, "field 'tv_voucher_number'", TextView.class);
    target.tv_authorization_number = Utils.findRequiredViewAsType(source, R.id.tv_authorization_number, "field 'tv_authorization_number'", TextView.class);
    target.tv_amount_money = Utils.findRequiredViewAsType(source, R.id.tv_amount_money, "field 'tv_amount_money'", TextView.class);
    target.tv_pos_remark = Utils.findRequiredViewAsType(source, R.id.tv_pos_remark, "field 'tv_pos_remark'", TextView.class);
    target.iv_signature = Utils.findRequiredViewAsType(source, R.id.iv_signature, "field 'iv_signature'", SimpleDraweeView.class);
    target.pos_line_1 = Utils.findRequiredView(source, R.id.pos_line_1, "field 'pos_line_1'");
    target.pos_line_2 = Utils.findRequiredView(source, R.id.pos_line_2, "field 'pos_line_2'");
    target.pos_line_3 = Utils.findRequiredView(source, R.id.pos_line_3, "field 'pos_line_3'");
    target.pos_line_4 = Utils.findRequiredView(source, R.id.pos_line_4, "field 'pos_line_4'");
    target.simpleDraweeView = Utils.findRequiredViewAsType(source, R.id.image_ticket, "field 'simpleDraweeView'", SimpleDraweeView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", FragmentToolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tv_pos_ticket_title = null;
    target.tv_merchant_name = null;
    target.tv_merchant_number = null;
    target.tv_terminal_number = null;
    target.tv_operator_number = null;
    target.tv_card_issuing_bank = null;
    target.tv_acquiring_bank = null;
    target.tv_trans_type = null;
    target.tv_card_num = null;
    target.tv_serial_number = null;
    target.tv_trans_date = null;
    target.tv_period_validity = null;
    target.tv_batch_number = null;
    target.tv_voucher_number = null;
    target.tv_authorization_number = null;
    target.tv_amount_money = null;
    target.tv_pos_remark = null;
    target.iv_signature = null;
    target.pos_line_1 = null;
    target.pos_line_2 = null;
    target.pos_line_3 = null;
    target.pos_line_4 = null;
    target.simpleDraweeView = null;
    target.toolbar = null;

    this.target = null;
  }
}
