// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.record.organization;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.view.widget.OKCardView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OrganizationDetailFragment_ViewBinding<T extends OrganizationDetailFragment> implements Unbinder {
  protected T target;

  @UiThread
  public OrganizationDetailFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.actionBack = Utils.findRequiredViewAsType(source, R.id.action_back, "field 'actionBack'", ImageButton.class);
    target.actionBackLayout = Utils.findRequiredViewAsType(source, R.id.action_back_layout, "field 'actionBackLayout'", RelativeLayout.class);
    target.sdvLogo = Utils.findRequiredViewAsType(source, R.id.sdv_logo, "field 'sdvLogo'", SimpleDraweeView.class);
    target.actionTitle = Utils.findRequiredViewAsType(source, R.id.action_title, "field 'actionTitle'", TextView.class);
    target.actionMenuButton = Utils.findRequiredViewAsType(source, R.id.action_menu_button, "field 'actionMenuButton'", ImageButton.class);
    target.actionMenuLayout = Utils.findRequiredViewAsType(source, R.id.action_menu_layout, "field 'actionMenuLayout'", RelativeLayout.class);
    target.sdvCard = Utils.findRequiredViewAsType(source, R.id.sdv_card, "field 'sdvCard'", SimpleDraweeView.class);
    target.tvCardNum = Utils.findRequiredViewAsType(source, R.id.tv_card_num, "field 'tvCardNum'", TextView.class);
    target.tvCardBalance = Utils.findRequiredViewAsType(source, R.id.tv_card_balance, "field 'tvCardBalance'", TextView.class);
    target.tvCardCredits = Utils.findRequiredViewAsType(source, R.id.tv_card_credits, "field 'tvCardCredits'", TextView.class);
    target.ivCardDate = Utils.findRequiredViewAsType(source, R.id.iv_card_date, "field 'ivCardDate'", ImageView.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
    target.ptrFrameLayout = Utils.findRequiredViewAsType(source, R.id.ptrFrameLayout, "field 'ptrFrameLayout'", PtrClassicFrameLayout.class);
    target.ivOcardState = Utils.findRequiredViewAsType(source, R.id.iv_ocard_state, "field 'ivOcardState'", LogoView.class);
    target.okcardView = Utils.findRequiredViewAsType(source, R.id.okcard_view, "field 'okcardView'", OKCardView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.actionBack = null;
    target.actionBackLayout = null;
    target.sdvLogo = null;
    target.actionTitle = null;
    target.actionMenuButton = null;
    target.actionMenuLayout = null;
    target.sdvCard = null;
    target.tvCardNum = null;
    target.tvCardBalance = null;
    target.tvCardCredits = null;
    target.ivCardDate = null;
    target.recyclerView = null;
    target.ptrFrameLayout = null;
    target.ivOcardState = null;
    target.okcardView = null;

    this.target = null;
  }
}
