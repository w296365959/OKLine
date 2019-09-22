// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.ContactDetail;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.view.widget.ListViewForScrollView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ContactDetailFragment_ViewBinding<T extends ContactDetailFragment> implements Unbinder {
  protected T target;

  private View view2131756085;

  private View view2131756088;

  private View view2131756089;

  private View view2131756090;

  private View view2131756094;

  private View view2131756091;

  private View view2131756095;

  private View view2131755580;

  private View view2131755456;

  private View view2131755460;

  private View view2131756104;

  @UiThread
  public ContactDetailFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.sdvDetailAvatar = Utils.findRequiredViewAsType(source, R.id.sdv_detail_avatar, "field 'sdvDetailAvatar'", SimpleDraweeView.class);
    target.tvDetailRemark = Utils.findRequiredViewAsType(source, R.id.tv_detail_remark, "field 'tvDetailRemark'", TextView.class);
    view = Utils.findRequiredView(source, R.id.iv_pencil, "field 'ivPencil' and method 'onViewClicked'");
    target.ivPencil = Utils.castView(view, R.id.iv_pencil, "field 'ivPencil'", ImageView.class);
    view2131756085 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_detail_phone, "field 'ivDetailPhone' and method 'onViewClicked'");
    target.ivDetailPhone = Utils.castView(view, R.id.iv_detail_phone, "field 'ivDetailPhone'", ImageView.class);
    view2131756088 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_detail_chat, "field 'ivDetailChat' and method 'onViewClicked'");
    target.ivDetailChat = Utils.castView(view, R.id.iv_detail_chat, "field 'ivDetailChat'", ImageView.class);
    view2131756089 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_detail_email, "field 'ivDetailEmail' and method 'onViewClicked'");
    target.ivDetailEmail = Utils.castView(view, R.id.iv_detail_email, "field 'ivDetailEmail'", ImageView.class);
    view2131756090 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_detail_delivery, "field 'ivDetailDelivery' and method 'onViewClicked'");
    target.ivDetailDelivery = Utils.castView(view, R.id.iv_detail_delivery, "field 'ivDetailDelivery'", ImageView.class);
    view2131756094 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_detail_transfer, "field 'ivDetailTransfer' and method 'onViewClicked'");
    target.ivDetailTransfer = Utils.castView(view, R.id.iv_detail_transfer, "field 'ivDetailTransfer'", ImageView.class);
    view2131756091 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ll_card_detail, "field 'llCardDetail' and method 'onViewClicked'");
    target.llCardDetail = Utils.castView(view, R.id.ll_card_detail, "field 'llCardDetail'", LinearLayout.class);
    view2131756095 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.ivUpwards = Utils.findRequiredViewAsType(source, R.id.iv_upwards, "field 'ivUpwards'", ImageView.class);
    target.llDetailShow = Utils.findRequiredViewAsType(source, R.id.ll_detail_show, "field 'llDetailShow'", LinearLayout.class);
    target.tvDetailRealName = Utils.findRequiredViewAsType(source, R.id.tv_detail_realName, "field 'tvDetailRealName'", TextView.class);
    target.tvDetailRealName2 = Utils.findRequiredViewAsType(source, R.id.tv_detail_realName2, "field 'tvDetailRealName2'", TextView.class);
    target.tvDetailPhoneNum = Utils.findRequiredViewAsType(source, R.id.tv_detail_phoneNum, "field 'tvDetailPhoneNum'", TextView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_contact, "field 'toolbar'", FragmentToolbar.class);
    target.ivContactState = Utils.findRequiredViewAsType(source, R.id.iv_contact_state, "field 'ivContactState'", ImageView.class);
    target.contactTransferAccountNum = Utils.findRequiredViewAsType(source, R.id.contact_transfer_account_num, "field 'contactTransferAccountNum'", TextView.class);
    target.transferAccountsDot = Utils.findRequiredViewAsType(source, R.id.transfer_accounts_dot, "field 'transferAccountsDot'", RelativeLayout.class);
    target.flChatContent = Utils.findRequiredViewAsType(source, R.id.fl_chat_content, "field 'flChatContent'", FrameLayout.class);
    target.llNameLayout = Utils.findRequiredViewAsType(source, R.id.ll_name_layout, "field 'llNameLayout'", LinearLayout.class);
    target.ivPencil1 = Utils.findRequiredViewAsType(source, R.id.iv_pencil1, "field 'ivPencil1'", ImageView.class);
    target.lvOtherPhone = Utils.findRequiredViewAsType(source, R.id.lv_other_phone, "field 'lvOtherPhone'", ListViewForScrollView.class);
    target.tvCompany = Utils.findRequiredViewAsType(source, R.id.tv_company, "field 'tvCompany'", TextView.class);
    target.tvPosition = Utils.findRequiredViewAsType(source, R.id.tv_position, "field 'tvPosition'", TextView.class);
    target.tvAddress = Utils.findRequiredViewAsType(source, R.id.tv_address, "field 'tvAddress'", TextView.class);
    target.lvCountInformation = Utils.findRequiredViewAsType(source, R.id.lv_count_information, "field 'lvCountInformation'", ListViewForScrollView.class);
    target.lvExpressInfo = Utils.findRequiredViewAsType(source, R.id.lv_express_info, "field 'lvExpressInfo'", ListViewForScrollView.class);
    target.identityInfo = Utils.findRequiredViewAsType(source, R.id.tv_identity_info, "field 'identityInfo'", TextView.class);
    target.identityName = Utils.findRequiredViewAsType(source, R.id.ll_identity_name, "field 'identityName'", LinearLayout.class);
    target.identityPhone = Utils.findRequiredViewAsType(source, R.id.ll_identity_phone, "field 'identityPhone'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.iv_pencil_otherPhone, "field 'ivPencilOtherPhone' and method 'onViewClicked'");
    target.ivPencilOtherPhone = Utils.castView(view, R.id.iv_pencil_otherPhone, "field 'ivPencilOtherPhone'", ImageView.class);
    view2131755580 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_pencil_workCard, "field 'ivPencilWorkCard' and method 'onViewClicked'");
    target.ivPencilWorkCard = Utils.castView(view, R.id.iv_pencil_workCard, "field 'ivPencilWorkCard'", ImageView.class);
    view2131755456 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_pencil_account, "field 'ivPencilAccount' and method 'onViewClicked'");
    target.ivPencilAccount = Utils.castView(view, R.id.iv_pencil_account, "field 'ivPencilAccount'", ImageView.class);
    view2131755460 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_pencil_delivery, "field 'ivPencilDelivery' and method 'onViewClicked'");
    target.ivPencilDelivery = Utils.castView(view, R.id.iv_pencil_delivery, "field 'ivPencilDelivery'", ImageView.class);
    view2131756104 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.deliveryInfo = Utils.findRequiredViewAsType(source, R.id.tv_my_delivery_info, "field 'deliveryInfo'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.sdvDetailAvatar = null;
    target.tvDetailRemark = null;
    target.ivPencil = null;
    target.ivDetailPhone = null;
    target.ivDetailChat = null;
    target.ivDetailEmail = null;
    target.ivDetailDelivery = null;
    target.ivDetailTransfer = null;
    target.llCardDetail = null;
    target.ivUpwards = null;
    target.llDetailShow = null;
    target.tvDetailRealName = null;
    target.tvDetailRealName2 = null;
    target.tvDetailPhoneNum = null;
    target.toolbar = null;
    target.ivContactState = null;
    target.contactTransferAccountNum = null;
    target.transferAccountsDot = null;
    target.flChatContent = null;
    target.llNameLayout = null;
    target.ivPencil1 = null;
    target.lvOtherPhone = null;
    target.tvCompany = null;
    target.tvPosition = null;
    target.tvAddress = null;
    target.lvCountInformation = null;
    target.lvExpressInfo = null;
    target.identityInfo = null;
    target.identityName = null;
    target.identityPhone = null;
    target.ivPencilOtherPhone = null;
    target.ivPencilWorkCard = null;
    target.ivPencilAccount = null;
    target.ivPencilDelivery = null;
    target.deliveryInfo = null;

    view2131756085.setOnClickListener(null);
    view2131756085 = null;
    view2131756088.setOnClickListener(null);
    view2131756088 = null;
    view2131756089.setOnClickListener(null);
    view2131756089 = null;
    view2131756090.setOnClickListener(null);
    view2131756090 = null;
    view2131756094.setOnClickListener(null);
    view2131756094 = null;
    view2131756091.setOnClickListener(null);
    view2131756091 = null;
    view2131756095.setOnClickListener(null);
    view2131756095 = null;
    view2131755580.setOnClickListener(null);
    view2131755580 = null;
    view2131755456.setOnClickListener(null);
    view2131755456 = null;
    view2131755460.setOnClickListener(null);
    view2131755460 = null;
    view2131756104.setOnClickListener(null);
    view2131756104 = null;

    this.target = null;
  }
}
