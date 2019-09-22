// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.record;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.user.customized.NonScrollableViewPager;
import com.vboss.okline.view.widget.OKCardView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RecordFragment_ViewBinding<T extends RecordFragment> implements Unbinder {
  protected T target;

  @UiThread
  public RecordFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.slidingTabs = Utils.findRequiredViewAsType(source, R.id.sliding_tabs, "field 'slidingTabs'", TabLayout.class);
    target.vpRecord = Utils.findRequiredViewAsType(source, R.id.vp_record, "field 'vpRecord'", NonScrollableViewPager.class);
    target.action_back = Utils.findRequiredViewAsType(source, R.id.action_back, "field 'action_back'", ImageButton.class);
    target.action_menu = Utils.findRequiredViewAsType(source, R.id.action_menu_button, "field 'action_menu'", ImageButton.class);
    target.action_title = Utils.findRequiredViewAsType(source, R.id.action_title, "field 'action_title'", TextView.class);
    target.action_logo = Utils.findRequiredViewAsType(source, R.id.sdv_logo, "field 'action_logo'", SimpleDraweeView.class);
    target.actionMenuLayout = Utils.findRequiredViewAsType(source, R.id.action_menu_layout, "field 'actionMenuLayout'", RelativeLayout.class);
    target.ivOcardState = Utils.findRequiredViewAsType(source, R.id.iv_ocard_state, "field 'ivOcardState'", LogoView.class);
    target.okcardView = Utils.findRequiredViewAsType(source, R.id.okcard_view, "field 'okcardView'", OKCardView.class);
    target.actionBackLayout = Utils.findRequiredViewAsType(source, R.id.action_back_layout, "field 'actionBackLayout'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.slidingTabs = null;
    target.vpRecord = null;
    target.action_back = null;
    target.action_menu = null;
    target.action_title = null;
    target.action_logo = null;
    target.actionMenuLayout = null;
    target.ivOcardState = null;
    target.okcardView = null;
    target.actionBackLayout = null;

    this.target = null;
  }
}
