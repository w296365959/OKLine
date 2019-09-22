// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.card.log;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardLogFragment_ViewBinding<T extends CardLogFragment> implements Unbinder {
  protected T target;

  @UiThread
  public CardLogFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recycleView, "field 'recyclerView'", RecyclerView.class);
    target.iv_card_date = Utils.findRequiredViewAsType(source, R.id.iv_card_date, "field 'iv_card_date'", ImageView.class);
    target.tv_card_name = Utils.findRequiredViewAsType(source, R.id.tv_card_name, "field 'tv_card_name'", TextView.class);
    target.tv_card_number = Utils.findRequiredViewAsType(source, R.id.tv_card_number, "field 'tv_card_number'", TextView.class);
    target.tv_card_balance = Utils.findRequiredViewAsType(source, R.id.tv_card_balance, "field 'tv_card_balance'", TextView.class);
    target.tv_card_integral = Utils.findRequiredViewAsType(source, R.id.tv_card_integral, "field 'tv_card_integral'", TextView.class);
    target.simple_card = Utils.findRequiredViewAsType(source, R.id.sdv_card, "field 'simple_card'", SimpleDraweeView.class);
    target.simple_card_logo = Utils.findRequiredViewAsType(source, R.id.sdv_card_app, "field 'simple_card_logo'", SimpleDraweeView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.fragment_toolbar, "field 'toolbar'", FragmentToolbar.class);
    target.classicFrameLayout = Utils.findRequiredViewAsType(source, R.id.trans_ptrFrameLayout, "field 'classicFrameLayout'", PtrClassicFrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.recyclerView = null;
    target.iv_card_date = null;
    target.tv_card_name = null;
    target.tv_card_number = null;
    target.tv_card_balance = null;
    target.tv_card_integral = null;
    target.simple_card = null;
    target.simple_card_logo = null;
    target.toolbar = null;
    target.classicFrameLayout = null;

    this.target = null;
  }
}
