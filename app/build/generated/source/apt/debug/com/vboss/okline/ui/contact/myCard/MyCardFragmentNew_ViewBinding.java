// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.myCard;

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

public class MyCardFragmentNew_ViewBinding<T extends MyCardFragmentNew> implements Unbinder {
  protected T target;

  private View view2131755580;

  private View view2131755582;

  private View view2131755460;

  private View view2131755462;

  @UiThread
  public MyCardFragmentNew_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.ivContactAvatarBig = Utils.findRequiredViewAsType(source, R.id.iv_contact_avatar_big, "field 'ivContactAvatarBig'", SimpleDraweeView.class);
    target.tvContactName = Utils.findRequiredViewAsType(source, R.id.tv_contact_name, "field 'tvContactName'", TextView.class);
    target.tvContactPhone = Utils.findRequiredViewAsType(source, R.id.tv_contact_phone, "field 'tvContactPhone'", TextView.class);
    view = Utils.findRequiredView(source, R.id.iv_pencil_otherPhone, "field 'ivPencilOtherPhone' and method 'onViewClicked'");
    target.ivPencilOtherPhone = Utils.castView(view, R.id.iv_pencil_otherPhone, "field 'ivPencilOtherPhone'", ImageView.class);
    view2131755580 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.lvOtherPhone = Utils.findRequiredViewAsType(source, R.id.lv_other_phone, "field 'lvOtherPhone'", ListViewForScrollView.class);
    view = Utils.findRequiredView(source, R.id.iv_pencil_myWorkCard, "field 'ivPencilMyWorkCard' and method 'onViewClicked'");
    target.ivPencilMyWorkCard = Utils.castView(view, R.id.iv_pencil_myWorkCard, "field 'ivPencilMyWorkCard'", ImageView.class);
    view2131755582 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.lvCountInformation = Utils.findRequiredViewAsType(source, R.id.lv_count_information, "field 'lvCountInformation'", ListViewForScrollView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_contact_newcard, "field 'toolbar'", FragmentToolbar.class);
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
    target.lvExpressInfo = Utils.findRequiredViewAsType(source, R.id.lv_express_info, "field 'lvExpressInfo'", ListViewForScrollView.class);
    view = Utils.findRequiredView(source, R.id.iv_edit_delivery, "field 'ivEditDelivery' and method 'onViewClicked'");
    target.ivEditDelivery = Utils.castView(view, R.id.iv_edit_delivery, "field 'ivEditDelivery'", ImageView.class);
    view2131755462 = view;
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

    target.ivContactAvatarBig = null;
    target.tvContactName = null;
    target.tvContactPhone = null;
    target.ivPencilOtherPhone = null;
    target.lvOtherPhone = null;
    target.ivPencilMyWorkCard = null;
    target.lvCountInformation = null;
    target.toolbar = null;
    target.tvCompany = null;
    target.tvPosition = null;
    target.tvAddress = null;
    target.ivPencilAccount = null;
    target.lvExpressInfo = null;
    target.ivEditDelivery = null;
    target.deliveryInfo = null;

    view2131755580.setOnClickListener(null);
    view2131755580 = null;
    view2131755582.setOnClickListener(null);
    view2131755582 = null;
    view2131755460.setOnClickListener(null);
    view2131755460 = null;
    view2131755462.setOnClickListener(null);
    view2131755462 = null;

    this.target = null;
  }
}
