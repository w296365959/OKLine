// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.express.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

public class ToBeConfirmFragment_ViewBinding<T extends ToBeConfirmFragment> implements Unbinder {
  protected T target;

  private View view2131755838;

  private View view2131755839;

  @UiThread
  public ToBeConfirmFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.expressCompanyRv = Utils.findRequiredViewAsType(source, R.id.express_company_rv, "field 'expressCompanyRv'", RecyclerView.class);
    target.expressNullCompanyTv = Utils.findRequiredViewAsType(source, R.id.express_null_company_tv, "field 'expressNullCompanyTv'", TextView.class);
    target.sendMessageTextView = Utils.findRequiredViewAsType(source, R.id.send_message_textView, "field 'sendMessageTextView'", TextView.class);
    view = Utils.findRequiredView(source, R.id.express_send_message_kd_tv, "field 'expressSendMessageKdTv' and method 'onClick'");
    target.expressSendMessageKdTv = Utils.castView(view, R.id.express_send_message_kd_tv, "field 'expressSendMessageKdTv'", TextView.class);
    view2131755838 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.express_send_message_kd_iv, "field 'expressSendMessageKdIv' and method 'onClick'");
    target.expressSendMessageKdIv = Utils.castView(view, R.id.express_send_message_kd_iv, "field 'expressSendMessageKdIv'", ImageView.class);
    view2131755839 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.expressToBeConfirmedNewTextView = Utils.findRequiredViewAsType(source, R.id.express_to_be_confirmed_new_textView, "field 'expressToBeConfirmedNewTextView'", TextView.class);
    target.expressOnTheWayKdRv = Utils.findRequiredViewAsType(source, R.id.express_onTheWay_kd_rv, "field 'expressOnTheWayKdRv'", RecyclerView.class);
    target.expressOnTheWayKdLl = Utils.findRequiredViewAsType(source, R.id.express_onTheWay_kd_ll, "field 'expressOnTheWayKdLl'", LinearLayout.class);
    target.expressSendUserImg = Utils.findRequiredViewAsType(source, R.id.express_send_user_img, "field 'expressSendUserImg'", SimpleDraweeView.class);
    target.expressSendNameTv = Utils.findRequiredViewAsType(source, R.id.express_send_name_tv, "field 'expressSendNameTv'", TextView.class);
    target.expressSendNumberTv = Utils.findRequiredViewAsType(source, R.id.express_send_number_tv, "field 'expressSendNumberTv'", TextView.class);
    target.expressSendMessageUserLl = Utils.findRequiredViewAsType(source, R.id.express_send_message_user_ll, "field 'expressSendMessageUserLl'", LinearLayout.class);
    target.expressNameOfGoodsTv = Utils.findRequiredViewAsType(source, R.id.express_name_of_goods_tv, "field 'expressNameOfGoodsTv'", TextView.class);
    target.expressWeightOfGoodsTv = Utils.findRequiredViewAsType(source, R.id.express_weight_of_goods_tv, "field 'expressWeightOfGoodsTv'", TextView.class);
    target.expressCargoMessageLl = Utils.findRequiredViewAsType(source, R.id.express_cargo_message_ll, "field 'expressCargoMessageLl'", LinearLayout.class);
    target.expressSendAddressTv = Utils.findRequiredViewAsType(source, R.id.express_send_address_tv, "field 'expressSendAddressTv'", TextView.class);
    target.expressUpdateSendAddressTv = Utils.findRequiredViewAsType(source, R.id.express_update_send_address_tv, "field 'expressUpdateSendAddressTv'", TextView.class);
    target.itemExpressUpdateAddressLl = Utils.findRequiredViewAsType(source, R.id.item_express_update_address_ll, "field 'itemExpressUpdateAddressLl'", LinearLayout.class);
    target.expressSendMessageLl = Utils.findRequiredViewAsType(source, R.id.express_send_message_ll, "field 'expressSendMessageLl'", LinearLayout.class);
    target.acceptMessageTextView = Utils.findRequiredViewAsType(source, R.id.accept_message_textView, "field 'acceptMessageTextView'", TextView.class);
    target.expressAcceptUserImg = Utils.findRequiredViewAsType(source, R.id.express_accept_user_img, "field 'expressAcceptUserImg'", SimpleDraweeView.class);
    target.expressAcceptNameTv = Utils.findRequiredViewAsType(source, R.id.express_accept_name_tv, "field 'expressAcceptNameTv'", TextView.class);
    target.expressAcceptNumberTv = Utils.findRequiredViewAsType(source, R.id.express_accept_number_tv, "field 'expressAcceptNumberTv'", TextView.class);
    target.expressAcceptAddressTv = Utils.findRequiredViewAsType(source, R.id.express_accept_address_tv, "field 'expressAcceptAddressTv'", TextView.class);
    target.expressUpdateAcceptAddressTv = Utils.findRequiredViewAsType(source, R.id.express_update_accept_address_tv, "field 'expressUpdateAcceptAddressTv'", TextView.class);
    target.expressOtherConfirmedAddressTv = Utils.findRequiredViewAsType(source, R.id.express_other_confirmed_address_tv, "field 'expressOtherConfirmedAddressTv'", TextView.class);
    target.expressAcceptMessageComfirmedIv = Utils.findRequiredViewAsType(source, R.id.express_accept_message_comfirmed_iv, "field 'expressAcceptMessageComfirmedIv'", ImageView.class);
    target.expressMessageView = Utils.findRequiredView(source, R.id.express_message_view, "field 'expressMessageView'");
    target.expressMsgAcceptLl = Utils.findRequiredViewAsType(source, R.id.express_msg_accept_ll, "field 'expressMsgAcceptLl'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.expressCompanyRv = null;
    target.expressNullCompanyTv = null;
    target.sendMessageTextView = null;
    target.expressSendMessageKdTv = null;
    target.expressSendMessageKdIv = null;
    target.expressToBeConfirmedNewTextView = null;
    target.expressOnTheWayKdRv = null;
    target.expressOnTheWayKdLl = null;
    target.expressSendUserImg = null;
    target.expressSendNameTv = null;
    target.expressSendNumberTv = null;
    target.expressSendMessageUserLl = null;
    target.expressNameOfGoodsTv = null;
    target.expressWeightOfGoodsTv = null;
    target.expressCargoMessageLl = null;
    target.expressSendAddressTv = null;
    target.expressUpdateSendAddressTv = null;
    target.itemExpressUpdateAddressLl = null;
    target.expressSendMessageLl = null;
    target.acceptMessageTextView = null;
    target.expressAcceptUserImg = null;
    target.expressAcceptNameTv = null;
    target.expressAcceptNumberTv = null;
    target.expressAcceptAddressTv = null;
    target.expressUpdateAcceptAddressTv = null;
    target.expressOtherConfirmedAddressTv = null;
    target.expressAcceptMessageComfirmedIv = null;
    target.expressMessageView = null;
    target.expressMsgAcceptLl = null;

    view2131755838.setOnClickListener(null);
    view2131755838 = null;
    view2131755839.setOnClickListener(null);
    view2131755839 = null;

    this.target = null;
  }
}
