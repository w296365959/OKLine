// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.addContact;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
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

public class AddCardFragment_ViewBinding<T extends AddCardFragment> implements Unbinder {
  protected T target;

  private View view2131755453;

  private View view2131755454;

  private View view2131755456;

  private View view2131755460;

  private View view2131755462;

  private View view2131755465;

  @UiThread
  public AddCardFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_contact_addcard, "field 'toolbar'", FragmentToolbar.class);
    target.ivContactAvatarBig = Utils.findRequiredViewAsType(source, R.id.iv_contact_avatar_big, "field 'ivContactAvatarBig'", SimpleDraweeView.class);
    target.tvEditName = Utils.findRequiredViewAsType(source, R.id.tv_edit_name, "field 'tvEditName'", TextView.class);
    target.tvEditPhone = Utils.findRequiredViewAsType(source, R.id.tv_edit_phone, "field 'tvEditPhone'", TextView.class);
    view = Utils.findRequiredView(source, R.id.iv_edit_card, "field 'ivEditCard' and method 'onViewClicked'");
    target.ivEditCard = Utils.castView(view, R.id.iv_edit_card, "field 'ivEditCard'", ImageView.class);
    view2131755453 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_other_connection, "field 'ivOtherConnection' and method 'onViewClicked'");
    target.ivOtherConnection = Utils.castView(view, R.id.iv_other_connection, "field 'ivOtherConnection'", ImageView.class);
    view2131755454 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.lvPhone = Utils.findRequiredViewAsType(source, R.id.lv_phone, "field 'lvPhone'", ListViewForScrollView.class);
    view = Utils.findRequiredView(source, R.id.iv_pencil_workCard, "field 'ivPencilWorkCard' and method 'onViewClicked'");
    target.ivPencilWorkCard = Utils.castView(view, R.id.iv_pencil_workCard, "field 'ivPencilWorkCard'", ImageView.class);
    view2131755456 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvCompany = Utils.findRequiredViewAsType(source, R.id.tv_company, "field 'tvCompany'", TextView.class);
    target.tvPosition = Utils.findRequiredViewAsType(source, R.id.tv_position, "field 'tvPosition'", TextView.class);
    target.tvAddress = Utils.findRequiredViewAsType(source, R.id.tv_address, "field 'tvAddress'", TextView.class);
    view = Utils.findRequiredView(source, R.id.iv_pencil_account, "field 'ivPencilAccount' and method 'onViewClicked'");
    target.ivPencilAccount = Utils.castView(view, R.id.iv_pencil_account, "field 'ivPencilAccount'", ImageView.class);
    view2131755460 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.lvCountInformation = Utils.findRequiredViewAsType(source, R.id.lv_count_information, "field 'lvCountInformation'", ListViewForScrollView.class);
    view = Utils.findRequiredView(source, R.id.iv_edit_delivery, "field 'ivEditDelivery' and method 'onViewClicked'");
    target.ivEditDelivery = Utils.castView(view, R.id.iv_edit_delivery, "field 'ivEditDelivery'", ImageView.class);
    view2131755462 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.lvExpressInfo = Utils.findRequiredViewAsType(source, R.id.lv_express_info, "field 'lvExpressInfo'", ListViewForScrollView.class);
    target.tvMyDeliveryInfo = Utils.findRequiredViewAsType(source, R.id.tv_my_delivery_info, "field 'tvMyDeliveryInfo'", TextView.class);
    view = Utils.findRequiredView(source, R.id.tv_save_card, "field 'tvSaveCard' and method 'onViewClicked'");
    target.tvSaveCard = Utils.castView(view, R.id.tv_save_card, "field 'tvSaveCard'", TextView.class);
    view2131755465 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.ivContactAvatarBig = null;
    target.tvEditName = null;
    target.tvEditPhone = null;
    target.ivEditCard = null;
    target.ivOtherConnection = null;
    target.lvPhone = null;
    target.ivPencilWorkCard = null;
    target.tvCompany = null;
    target.tvPosition = null;
    target.tvAddress = null;
    target.ivPencilAccount = null;
    target.lvCountInformation = null;
    target.ivEditDelivery = null;
    target.lvExpressInfo = null;
    target.tvMyDeliveryInfo = null;
    target.tvSaveCard = null;

    view2131755453.setOnClickListener(null);
    view2131755453 = null;
    view2131755454.setOnClickListener(null);
    view2131755454 = null;
    view2131755456.setOnClickListener(null);
    view2131755456 = null;
    view2131755460.setOnClickListener(null);
    view2131755460 = null;
    view2131755462.setOnClickListener(null);
    view2131755462 = null;
    view2131755465.setOnClickListener(null);
    view2131755465 = null;

    this.target = null;
  }
}
