// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.express.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
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

public class CompletedFragment_ViewBinding<T extends CompletedFragment> implements Unbinder {
  protected T target;

  private View view2131755838;

  private View view2131755839;

  @UiThread
  public CompletedFragment_ViewBinding(final T target, View source) {
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
    target.expressEditEditSendUserImg = Utils.findRequiredViewAsType(source, R.id.express_edit_edit_send_user_img, "field 'expressEditEditSendUserImg'", SimpleDraweeView.class);
    target.expressEditSendNameTv = Utils.findRequiredViewAsType(source, R.id.express_edit_send_name_tv, "field 'expressEditSendNameTv'", TextView.class);
    target.expressEditSendNumberTv = Utils.findRequiredViewAsType(source, R.id.express_edit_send_number_tv, "field 'expressEditSendNumberTv'", TextView.class);
    target.expressEditSendMessageUserLl = Utils.findRequiredViewAsType(source, R.id.express_edit_send_message_user_ll, "field 'expressEditSendMessageUserLl'", LinearLayout.class);
    target.expressOnTheWayEditKdRv = Utils.findRequiredViewAsType(source, R.id.express_onTheWay_edit_kd_rv, "field 'expressOnTheWayEditKdRv'", RecyclerView.class);
    target.expressOnTheWayEditKdLl = Utils.findRequiredViewAsType(source, R.id.express_onTheWay_edit_kd_ll, "field 'expressOnTheWayEditKdLl'", LinearLayout.class);
    target.expressNameOfGoodsEt = Utils.findRequiredViewAsType(source, R.id.express_name_of_goods_et, "field 'expressNameOfGoodsEt'", EditText.class);
    target.expressWeightOfGoodsEt = Utils.findRequiredViewAsType(source, R.id.express_weight_of_goods_et, "field 'expressWeightOfGoodsEt'", EditText.class);
    target.expressCargoMessageEditLl = Utils.findRequiredViewAsType(source, R.id.express_cargo_message_edit_ll, "field 'expressCargoMessageEditLl'", LinearLayout.class);
    target.expressEditSendAddressTv = Utils.findRequiredViewAsType(source, R.id.express_edit_send_address_tv, "field 'expressEditSendAddressTv'", TextView.class);
    target.expressEditUpdateSendAddressTv = Utils.findRequiredViewAsType(source, R.id.express_edit_update_send_address_tv, "field 'expressEditUpdateSendAddressTv'", TextView.class);
    target.itemExpressEditUpdateAddressLl = Utils.findRequiredViewAsType(source, R.id.item_express_edit_update_address_ll, "field 'itemExpressEditUpdateAddressLl'", LinearLayout.class);
    target.expressSendMessageEditLl = Utils.findRequiredViewAsType(source, R.id.express_send_message_edit_ll, "field 'expressSendMessageEditLl'", LinearLayout.class);
    target.acceptMessageTextView = Utils.findRequiredViewAsType(source, R.id.accept_message_textView, "field 'acceptMessageTextView'", TextView.class);
    target.expressAcceptUserEditImg = Utils.findRequiredViewAsType(source, R.id.express_accept_user_edit_img, "field 'expressAcceptUserEditImg'", SimpleDraweeView.class);
    target.expressAcceptNameEditTv = Utils.findRequiredViewAsType(source, R.id.express_accept_name_edit_tv, "field 'expressAcceptNameEditTv'", TextView.class);
    target.expressAcceptNumberEditTv = Utils.findRequiredViewAsType(source, R.id.express_accept_number_edit_tv, "field 'expressAcceptNumberEditTv'", TextView.class);
    target.expressAcceptAddressEditTv = Utils.findRequiredViewAsType(source, R.id.express_accept_address_edit_tv, "field 'expressAcceptAddressEditTv'", TextView.class);
    target.expressMessageView = Utils.findRequiredView(source, R.id.express_message_view, "field 'expressMessageView'");
    target.expressOnTheWaySendMessageEditLl = Utils.findRequiredViewAsType(source, R.id.express_onTheWay_send_message_edit_ll, "field 'expressOnTheWaySendMessageEditLl'", LinearLayout.class);
    target.expressOddNumbersTv = Utils.findRequiredViewAsType(source, R.id.express_odd_numbers_tv, "field 'expressOddNumbersTv'", TextView.class);
    target.expressAddressNameTv = Utils.findRequiredViewAsType(source, R.id.express_address_name_tv, "field 'expressAddressNameTv'", TextView.class);
    target.expressRecordRv = Utils.findRequiredViewAsType(source, R.id.express_record_rv, "field 'expressRecordRv'", RecyclerView.class);
    target.expressNullRecordTv = Utils.findRequiredViewAsType(source, R.id.express_null_record_tv, "field 'expressNullRecordTv'", TextView.class);
    target.textRecordDateTitle = Utils.findRequiredViewAsType(source, R.id.text_record_date_title, "field 'textRecordDateTitle'", TextView.class);
    target.arrowRecordDateTitleIv = Utils.findRequiredViewAsType(source, R.id.arrow_record_date_title_iv, "field 'arrowRecordDateTitleIv'", ImageView.class);
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
    target.textAcceptDateTitle = Utils.findRequiredViewAsType(source, R.id.text_accept_date_title, "field 'textAcceptDateTitle'", TextView.class);
    target.arrowAcceptDateTitleIv = Utils.findRequiredViewAsType(source, R.id.arrow_accept_date_title_iv, "field 'arrowAcceptDateTitleIv'", ImageView.class);
    target.expressAcceptUserImg = Utils.findRequiredViewAsType(source, R.id.express_accept_user_img, "field 'expressAcceptUserImg'", SimpleDraweeView.class);
    target.expressAcceptNameTv = Utils.findRequiredViewAsType(source, R.id.express_accept_name_tv, "field 'expressAcceptNameTv'", TextView.class);
    target.expressAcceptNumberTv = Utils.findRequiredViewAsType(source, R.id.express_accept_number_tv, "field 'expressAcceptNumberTv'", TextView.class);
    target.expressAcceptAddressTv = Utils.findRequiredViewAsType(source, R.id.express_accept_address_tv, "field 'expressAcceptAddressTv'", TextView.class);
    target.expressUpdateAcceptAddressTv = Utils.findRequiredViewAsType(source, R.id.express_update_accept_address_tv, "field 'expressUpdateAcceptAddressTv'", TextView.class);
    target.expressOtherConfirmedAddressTv = Utils.findRequiredViewAsType(source, R.id.express_other_confirmed_address_tv, "field 'expressOtherConfirmedAddressTv'", TextView.class);
    target.expressAcceptMessageComfirmedIv = Utils.findRequiredViewAsType(source, R.id.express_accept_message_comfirmed_iv, "field 'expressAcceptMessageComfirmedIv'", ImageView.class);
    target.expressOnTheWayMessageRecordLl = Utils.findRequiredViewAsType(source, R.id.express_onTheWay_message_record_ll, "field 'expressOnTheWayMessageRecordLl'", LinearLayout.class);
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
    target.expressEditEditSendUserImg = null;
    target.expressEditSendNameTv = null;
    target.expressEditSendNumberTv = null;
    target.expressEditSendMessageUserLl = null;
    target.expressOnTheWayEditKdRv = null;
    target.expressOnTheWayEditKdLl = null;
    target.expressNameOfGoodsEt = null;
    target.expressWeightOfGoodsEt = null;
    target.expressCargoMessageEditLl = null;
    target.expressEditSendAddressTv = null;
    target.expressEditUpdateSendAddressTv = null;
    target.itemExpressEditUpdateAddressLl = null;
    target.expressSendMessageEditLl = null;
    target.acceptMessageTextView = null;
    target.expressAcceptUserEditImg = null;
    target.expressAcceptNameEditTv = null;
    target.expressAcceptNumberEditTv = null;
    target.expressAcceptAddressEditTv = null;
    target.expressMessageView = null;
    target.expressOnTheWaySendMessageEditLl = null;
    target.expressOddNumbersTv = null;
    target.expressAddressNameTv = null;
    target.expressRecordRv = null;
    target.expressNullRecordTv = null;
    target.textRecordDateTitle = null;
    target.arrowRecordDateTitleIv = null;
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
    target.textAcceptDateTitle = null;
    target.arrowAcceptDateTitleIv = null;
    target.expressAcceptUserImg = null;
    target.expressAcceptNameTv = null;
    target.expressAcceptNumberTv = null;
    target.expressAcceptAddressTv = null;
    target.expressUpdateAcceptAddressTv = null;
    target.expressOtherConfirmedAddressTv = null;
    target.expressAcceptMessageComfirmedIv = null;
    target.expressOnTheWayMessageRecordLl = null;

    view2131755838.setOnClickListener(null);
    view2131755838 = null;
    view2131755839.setOnClickListener(null);
    view2131755839 = null;

    this.target = null;
  }
}
