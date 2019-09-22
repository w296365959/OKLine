// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.card.main;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.card.widget.SliderListView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardFragment_ViewBinding<T extends CardFragment> implements Unbinder {
  protected T target;

  @UiThread
  public CardFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.mTabLayout = Utils.findRequiredViewAsType(source, R.id.tab_layout, "field 'mTabLayout'", TabLayout.class);
    target.tv_floating = Utils.findRequiredViewAsType(source, R.id.tv_floating, "field 'tv_floating'", TextView.class);
    target.sliderListView = Utils.findRequiredViewAsType(source, R.id.sliderListView, "field 'sliderListView'", SliderListView.class);
    target.layout_card_empty = Utils.findRequiredViewAsType(source, R.id.layout_card_empty, "field 'layout_card_empty'", LinearLayout.class);
    target.btn_scanner = Utils.findRequiredViewAsType(source, R.id.btn_scanner, "field 'btn_scanner'", ImageButton.class);
    target.btn_receivables = Utils.findRequiredViewAsType(source, R.id.btn_receivables, "field 'btn_receivables'", ImageButton.class);
    target.btn_search = Utils.findRequiredViewAsType(source, R.id.action_menu_button, "field 'btn_search'", ImageButton.class);
    target.ib_drop_add = Utils.findRequiredViewAsType(source, R.id.ib_drop_add, "field 'ib_drop_add'", ImageButton.class);
    target.tv_card_empty = Utils.findRequiredViewAsType(source, R.id.tv_card_empty, "field 'tv_card_empty'", TextView.class);
    target.logoView = Utils.findRequiredViewAsType(source, R.id.logoView, "field 'logoView'", LogoView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mTabLayout = null;
    target.tv_floating = null;
    target.sliderListView = null;
    target.layout_card_empty = null;
    target.btn_scanner = null;
    target.btn_receivables = null;
    target.btn_search = null;
    target.ib_drop_add = null;
    target.tv_card_empty = null;
    target.logoView = null;

    this.target = null;
  }
}
